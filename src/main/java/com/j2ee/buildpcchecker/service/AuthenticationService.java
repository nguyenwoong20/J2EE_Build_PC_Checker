package com.j2ee.buildpcchecker.service;

import com.j2ee.buildpcchecker.dto.request.AuthenticationRequest;
import com.j2ee.buildpcchecker.dto.request.ForgotPasswordRequest;
import com.j2ee.buildpcchecker.dto.request.IntrospectRequest;
import com.j2ee.buildpcchecker.dto.request.LogOutRequest;
import com.j2ee.buildpcchecker.dto.request.RefreshRequest;
import com.j2ee.buildpcchecker.dto.request.ResetPasswordRequest;
import com.j2ee.buildpcchecker.dto.response.AuthenticationResponse;
import com.j2ee.buildpcchecker.dto.response.IntrospectResponse;
import com.j2ee.buildpcchecker.entity.InvalidatedToken;
import com.j2ee.buildpcchecker.entity.PasswordResetOtp;
import com.j2ee.buildpcchecker.entity.User;
import com.j2ee.buildpcchecker.exception.AppException;
import com.j2ee.buildpcchecker.exception.ErrorCode;
import com.j2ee.buildpcchecker.repository.InvalidatedTokenRepository;
import com.j2ee.buildpcchecker.repository.PasswordResetOtpRepository;
import com.j2ee.buildpcchecker.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Random;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService
{
    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;
    PasswordResetOtpRepository passwordResetOtpRepository;
    EmailService emailService;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException
    {
        var token = request.getToken();
        boolean isValid = true;

        try {
            verifyToken(token, false);
        }
        catch (AppException ex)
        {
            isValid = false;
        }

        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request)
    {
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        //Math 2 password của request và user trong database
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if(!authenticated)
        {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        // Kiểm tra email đã được xác thực chưa
        if (!user.getEmailVerified())
        {
            throw new AppException(ErrorCode.EMAIL_NOT_VERIFIED);
        }

        // Kiểm tra tài khoản đã được kích hoạt chưa
        if (!user.getEnabled())
        {
            throw new AppException(ErrorCode.ACCOUNT_DISABLED);
        }

        var token = generateToken(user);
        return AuthenticationResponse.builder()
                .isAuthenticated(true)
                .token(token)
                .build();
    }

    public void logout(LogOutRequest request) throws ParseException, JOSEException
    {
        try {
            var signedToken = verifyToken(request.getToken(), true);

            String jti = signedToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signedToken.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(jti)
                    .expiryTime(expiryTime)
                    .build();

            invalidatedTokenRepository.save(invalidatedToken);
        } catch (AppException exception) {
            log.info("Token already invalidated");
        }
    }

    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException
    {
        var signedToken = verifyToken(request.getToken(), true);

        var jti = signedToken.getJWTClaimsSet().getJWTID();
        var expiryTime = signedToken.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jti)
                .expiryTime(expiryTime)
                .build();

        invalidatedTokenRepository.save(invalidatedToken);

        var email = signedToken.getJWTClaimsSet().getSubject();
        var user = userRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.UNAUTHENTICATED)
        );

        var token = generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .isAuthenticated(true)
                .build();
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws ParseException, JOSEException
    {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expirationTime = (isRefresh)
                ? new Date(signedJWT.getJWTClaimsSet().getIssueTime()
                        .toInstant().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS).toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        if(!(verified && expirationTime.after(new Date())))
        {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        if(invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
        {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        return signedJWT;
    }

    private String generateToken(User user)
    {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("haoaboutme.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        }
        catch (JOSEException ex)
        {
            log.error("Cannot generate token", ex);
            throw new RuntimeException(ex);
        }
    }

    private String buildScope(User user)
    {
        StringJoiner joiner = new StringJoiner(" ");
        if(!user.getRoles().isEmpty())
        {
            user.getRoles().forEach(role -> {
                joiner.add("ROLE_" + role.getName());
                if(!CollectionUtils.isEmpty(role.getPermissions()))
                {
                    role.getPermissions().forEach(permission -> {
                        joiner.add(permission.getName());
                    });
                }
            });
        }
        return joiner.toString();
    }

    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        // 1. Kiểm tra email tồn tại
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // 2. Tài khoản phải đã verify email
        if (!user.getEmailVerified()) {
            throw new AppException(ErrorCode.EMAIL_NOT_VERIFIED);
        }

        // 3. Xóa OTP cũ của email này (tránh nhiễu, chỉ 1 OTP active tại 1 thời điểm)
        passwordResetOtpRepository.deleteAllByEmail(request.getEmail());

        // 4. Tạo OTP 6 chữ số ngẫu nhiên
        String otp = String.format("%06d", new Random().nextInt(1000000));

        // 5. Lưu OTP vào database
        PasswordResetOtp resetOtp = PasswordResetOtp.builder()
                .email(request.getEmail())
                .otp(otp)
                .build();
        passwordResetOtpRepository.save(resetOtp);

        // 6. Gửi OTP qua email
        emailService.sendOtpEmail(request.getEmail(), otp);

        log.info("OTP sent to email: {}", request.getEmail());
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        // 1. Tìm OTP hợp lệ: đúng email + đúng mã + chưa dùng + chưa hết hạn
        PasswordResetOtp resetOtp = passwordResetOtpRepository
                .findByEmailAndOtpAndIsUsedFalseAndExpiryDateAfter(
                        request.getEmail(),
                        request.getOtp(),
                        LocalDateTime.now()
                )
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_OTP));

        // 2. Tìm user theo email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // 3. Cập nhật password mới (hash bcrypt)
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        // 4. Đánh dấu OTP đã dùng (không cho dùng lại)
        resetOtp.setIsUsed(true);
        passwordResetOtpRepository.save(resetOtp);

        log.info("Password reset successfully for email: {}", request.getEmail());
    }
}
