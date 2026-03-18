# Hướng dẫn Setup CORS cho Frontend

## 🔧 Đã cấu hình

### 1. CORS Configuration
File: `src/main/java/com/j2ee/buildpcchecker/configuration/CorsConfig.java`

**Cho phép các origin sau:**
- `http://127.0.0.1:5500` ✅
- `http://localhost:5500` ✅
- `http://127.0.0.1:5501` ✅
- `http://localhost:5501` ✅
- `http://127.0.0.1:5502` ✅
- `http://localhost:5502` ✅

**Cho phép các HTTP Methods:**
- GET, POST, PUT, DELETE, OPTIONS, PATCH

**Cho phép:**
- Tất cả headers
- Credentials (cookies, authorization headers)
- Preflight cache: 3600 seconds (1 giờ)

---

## 🚀 Cách chạy Frontend với Live Server

### Bước 1: Khởi động Backend
```bash
# Từ thư mục gốc của project
mvnw spring-boot:run

# Hoặc nếu đã build
java -jar target/buildpcchecker-0.0.1-SNAPSHOT.jar
```

Backend sẽ chạy tại: **http://localhost:8080**

### Bước 2: Khởi động Frontend với Live Server
1. Mở VSCode
2. Install extension **Live Server** (nếu chưa có)
3. Tạo thư mục frontend (ví dụ: `frontend/` hoặc `public/`)
4. Tạo file `index.html` trong thư mục đó
5. Click chuột phải vào file `index.html` → **Open with Live Server**

Frontend sẽ chạy tại: **http://127.0.0.1:5500** (hoặc port khác)

---

## 📝 Ví dụ Test CORS

### Test với HTML đơn giản

Tạo file `test-cors.html`:

```html
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Test CORS</title>
</head>
<body>
    <h1>Test CORS với Backend</h1>
    <button onclick="testConnection()">Test Connection</button>
    <div id="result"></div>

    <script>
        async function testConnection() {
            const resultDiv = document.getElementById('result');
            resultDiv.innerHTML = '<p>Đang kết nối...</p>';

            try {
                // Test đăng ký user mới
                const response = await fetch('http://localhost:8080/identity/users', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        username: 'testuser_' + Date.now(),
                        firstname: 'Test',
                        lastname: 'User',
                        email: 'test' + Date.now() + '@example.com',
                        password: 'password123',
                        dateOfBirth: '2000-01-01'
                    })
                });

                const data = await response.json();
                
                if (data.code === 1000) {
                    resultDiv.innerHTML = `
                        <p style="color: green;">✅ CORS hoạt động tốt!</p>
                        <pre>${JSON.stringify(data, null, 2)}</pre>
                    `;
                } else {
                    resultDiv.innerHTML = `
                        <p style="color: orange;">⚠️ API response:</p>
                        <pre>${JSON.stringify(data, null, 2)}</pre>
                    `;
                }
            } catch (error) {
                resultDiv.innerHTML = `
                    <p style="color: red;">❌ Lỗi CORS hoặc kết nối!</p>
                    <pre>${error.message}</pre>
                `;
            }
        }
    </script>
</body>
</html>
```

---

## 🔍 Kiểm tra CORS trong Browser

### Chrome DevTools
1. Mở trang frontend (http://127.0.0.1:5500)
2. Nhấn **F12** để mở DevTools
3. Vào tab **Console**
4. Gọi API và xem kết quả

**Nếu CORS hoạt động đúng:**
- Không có lỗi CORS trong Console
- API trả về dữ liệu thành công

**Nếu có lỗi CORS:**
```
Access to fetch at 'http://localhost:8080/identity/...' from origin 'http://127.0.0.1:5500' 
has been blocked by CORS policy: No 'Access-Control-Allow-Origin' header is present...
```

### Network Tab
1. Vào tab **Network** trong DevTools
2. Gọi API từ frontend
3. Click vào request để xem details
4. Kiểm tra **Response Headers** phải có:
   ```
   Access-Control-Allow-Origin: http://127.0.0.1:5500
   Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS, PATCH
   Access-Control-Allow-Headers: *
   Access-Control-Allow-Credentials: true
   ```

---

## ⚙️ Thêm Origin mới (nếu cần)

Nếu bạn sử dụng port khác, thêm vào `CorsConfig.java`:

```java
configuration.setAllowedOrigins(Arrays.asList(
    "http://127.0.0.1:5500",
    "http://localhost:5500",
    "http://127.0.0.1:5501",
    "http://localhost:5501",
    "http://127.0.0.1:5502",
    "http://localhost:5502",
    "http://127.0.0.1:8000",     // Thêm port mới
    "http://localhost:8000"       // Thêm port mới
));
```

Sau đó **restart backend**.

---

## 🛠️ Troubleshooting

### Vấn đề 1: Vẫn bị lỗi CORS
**Giải pháp:**
1. Restart backend sau khi thay đổi config
2. Clear browser cache (Ctrl + F5)
3. Kiểm tra port của Live Server (phải khớp với config)

### Vấn đề 2: Preflight request bị fail
**Nguyên nhân:** OPTIONS request không được phép

**Giải pháp:** Đã được xử lý trong config, OPTIONS method được cho phép.

### Vấn đề 3: Authorization header không được gửi
**Nguyên nhân:** `allowCredentials` chưa được set

**Giải pháp:** Đã được set `configuration.setAllowCredentials(true)` trong config.

---

## 📚 API Base URL trong Frontend

Trong code JavaScript, sử dụng:

```javascript
const API_BASE_URL = 'http://localhost:8080/identity';

// Ví dụ login
async function login(email, password) {
    const response = await fetch(`${API_BASE_URL}/auth/token`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ email, password })
    });
    
    return await response.json();
}

// Ví dụ gọi API với JWT token
async function getUsers() {
    const token = localStorage.getItem('token');
    
    const response = await fetch(`${API_BASE_URL}/users`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    });
    
    return await response.json();
}
```

---

## ✅ Checklist Setup

- [x] Tạo file `CorsConfig.java`
- [x] Cập nhật `SecurityConfig.java` để enable CORS
- [x] Thêm origins cho Live Server (port 5500, 5501, 5502)
- [x] Cho phép credentials
- [x] Cho phép tất cả HTTP methods cần thiết
- [x] Cho phép tất cả headers

---

## 🎯 Kết luận

CORS đã được cấu hình đầy đủ cho frontend chạy trên Live Server. Bạn có thể:

1. ✅ Gọi API từ `http://127.0.0.1:5500`
2. ✅ Gửi JWT token trong Authorization header
3. ✅ Sử dụng tất cả HTTP methods (GET, POST, PUT, DELETE)
4. ✅ Không gặp lỗi CORS khi develop

**Lưu ý:** Khi deploy production, nhớ cập nhật `allowedOrigins` với domain thật của bạn!

---

**Last Updated:** 2026-02-02  
**Author:** BuildPC Checker Team

