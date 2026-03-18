# ✅ JWT Authentication Header Đã Được Thêm Vào Swagger UI

## 🎯 Thay Đổi

Đã thêm **JWT Bearer Authentication** vào Swagger UI configuration.

## 🔧 Cập Nhật OpenApiConfig

### File: `OpenApiConfig.java`

```java
@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "Bearer Authentication";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer))
                // ✅ Thêm Security Requirement
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                // ✅ Thêm Security Scheme
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, createSecurityScheme()));
    }

    private SecurityScheme createSecurityScheme() {
        return new SecurityScheme()
                .name("Bearer Authentication")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .description("Enter JWT token (without 'Bearer' prefix)");
    }
}
```

## 🚀 Cách Sử Dụng

### Bước 1: Start Application
```bash
mvn spring-boot:run
```

### Bước 2: Mở Swagger UI
```
http://localhost:8080/identity/swagger-ui.html
```

### Bước 3: Login Để Lấy Token

#### 3.1. Tạo User (Nếu Chưa Có)
1. Tìm endpoint: **POST /users**
2. Click **"Try it out"**
3. Nhập data:
```json
{
  "username": "testuser",
  "password": "Test@123",
  "email": "test@example.com",
  "firstName": "Test",
  "lastName": "User",
  "dob": "1990-01-01"
}
```
4. Click **"Execute"**

#### 3.2. Login
1. Tìm endpoint: **POST /auth/token**
2. Click **"Try it out"**
3. Nhập credentials:
```json
{
  "username": "testuser",
  "password": "Test@123"
}
```
4. Click **"Execute"**
5. **Copy token** từ response:
```json
{
  "code": 1000,
  "result": {
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImlhdCI6MTY...",
    "authenticated": true
  }
}
```

### Bước 4: Authorize Swagger UI

#### 4.1. Click Nút "Authorize" 🔓
- Nút này nằm ở **góc trên bên phải** Swagger UI
- Có icon ổ khóa 🔓

#### 4.2. Nhập JWT Token
- **KHÔNG cần** thêm "Bearer " prefix
- Chỉ cần paste token thôi:
```
eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImlhdCI6MTY...
```

#### 4.3. Click "Authorize"
- Click button **"Authorize"**
- Click **"Close"** để đóng popup

### Bước 5: Test Protected APIs

Bây giờ có thể test bất kỳ protected endpoint nào:

#### Test Case Size API:
```
1. POST /case-sizes
   Body: {"id":"ATX","name":"ATX Full Tower"}
   
2. GET /case-sizes
   → Xem tất cả case sizes
   
3. PUT /case-sizes/ATX
   → Update case size
```

#### Test PC Case API:
```
1. POST /cases
   Body: {
     "name":"NZXT H510",
     "sizeId":"ATX",
     "maxVgaLengthMm":381,
     "maxCoolerHeightMm":165,
     "maxRadiatorSize":280,
     "drive35Slot":2,
     "drive25Slot":3
   }
   
2. GET /cases
   → Xem tất cả cases
```

Tất cả requests sẽ **tự động include** JWT token trong header:
```
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

## 🎨 Giao Diện Swagger UI

### Trước Khi Authorize:
- Nút: **🔓 Authorize** (màu xám)
- Status: Chưa authenticated

### Sau Khi Authorize:
- Nút: **🔒 Authorize** (có màu)
- Status: Đã authenticated
- Mỗi endpoint sẽ có icon ổ khóa 🔒 (chỉ protected endpoints)

## 📊 Public vs Protected Endpoints

### Public Endpoints (Không Cần Token):
| Endpoint | Method | Mô Tả |
|----------|--------|-------|
| /users | POST | Đăng ký user |
| /auth/token | POST | Login |
| /auth/introspect | POST | Validate token |
| /auth/verify-email | POST | Xác thực email |
| /auth/resend-verification | GET | Gửi lại email |

**→ Test ngay không cần Authorize**

### Protected Endpoints (Cần JWT Token):
| Endpoint Group | Mô Tả |
|----------------|-------|
| /case-sizes/* | CRUD Case Sizes |
| /cases/* | CRUD PC Cases |
| /cpus/* | CRUD CPUs |
| /mainboards/* | CRUD Mainboards |
| /rams/* | CRUD RAMs |
| /vgas/* | CRUD VGAs |
| /ssds/* | CRUD SSDs |
| /hdds/* | CRUD HDDs |
| /psus/* | CRUD PSUs |
| /coolers/* | CRUD Coolers |

**→ Phải Authorize trước khi test**

## 🔍 Troubleshooting

### Lỗi 401 Unauthorized
**Nguyên nhân:**
- Token chưa được set
- Token đã hết hạn (expired)
- Token không hợp lệ

**Giải pháp:**
1. Click "Authorize" lại
2. Login lại để lấy token mới
3. Paste token mới
4. Click "Authorize"

### Token Hết Hạn
**Token có thời gian sống:**
- `valid-duration: 300` seconds (5 phút)
- Sau 5 phút phải login lại

**Giải pháp:**
1. POST /auth/token lại
2. Copy token mới
3. Authorize lại

### Không Thấy Nút Authorize
**Nguyên nhân:**
- OpenApiConfig chưa có SecurityScheme
- Application chưa restart

**Giải pháp:**
1. Restart application
2. Hard refresh browser (Ctrl+F5)

## 🎯 Best Practices

### 1. Test Workflow Chuẩn:
```
1. Tạo user (POST /users) - 1 lần
2. Login (POST /auth/token) - Khi cần token
3. Authorize với token
4. Test protected APIs
5. Khi token hết hạn → Login lại
```

### 2. Security Note:
- **Development:** Token ngắn (5 phút) để test
- **Production:** Nên dài hơn (30 phút - 1 giờ)
- Luôn logout khi xong việc

### 3. Multiple Users:
- Có thể tạo nhiều users với roles khác nhau
- Test authorization với từng role

## 🎉 Kết Quả

### ✅ Bây Giờ Có:
- [x] Nút **Authorize** ở góc trên phải
- [x] JWT token field trong authorize popup
- [x] Auto include token trong tất cả requests
- [x] Icon ổ khóa 🔒 trên protected endpoints
- [x] Description: "Enter JWT token (without 'Bearer' prefix)"

### ✅ Có Thể:
- [x] Test public APIs không cần token
- [x] Authorize một lần, test nhiều APIs
- [x] Re-authorize khi token hết hạn
- [x] Professional API documentation với security

## 📚 References

- OpenAPI Security: https://swagger.io/docs/specification/authentication/
- JWT Bearer: https://swagger.io/docs/specification/authentication/bearer-authentication/
- SpringDoc Security: https://springdoc.org/#how-can-i-configure-swagger-ui

---

**Swagger UI giờ đã có Authentication Header hoàn chỉnh! 🎊**

**Workflow:** Login → Copy Token → Authorize → Test APIs! 🚀

