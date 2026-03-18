# Email Verification API Test Samples

## Chuẩn bị
Đảm bảo đã cấu hình SMTP trong `application.yaml`:
```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: your-email@gmail.com
    password: your-app-password
```

## 1. Đăng ký tài khoản mới (sẽ gửi email xác thực)

**POST** `http://localhost:8080/identity/users`

**Headers:**
```
Content-Type: application/json
```

**Body:**
```json
{
  "username": "testuser",
  "firstname": "Test",
  "lastname": "User",
  "email": "testuser@example.com",
  "password": "password123",
  "dateOfBirth": "2000-01-01"
}
```

**Expected Response:**
```json
{
  "code": 1000,
  "result": {
    "id": "uuid-here",
    "username": "testuser",
    "firstname": "Test",
    "lastname": "User",
    "email": "testuser@example.com",
    "dateOfBirth": "2000-01-01",
    "enabled": false,
    "emailVerified": false,
    "roles": []
  }
}
```

📧 **Kiểm tra email:** User sẽ nhận được email với subject "Xác thực tài khoản - Build PC Checker"

---

## 2. Thử đăng nhập khi chưa xác thực email (sẽ bị từ chối)

**POST** `http://localhost:8080/identity/auth/token`

**Headers:**
```
Content-Type: application/json
```

**Body:**
```json
{
  "email": "testuser@example.com",
  "password": "password123"
}
```

**Expected Response (Error):**
```json
{
  "code": 1011,
  "message": "Email is not verified. Please verify your email first"
}
```

---

## 3. Xác thực email bằng token

### Cách 1: Sử dụng link trong email
User click vào link trong email, ví dụ:
```
http://localhost:8080/identity/auth/verify-email?token=abc-123-def-456
```

### Cách 2: Test bằng API
**GET** `http://localhost:8080/identity/auth/verify-email?token=YOUR_TOKEN_HERE`

**Expected Response:**
```json
{
  "code": 1000,
  "message": "Email verified successfully! You can now login.",
  "result": {
    "id": "uuid-here",
    "username": "testuser",
    "firstname": "Test",
    "lastname": "User",
    "email": "testuser@example.com",
    "dateOfBirth": "2000-01-01",
    "enabled": true,
    "emailVerified": true,
    "roles": []
  }
}
```

---

## 4. Đăng nhập sau khi xác thực email thành công

**POST** `http://localhost:8080/identity/auth/token`

**Headers:**
```
Content-Type: application/json
```

**Body:**
```json
{
  "email": "testuser@example.com",
  "password": "password123"
}
```

**Expected Response (Success):**
```json
{
  "code": 1000,
  "result": {
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0dXNlckBleGFtcGxlLmNvbSIsImlzcyI6Imhhb2Fib3V0bWUuY29tIiwiaWF0IjoxNzA3ODMwNDAwLCJleHAiOjE3MDc4MzA3MDAsImp0aSI6InV1aWQtaGVyZSIsInNjb3BlIjoiIn0.signature-here",
    "authenticated": true
  }
}
```

---

## 5. Gửi lại email xác thực (nếu token hết hạn hoặc không nhận được email)

**POST** `http://localhost:8080/identity/auth/resend-verification?email=testuser@example.com`

**Expected Response:**
```json
{
  "code": 1000,
  "message": "Please check your email inbox and spam folder.",
  "result": "Verification email has been sent to: testuser@example.com"
}
```

---

## 6. Test với Admin account (không cần xác thực)

Admin account được tạo sẵn với email verified = true

**POST** `http://localhost:8080/identity/auth/token`

**Headers:**
```
Content-Type: application/json
```

**Body:**
```json
{
  "email": "haoaboutme@gmail.com",
  "password": "admin"
}
```

**Expected Response (Success - không cần verify):**
```json
{
  "code": 1000,
  "result": {
    "token": "eyJhbGciOiJIUzUxMiJ9...",
    "authenticated": true
  }
}
```

---

## Error Cases

### 1. Token không hợp lệ hoặc đã hết hạn (sau 24 giờ)
**GET** `http://localhost:8080/identity/auth/verify-email?token=invalid-token`

**Response:**
```json
{
  "code": 1010,
  "message": "Invalid or expired verification token"
}
```

