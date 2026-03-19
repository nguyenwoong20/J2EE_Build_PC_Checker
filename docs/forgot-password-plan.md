# 🔐 Kế Hoạch Triển Khai: Forgot Password (OTP via Gmail)

> **Dự án:** Build PC Checker  
> **Ngày lập:** 2026-03-19  
> **Phạm vi:** Backend Spring Boot

---

## 📋 Tổng Quan Luồng

```
[Client] ---(1) POST /auth/forgot-password {email}---> [Server]
                                                           |
                                                    Tạo OTP 6 số
                                                    Lưu vào DB (5 phút)
                                                    Gửi email OTP
                                                           |
[Client] <----------(2) "OTP sent to email"-------------- [Server]

[Client] ---(3) POST /auth/reset-password {email, otp, newPassword}---> [Server]
                                                                            |
                                                                     Xác thực OTP
                                                                     Xác thực email khớp
                                                                     Đổi password
                                                                     Xóa OTP đã dùng
                                                                            |
[Client] <----------(4) "Password reset successful"-------------------- [Server]
```

---

## 🗄️ Bước 1: Tạo Entity `PasswordResetOtp`

**File:** `src/main/java/com/j2ee/buildpcchecker/entity/PasswordResetOtp.java`

```java
@Entity
@Table(name = "password_reset_otp")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PasswordResetOtp {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(nullable = false)
    String email;           // Email người yêu cầu reset

    @Column(nullable = false, length = 6)
    String otp;             // Mã OTP 6 chữ số

    @Column(name = "expiry_date", nullable = false)
    LocalDateTime expiryDate;

    @Column(name = "is_used", nullable = false)
    Boolean isUsed;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        expiryDate = LocalDateTime.now().plusMinutes(5); // OTP hết hạn sau 5 phút
        isUsed = false;
    }
}
```

> **Lý do dùng bảng riêng thay vì tái dùng `VerificationToken`:**  
> `VerificationToken` dùng cho email verification (24h, UUID token), còn OTP có TTL ngắn (5 phút), format khác (6 chữ số), và cần track trạng thái `isUsed` để chống replay attack.

---

## 🗃️ Bước 2: Tạo Repository `PasswordResetOtpRepository`

**File:** `src/main/java/com/j2ee/buildpcchecker/repository/PasswordResetOtpRepository.java`

```java
public interface PasswordResetOtpRepository extends JpaRepository<PasswordResetOtp, String> {

    // Tìm OTP hợp lệ: đúng email, đúng mã, chưa dùng, chưa hết hạn
    Optional<PasswordResetOtp> findByEmailAndOtpAndIsUsedFalseAndExpiryDateAfter(
            String email, String otp, LocalDateTime now);

    // Xóa toàn bộ OTP cũ của email (trước khi tạo OTP mới)
    void deleteAllByEmail(String email);
    
    // Xóa OTP đã hết hạn (dùng cho scheduled cleanup - optional)
    void deleteAllByExpiryDateBefore(LocalDateTime now);
}
```

---

## 📨 Bước 3: Thêm Method Gửi OTP vào `EmailService`

**File:** `src/main/java/com/j2ee/buildpcchecker/service/EmailService.java`

Thêm method mới (giữ nguyên `sendVerificationEmail` hiện tại):

```java
public void sendOtpEmail(String toEmail, String otp) {
    try {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(fromEmail);
        helper.setTo(toEmail);
        helper.setSubject("Mã OTP Đặt Lại Mật Khẩu - Build PC Checker");

        String htmlContent = buildOtpEmailTemplate(otp);
        helper.setText(htmlContent, true);

        mailSender.send(message);
        log.info("OTP email sent to: {}", toEmail);
    } catch (MessagingException e) {
        log.error("Failed to send OTP email to: {}", toEmail, e);
        throw new RuntimeException("Failed to send OTP email", e);
    }
}

private String buildOtpEmailTemplate(String otp) {
    return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: 'Segoe UI', sans-serif; background: linear-gradient(135deg, #667eea, #764ba2); padding: 40px 20px; }
                    .container { max-width: 600px; margin: 0 auto; background: #fff; border-radius: 16px; overflow: hidden; box-shadow: 0 10px 40px rgba(0,0,0,0.2); }
                    .header { background: linear-gradient(135deg, #667eea, #764ba2); padding: 40px 30px; text-align: center; color: white; }
                    .header h1 { font-size: 26px; margin-bottom: 8px; }
                    .content { padding: 40px 30px; }
                    .otp-box { background: #f0f4ff; border: 2px dashed #667eea; border-radius: 12px; text-align: center; padding: 24px; margin: 24px 0; }
                    .otp-code { font-size: 48px; font-weight: 800; letter-spacing: 12px; color: #667eea; font-family: monospace; }
                    .warning-box { background: #fff3cd; border-left: 4px solid #ffc107; padding: 15px; border-radius: 8px; margin: 20px 0; }
                    .warning-box p { color: #856404; margin: 0; font-size: 14px; }
                    .footer { background: #f8f9fa; text-align: center; padding: 24px; font-size: 13px; color: #6c757d; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>🔐 Đặt Lại Mật Khẩu</h1>
                        <p>Build PC Checker</p>
                    </div>
                    <div class="content">
                        <p>Chúng tôi nhận được yêu cầu đặt lại mật khẩu cho tài khoản của bạn.</p>
                        <p>Sử dụng mã OTP dưới đây để đặt lại mật khẩu:</p>
                        <div class="otp-box">
                            <div class="otp-code">%s</div>
                        </div>
                        <div class="warning-box">
                            <p>⏰ <strong>Mã OTP có hiệu lực trong 5 phút.</strong> Không chia sẻ mã này với bất kỳ ai.</p>
                        </div>
                        <p style="color: #888; font-size: 14px; margin-top: 20px;">Nếu bạn không yêu cầu đặt lại mật khẩu, hãy bỏ qua email này. Tài khoản của bạn vẫn an toàn.</p>
                    </div>
                    <div class="footer">
                        <p><strong>Build PC Checker</strong> — Hệ thống kiểm tra tương thích linh kiện PC</p>
                        <p>© 2026 Build PC Checker. All rights reserved.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(otp);
}
```

