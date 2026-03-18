# Hướng dẫn thiết lập Email Verification

## Tính năng đã được triển khai

Hệ thống xác thực email đã được thêm vào với các tính năng sau:

### 1. **Khi đăng ký tài khoản mới:**
   - User sẽ nhận được email xác thực
   - Tài khoản sẽ ở trạng thái `enabled = false` và `emailVerified = false`
   - User không thể đăng nhập cho đến khi xác thực email

### 2. **Các endpoint mới:**
   - `GET /identity/auth/verify-email?token={token}` - Xác thực email
   - `POST /identity/auth/resend-verification?email={email}` - Gửi lại email xác thực

### 3. **Các ErrorCode mới:**
   - `INVALID_VERIFICATION_TOKEN` (1010) - Token không hợp lệ hoặc đã hết hạn
   - `EMAIL_NOT_VERIFIED` (1011) - Email chưa được xác thực
   - `EMAIL_ALREADY_VERIFIED` (1012) - Email đã được xác thực
   - `ACCOUNT_DISABLED` (1013) - Tài khoản bị vô hiệu hóa

## Cấu hình Gmail SMTP

### Bước 1: Tạo App Password cho Gmail

1. Truy cập: https://myaccount.google.com/security
2. Bật **2-Step Verification** (Xác thực 2 bước) nếu chưa bật
3. Sau khi bật 2-Step Verification, tìm **App passwords** (Mật khẩu ứng dụng)
4. Chọn **Mail** và **Other (Custom name)**
5. Nhập tên: `Build PC Checker`
6. Google sẽ tạo mật khẩu 16 ký tự (VD: `abcd efgh ijkl mnop`)
7. Copy mật khẩu này (không có khoảng trắng)

### Bước 2: Cập nhật application.yaml

```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: your-email@gmail.com        # Thay bằng email của bạn
    password: abcdefghijklmnop            # Thay bằng App Password (không có khoảng trắng)
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
            required: true
          connectiontimeout: 5000
          timeout: 5000
          writetimeout: 5000

app:
  base-url: http://localhost:8080/identity  # URL base của ứng dụng (thay đổi khi deploy)
```

### Bước 3: Cài đặt dependency (đã thêm vào pom.xml)

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
</dependency>
```

Chạy lệnh sau để tải dependency:
```bash
mvn clean install
```

## Cấu trúc Database mới

### Bảng `verification_token`:
```sql
CREATE TABLE verification_token (
    id VARCHAR(36) PRIMARY KEY,
    token VARCHAR(255) NOT NULL,
    user_id VARCHAR(36) NOT NULL,
    expiry_date DATETIME NOT NULL,
    created_at DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(id)
);
```

### Cập nhật bảng `user`:
```sql
ALTER TABLE user 
ADD COLUMN enabled BOOLEAN DEFAULT FALSE,
ADD COLUMN email_verified BOOLEAN DEFAULT FALSE;
```

**Lưu ý:** Hibernate sẽ tự động tạo/cập nhật bảng khi chạy ứng dụng (do `ddl-auto: update`)

## Cách sử dụng

### 1. Đăng ký tài khoản mới

**Request:**
```bash
POST http://localhost:8080/identity/users
Content-Type: application/json