### 2. Email đã được xác thực rồi
**GET** `http://localhost:8080/identity/auth/verify-email?token=used-token`

**Response:**
```json
{
  "code": 1012,
  "message": "Email is already verified"
}
```

### 3. Gửi lại email cho tài khoản đã xác thực
**POST** `http://localhost:8080/identity/auth/resend-verification?email=verified@example.com`

**Response:**
```json
{
  "code": 1012,
  "message": "Email is already verified"
}
```

### 4. Gửi lại email cho tài khoản không tồn tại
**POST** `http://localhost:8080/identity/auth/resend-verification?email=notexist@example.com`

**Response:**
```json
{
  "code": 1006,
  "message": "User not exist"
}
```

---

## Testing Flow Recommendations

### Test Case 1: Happy Path (Đăng ký thành công)
1. Đăng ký tài khoản mới → Nhận response với `enabled: false`, `emailVerified: false`
2. Kiểm tra email inbox (hoặc spam folder)
3. Click link xác thực trong email → Nhận response success
4. Đăng nhập thành công → Nhận JWT token

### Test Case 2: Đăng nhập trước khi xác thực
1. Đăng ký tài khoản mới
2. Thử đăng nhập ngay → Nhận error 1011 (Email not verified)

### Test Case 3: Token hết hạn
1. Đăng ký tài khoản mới
2. Đợi 24 giờ (hoặc thay đổi expiry time trong code để test nhanh)
3. Click link xác thực → Nhận error 1010 (Invalid token)
4. Gửi lại email xác thực → Nhận email mới
5. Click link mới → Xác thực thành công

### Test Case 4: Gửi lại email
1. Đăng ký tài khoản mới
2. Gọi API resend-verification
3. Kiểm tra email → Nhận email mới
4. Token cũ sẽ không còn hiệu lực
5. Sử dụng token mới để xác thực

---

## cURL Commands (Alternative)

### Đăng ký
```bash
curl -X POST http://localhost:8080/identity/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "firstname": "Test",
    "lastname": "User",
    "email": "testuser@example.com",
    "password": "password123",
    "dateOfBirth": "2000-01-01"
  }'
```

### Xác thực email
```bash
curl -X GET "http://localhost:8080/identity/auth/verify-email?token=YOUR_TOKEN"
```

### Gửi lại email
```bash
curl -X POST "http://localhost:8080/identity/auth/resend-verification?email=testuser@example.com"
```

### Đăng nhập
```bash
curl -X POST http://localhost:8080/identity/auth/token \
  -H "Content-Type: application/json" \
  -d '{
    "email": "testuser@example.com",
    "password": "password123"
  }'
```

---

## Database Verification

### Kiểm tra User table
```sql
SELECT id, username, email, enabled, email_verified 
FROM user 
WHERE email = 'testuser@example.com';
```

**Before verification:**
```
enabled: false
email_verified: false
```

**After verification:**
```
enabled: true
email_verified: true
```

### Kiểm tra VerificationToken table
```sql
SELECT * FROM verification_token 
WHERE user_id = 'user-uuid-here';
```

**Note:** Token sẽ bị xóa sau khi xác thực thành công hoặc khi gửi lại email mới.

---

## Tips

1. **Kiểm tra Spam Folder:** Gmail có thể đưa email vào spam
2. **Email Delay:** Có thể mất 1-2 phút để nhận email
3. **Token Expiry:** Token hết hạn sau 24 giờ
4. **Base URL:** Thay đổi `app.base-url` trong application.yaml khi deploy production
5. **Testing Email:** Có thể dùng Mailtrap hoặc MailHog để test local

---

## Troubleshooting

### Email không gửi được
- Kiểm tra logs xem có lỗi gì không
- Verify SMTP credentials trong application.yaml
- Đảm bảo đã tạo App Password cho Gmail (không dùng password thường)

### Token không hợp lệ
- Token chỉ dùng 1 lần
- Token hết hạn sau 24 giờ
- Token bị xóa khi request gửi lại email mới

### Vẫn không đăng nhập được sau khi verify
- Kiểm tra database: `enabled` và `email_verified` phải là `true`
- Kiểm tra password có đúng không
- Xem logs để biết lỗi cụ thể