---

## 📦 Bước 4: Tạo DTOs

### 4.1 `ForgotPasswordRequest.java`
**File:** `src/main/java/com/j2ee/buildpcchecker/dto/request/ForgotPasswordRequest.java`

```java
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ForgotPasswordRequest {

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    String email;
}
```

### 4.2 `ResetPasswordRequest.java`
**File:** `src/main/java/com/j2ee/buildpcchecker/dto/request/ResetPasswordRequest.java`

```java
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResetPasswordRequest {

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    String email;

    @NotBlank(message = "OTP không được để trống")
    @Size(min = 6, max = 6, message = "OTP phải có đúng 6 chữ số")
    String otp;

    @NotBlank(message = "Mật khẩu mới không được để trống")
    @Size(min = 8, message = "Mật khẩu phải có ít nhất 8 ký tự")
    String newPassword;
}
```

---

## ⚙️ Bước 5: Thêm Logic vào `AuthenticationService`

**File:** `src/main/java/com/j2ee/buildpcchecker/service/AuthenticationService.java`

Inject thêm các dependency:
```java
final PasswordResetOtpRepository passwordResetOtpRepository;
final EmailService emailService;
final PasswordEncoder passwordEncoder; // BCryptPasswordEncoder
```

Thêm 2 method mới:

### 5.1 `forgotPassword()`

```java
public void forgotPassword(ForgotPasswordRequest request) {
    // 1. Kiểm tra email tồn tại trong hệ thống
    User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

    // 2. Kiểm tra tài khoản đã verify email chưa (không reset pass cho acc chưa active)
    if (!user.getEmailVerified()) {
        throw new AppException(ErrorCode.EMAIL_NOT_VERIFIED);
    }

    // 3. Xóa toàn bộ OTP cũ của email này (tránh nhiễu)
    passwordResetOtpRepository.deleteAllByEmail(request.getEmail());

    // 4. Tạo OTP 6 chữ số ngẫu nhiên
    String otp = String.format("%06d", new Random().nextInt(999999));

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
```

### 5.2 `resetPassword()`

```java
public void resetPassword(ResetPasswordRequest request) {
    // 1. Tìm OTP hợp lệ: đúng email + đúng mã + chưa dùng + chưa hết hạn
    PasswordResetOtp resetOtp = passwordResetOtpRepository
            .findByEmailAndOtpAndIsUsedFalseAndExpiryDateAfter(
                    request.getEmail(),
                    request.getOtp(),
                    LocalDateTime.now()
            )
            .orElseThrow(() -> new AppException(ErrorCode.INVALID_OTP));

    // 2. Tìm user theo email (đảm bảo email khớp với OTP)
    User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

    // 3. Cập nhật password mới (hash bcrypt)
    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    userRepository.save(user);

    // 4. Đánh dấu OTP đã dùng (không cho dùng lại)
    resetOtp.setIsUsed(true);
    passwordResetOtpRepository.save(resetOtp);

    log.info("Password reset successfully for email: {}", request.getEmail());
}
```

---

## 🌐 Bước 6: Thêm Endpoint vào `AuthenticationController`

**File:** `src/main/java/com/j2ee/buildpcchecker/controller/AuthenticationController.java`