{
  "username": "johndoe",
  "firstname": "John",
  "lastname": "Doe",
  "email": "john.doe@example.com",
  "password": "password123",
  "dateOfBirth": "2000-01-01"
}
```

**Response:**
```json
{
  "code": 1000,
  "result": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "username": "johndoe",
    "email": "john.doe@example.com",
    "enabled": false,
    "emailVerified": false
  }
}
```

User sẽ nhận được email với link xác thực.

### 2. Xác thực email

User click vào link trong email hoặc gọi API:

**Request:**
```bash
GET http://localhost:8080/identity/auth/verify-email?token=abc-123-def-456
```

**Response:**
```json
{
  "code": 1000,
  "message": "Email verified successfully! You can now login.",
  "result": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "username": "johndoe",
    "email": "john.doe@example.com",
    "enabled": true,
    "emailVerified": true
  }
}
```

### 3. Gửi lại email xác thực

Nếu user không nhận được email hoặc token hết hạn:

**Request:**
```bash
POST http://localhost:8080/identity/auth/resend-verification?email=john.doe@example.com
```

**Response:**
```json
{
  "code": 1000,
  "message": "Please check your email inbox and spam folder.",
  "result": "Verification email has been sent to: john.doe@example.com"
}
```

### 4. Đăng nhập

User phải xác thực email trước khi đăng nhập:

**Request:**
```bash
POST http://localhost:8080/identity/auth/token
Content-Type: application/json

{
  "email": "john.doe@example.com",
  "password": "password123"
}
```

**Nếu email chưa xác thực - Response:**
```json
{
  "code": 1011,
  "message": "Email is not verified. Please verify your email first"
}
```

**Nếu email đã xác thực - Response:**
```json
{
  "code": 1000,
  "result": {
    "token": "eyJhbGciOiJIUzUxMiJ9...",
    "authenticated": true
  }
}
```

## Lưu ý quan trọng

### 1. Token expiry:
- Token xác thực có hiệu lực trong **24 giờ**
- Sau 24 giờ, user cần yêu cầu gửi lại email

### 2. Security:
- Token chỉ được sử dụng 1 lần
- Sau khi xác thực thành công, token sẽ bị xóa khỏi database

### 3. Admin account:
- Admin account được tạo sẵn với `enabled = true` và `emailVerified = true`
- Admin không cần xác thực email

### 4. Testing với Mailtrap (Optional):

Nếu chưa muốn dùng Gmail thật, có thể test với Mailtrap:

```yaml
spring:
  mail:
    host: smtp.mailtrap.io
    port: 2525
    username: your-mailtrap-username
    password: your-mailtrap-password
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
```

Đăng ký miễn phí tại: https://mailtrap.io/

## Troubleshooting

### Lỗi: "Could not autowire JavaMailSender"
- Đảm bảo đã thêm dependency `spring-boot-starter-mail` vào pom.xml
- Chạy `mvn clean install`

### Lỗi: "Failed to send email"
- Kiểm tra email và App Password trong application.yaml
- Đảm bảo đã bật 2-Step Verification cho Gmail
- Kiểm tra firewall có chặn port 587 không

### Token không hợp lệ
- Token có thể đã hết hạn (sau 24 giờ)
- Yêu cầu gửi lại email xác thực

### Email không đến
- Kiểm tra thư mục Spam
- Đợi vài phút (có thể bị delay)
- Yêu cầu gửi lại email

## Mở rộng trong tương lai

1. **Password Reset**: Chức năng reset mật khẩu qua email
2. **Email Templates**: Thiết kế email đẹp hơn với HTML/CSS
3. **Rate Limiting**: Giới hạn số lần gửi email trong 1 khoảng thời gian
4. **Multi-language Support**: Hỗ trợ nhiều ngôn ngữ cho email
5. **SMS Verification**: Xác thực qua SMS (tùy chọn)

## File đã được tạo/chỉnh sửa

1. **Entity:**
   - `VerificationToken.java` (NEW)
   - `User.java` (UPDATED - thêm enabled, emailVerified)

2. **Repository:**
   - `VerificationTokenRepository.java` (NEW)

3. **Service:**
   - `EmailService.java` (NEW)
   - `UserService.java` (UPDATED)
   - `AuthenticationService.java` (UPDATED)

4. **Controller:**
   - `AuthenticationController.java` (UPDATED)

5. **Configuration:**
   - `ApplicationInitConfig.java` (UPDATED)
   - `application.yaml` (UPDATED)

6. **Exception:**
   - `ErrorCode.java` (UPDATED)

7. **Dependencies:**
   - `pom.xml` (UPDATED)

