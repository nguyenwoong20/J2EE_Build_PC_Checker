# Swagger UI - Hướng Dẫn Sử Dụng

## ✅ Đã Cài Đặt

Swagger đã được tích hợp vào dự án với các thành phần sau:

### 1. Dependencies (pom.xml)
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.3.0</version>
</dependency>
```

### 2. Configuration (application.yaml)
```yaml
springdoc:
  api-docs:
    path: /api-docs
    enabled: true
  swagger-ui:
    path: /swagger-ui.html
    enabled: true
    operations-sorter: method
    tags-sorter: alpha
    try-it-out-enabled: true
    filter: true
    display-request-duration: true
```

### 3. OpenAPI Config (OpenApiConfig.java)
- Cấu hình thông tin API
- Server URL
- API version và description

---

## 🚀 Cách Sử Dụng

### Bước 1: Chạy Application
```bash
# Sử dụng Maven
mvn spring-boot:run

# Hoặc run file JAR
java -jar target/buildpcchecker-0.0.1-SNAPSHOT.jar
```

### Bước 2: Truy Cập Swagger UI
Mở trình duyệt và truy cập một trong các URL sau:

**Swagger UI:**
```
http://localhost:8080/identity/swagger-ui.html
```
hoặc
```
http://localhost:8080/identity/swagger-ui/index.html
```

**API Docs (JSON):**
```
http://localhost:8080/identity/api-docs
```

---

## 📋 Các Tính Năng

### 1. **Tự Động Quét Tất Cả Endpoints**
- Swagger tự động phát hiện tất cả các `@RestController`
- Hiển thị tất cả endpoints (GET, POST, PUT, DELETE)
- Không cần thêm annotations gì thêm!

### 2. **Giao Diện Đẹp và Dễ Sử Dụng**
- Các endpoints được nhóm theo Controller
- Sắp xếp alphabetically
- Có filter để tìm kiếm nhanh

### 3. **Try It Out - Test API Trực Tiếp**
- Click vào endpoint bất kỳ
- Click nút **"Try it out"**
- Nhập dữ liệu vào form
- Click **"Execute"**
- Xem kết quả ngay lập tức

### 4. **Schema Models**
- Hiển thị cấu trúc dữ liệu của Request/Response
- Validation constraints
- Data types
- Required fields

---

## 🎯 Ví Dụ Sử Dụng

### Test API Case Size

#### 1. Tạo Case Size Mới
1. Mở Swagger UI: `http://localhost:8080/identity/swagger-ui.html`
2. Tìm endpoint **POST /case-sizes**
3. Click **"Try it out"**
4. Nhập dữ liệu vào Request body:
```json
{
  "id": "ATX",
  "name": "ATX Full Tower"
}
```
5. Click **"Execute"**
6. Xem Response:
```json
{
  "code": 1000,
  "message": "Success",
  "result": {
    "id": "ATX",
    "name": "ATX Full Tower"
  }
}
```

#### 2. Lấy Danh Sách Case Sizes
1. Tìm endpoint **GET /case-sizes**
2. Click **"Try it out"**
3. Click **"Execute"**
4. Xem danh sách tất cả case sizes

#### 3. Tạo PC Case với Size
1. Tìm endpoint **POST /cases**
2. Click **"Try it out"**
3. Nhập dữ liệu:
```json
{
  "name": "NZXT H510",
  "sizeId": "ATX",
  "maxVgaLengthMm": 381,
  "maxCoolerHeightMm": 165,
  "maxRadiatorSize": 280,
  "drive35Slot": 2,
  "drive25Slot": 3,
  "description": "Compact ATX case"
}
```
4. Click **"Execute"**

---

## 🔍 Các Endpoints Có Sẵn

Swagger sẽ tự động hiển thị tất cả endpoints từ các Controller:

### Case Size Controller (`/case-sizes`)
- `POST /case-sizes` - Tạo mới
- `GET /case-sizes` - Lấy tất cả
- `GET /case-sizes/{id}` - Lấy theo ID
- `PUT /case-sizes/{id}` - Cập nhật
- `DELETE /case-sizes/{id}` - Xóa