```java
// POST /auth/forgot-password
@PostMapping("/forgot-password")
ApiResponse<String> forgotPassword(@RequestBody @Valid ForgotPasswordRequest request) {
    authenticationService.forgotPassword(request);
    return ApiResponse.<String>builder()
            .result("OTP đã được gửi đến email: " + request.getEmail())
            .message("Vui lòng kiểm tra hộp thư và nhập OTP để đặt lại mật khẩu. OTP có hiệu lực trong 5 phút.")
            .build();
}

// POST /auth/reset-password
@PostMapping("/reset-password")
ApiResponse<String> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
    authenticationService.resetPassword(request);
    return ApiResponse.<String>builder()
            .result("Đặt lại mật khẩu thành công")
            .message("Bạn có thể đăng nhập bằng mật khẩu mới.")
            .build();
}
```

---

## 🚨 Bước 7: Thêm Error Code

**File:** `src/main/java/com/j2ee/buildpcchecker/exception/ErrorCode.java`

```java
INVALID_OTP(1023, "OTP không hợp lệ hoặc đã hết hạn", HttpStatus.BAD_REQUEST),
OTP_EXPIRED(1024, "OTP đã hết hạn, vui lòng yêu cầu gửi lại", HttpStatus.BAD_REQUEST),
```

---

## 🔒 Bước 8: Cấu Hình Security (Cho phép truy cập không cần token)

**File:** `src/main/java/com/j2ee/buildpcchecker/configuration/SecurityConfig.java` (hoặc tương đương)

Thêm 2 endpoint mới vào danh sách `permitAll`:

```java
"/auth/forgot-password",
"/auth/reset-password",
```

---

## 🗄️ Bước 9: Migration Database

Tạo bảng mới trong database (nếu không dùng `spring.jpa.hibernate.ddl-auto=update`):

```sql
CREATE TABLE password_reset_otp (
    id          VARCHAR(36)  PRIMARY KEY,
    email       VARCHAR(255) NOT NULL,
    otp         VARCHAR(6)   NOT NULL,
    expiry_date DATETIME     NOT NULL,
    is_used     BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at  DATETIME     NOT NULL,
    INDEX idx_email (email),
    INDEX idx_expiry (expiry_date)
);
```

---

## 📊 Tổng Hợp Các File Cần Tạo/Sửa

| Hành động | File |
|:---:|---|
| ✅ Tạo mới | `entity/PasswordResetOtp.java` |
| ✅ Tạo mới | `repository/PasswordResetOtpRepository.java` |
| ✅ Tạo mới | `dto/request/ForgotPasswordRequest.java` |
| ✅ Tạo mới | `dto/request/ResetPasswordRequest.java` |
| ✏️ Sửa | `service/EmailService.java` — thêm `sendOtpEmail()` |
| ✏️ Sửa | `service/AuthenticationService.java` — thêm `forgotPassword()`, `resetPassword()` |
| ✏️ Sửa | `controller/AuthenticationController.java` — thêm 2 endpoint |
| ✏️ Sửa | `exception/ErrorCode.java` — thêm `INVALID_OTP` |
| ✏️ Sửa | `configuration/SecurityConfig.java` — permitAll 2 endpoint mới |

---

## 🧪 Test Cases

### Happy Path
```
# Bước 1: Gửi yêu cầu OTP
POST /auth/forgot-password
{ "email": "user@gmail.com" }
→ 200 OK, OTP gửi về Gmail

# Bước 2: Đặt lại mật khẩu
POST /auth/reset-password
{ "email": "user@gmail.com", "otp": "123456", "newPassword": "NewPass@123" }
→ 200 OK, đổi mật khẩu thành công
```

### Error Cases
| Scenario | Expected Response |
|---|---|
| Email không tồn tại | `USER_NOT_EXISTED` |
| Email chưa verify | `EMAIL_NOT_VERIFIED` |
| OTP sai | `INVALID_OTP` |
| OTP đã hết hạn (> 5 phút) | `INVALID_OTP` |
| OTP đã dùng rồi | `INVALID_OTP` |
| Email trong request reset ≠ email khi request OTP | `INVALID_OTP` |

---

## 🛡️ Lưu Ý Bảo Mật

- **OTP 6 số** → 1,000,000 khả năng, đủ an toàn cho TTL 5 phút
- **TTL ngắn (5 phút)** → giảm window tấn công brute force
- **`isUsed` flag** → chống tái sử dụng OTP đã reset thành công
- **Xóa OTP cũ** trước khi tạo mới → tránh nhiễu, chỉ 1 OTP active tại mỗi thời điểm
- **Không trả về thông báo khác nhau** cho "email không tồn tại" vs "OTP sai" ở production → tránh user enumeration (optional hardening)
- **Rate limiting** (optional): Giới hạn số lần gọi `/forgot-password` per email per giờ để tránh spam