### Case Controller (`/cases`)
- `POST /cases` - Tạo mới
- `GET /cases` - Lấy tất cả
- `GET /cases/{id}` - Lấy theo ID
- `PUT /cases/{id}` - Cập nhật
- `DELETE /cases/{id}` - Xóa

### CPU Controller (`/cpus`)
- Tương tự...

### Mainboard Controller (`/mainboards`)
- Tương tự...

... và tất cả các controllers khác trong dự án!

---

## 💡 Tips & Tricks

### 1. **Sử dụng Filter**
- Gõ tên endpoint vào ô search để lọc nhanh
- VD: gõ "case" để chỉ hiển thị các endpoints liên quan đến case

### 2. **Xem Request/Response Schema**
- Click vào **"Schemas"** ở cuối trang
- Xem cấu trúc chi tiết của tất cả DTO

### 3. **Copy Request Examples**
- Swagger tự động generate example data từ các field types
- Copy và modify theo nhu cầu

### 4. **Response Codes**
- Swagger hiển thị tất cả HTTP status codes có thể
- 200: Success
- 400: Bad Request
- 404: Not Found
- 401: Unauthorized

---

## 🔧 Tùy Chỉnh (Nếu Cần)

### Thêm Description cho Controller
Nếu muốn thêm mô tả cho Controller, có thể thêm annotation:
```java
@Tag(name = "Case Size", description = "APIs for managing PC case sizes")
@RestController
@RequestMapping("/case-sizes")
public class CaseSizeController {
    // ...
}
```

### Thêm Example cho Request
```java
@Schema(description = "Case Size ID", example = "ATX")
String id;
```

**Nhưng điều này KHÔNG BẮT BUỘC!** Swagger vẫn hoạt động tốt mà không cần các annotation này.

---

## ⚠️ Lưu Ý

### Context Path
Application đang chạy với context path `/identity`, nên:
- Base URL: `http://localhost:8080/identity`
- Swagger UI: `http://localhost:8080/identity/swagger-ui.html`
- API Docs: `http://localhost:8080/identity/api-docs`

### Security
Nếu API có bật authentication, bạn cần:
1. Click nút **"Authorize"** ở góc trên bên phải
2. Nhập JWT token
3. Click **"Authorize"**
4. Sau đó mới test được các secured endpoints

---

## 🎉 So Sánh với Postman

| Feature | Postman | Swagger UI |
|---------|---------|------------|
| Auto-discover endpoints | ❌ Phải tạo thủ công | ✅ Tự động |
| Request examples | ❌ Phải nhập | ✅ Có sẵn |
| Schema documentation | ❌ Không có | ✅ Có đầy đủ |
| Test API | ✅ Có | ✅ Có |
| Save history | ✅ Có | ❌ Không |
| Environment variables | ✅ Có | ❌ Không |
| Team collaboration | ✅ Có (paid) | ✅ Share URL |

**Kết luận:** 
- Swagger UI tốt cho **development** và **documentation**
- Postman tốt cho **testing** phức tạp và **automation**
- Nên dùng cả hai!

---

## 🐛 Troubleshooting

### Swagger UI không hiển thị
1. Kiểm tra application đã chạy chưa
2. Kiểm tra URL có đúng không (nhớ có `/identity` ở đầu)
3. Check logs xem có lỗi không

### Không thấy một số endpoints
1. Kiểm tra Controller có annotation `@RestController` không
2. Kiểm tra `@RequestMapping` có đúng không
3. Restart application

### Test API báo lỗi 401
1. API có security
2. Click nút **"Authorize"** 
3. Nhập JWT token
4. Thử lại

---

## 📚 Tài Liệu Tham Khảo

- Springdoc OpenAPI: https://springdoc.org/
- Swagger UI: https://swagger.io/tools/swagger-ui/
- OpenAPI Specification: https://swagger.io/specification/

---

**Chúc bạn test API vui vẻ! 🚀**

