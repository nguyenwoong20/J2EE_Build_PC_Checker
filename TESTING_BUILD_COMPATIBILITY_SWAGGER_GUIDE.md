# 🧪 Hướng Dẫn Test API Check Build Compatibility Trong Swagger

## 📋 TỔNG QUAN DỰ ÁN BUILD PC CHECKER

### 🎯 Các Chức Năng Chính

#### 1️⃣ **Authentication & Authorization**
- **Authentication Controller** (`/auth`)
  - `POST /auth/token` - Đăng nhập và lấy JWT token
  - `POST /auth/introspect` - Kiểm tra token hợp lệ
  - `POST /auth/logout` - Đăng xuất
  - `POST /auth/refresh` - Refresh token
  - `POST /auth/outbound/authentication` - OAuth2 đăng nhập (Google)
  - `POST /auth/send-verification-email` - Gửi email xác thực
  - `POST /auth/verify-email` - Xác thực email

#### 2️⃣ **User Management**
- **User Controller** (`/users`)
  - `POST /users` - Tạo user mới
  - `GET /users` - Lấy danh sách users
  - `GET /users/me` - Lấy thông tin user hiện tại
  - `GET /users/{userId}` - Lấy user theo ID
  - `PUT /users/{userId}` - Cập nhật user
  - `PUT /users/me` - Cập nhật thông tin cá nhân
  - `PUT /users/change-password` - Đổi mật khẩu

- **Role Controller** (`/roles`)
  - CRUD operations cho Role

- **Permission Controller** (`/permissions`)
  - CRUD operations cho Permission

#### 3️⃣ **PC Components Management** (CRUD cho tất cả linh kiện)

**Main Components:**
- **CPU Controller** (`/cpus`) - Bộ vi xử lý
- **Mainboard Controller** (`/mainboards`) - Bo mạch chủ
- **RAM Controller** (`/rams`) - Bộ nhớ RAM
- **VGA Controller** (`/vgas`) - Card đồ họa
- **SSD Controller** (`/ssds`) - Ổ cứng SSD
- **HDD Controller** (`/hdds`) - Ổ cứng HDD
- **PSU Controller** (`/psus`) - Nguồn máy tính
- **Case Controller** (`/cases`) - Vỏ case
- **Cooler Controller** (`/coolers`) - Tản nhiệt

**Reference Data (Enum/Lookup Tables):**
- **Socket Controller** (`/sockets`) - Các loại socket CPU
- **RAM Type Controller** (`/ram-types`) - DDR3, DDR4, DDR5
- **SSD Type Controller** (`/ssd-types`) - M.2, SATA
- **Form Factor Controller** (`/form-factors`) - ATX, Micro-ATX, Mini-ITX
- **Case Size Controller** (`/case-sizes`) - Full Tower, Mid Tower, Mini Tower
- **Cooler Type Controller** (`/cooler-types`) - Air, AIO
- **PCIe Version Controller** (`/pcie-versions`) - PCIe 3.0, 4.0, 5.0
- **PCIe Connector Controller** (`/pcie-connectors`) - 6-pin, 8-pin, 12VHPWR
- **Interface Type Controller** (`/interface-types`) - SATA, M.2, NVMe

#### 4️⃣ **⭐ BUILD COMPATIBILITY CHECK** 
- **Build Controller** (`/builds`)
  - `POST /builds/check-compatibility` - **Kiểm tra tương thích linh kiện**

---

## 🚀 BƯỚC 1: KHỞI ĐỘNG ỨNG DỤNG

### Kiểm tra ứng dụng đang chạy
```powershell
# Kiểm tra port 8080 có đang được sử dụng không
netstat -ano | findstr :8080
```

### Nếu chưa chạy, khởi động ứng dụng:
```powershell
cd C:\Project\BEBuildPCChecker\J2EE_Build_PC_Checker
mvn spring-boot:run
```

### Hoặc chạy file JAR đã build:
```powershell
java -jar target\buildpcchecker-0.0.1-SNAPSHOT.jar
```

---

## 🌐 BƯỚC 2: MỞ SWAGGER UI

### Truy cập Swagger UI trong trình duyệt:
```
http://localhost:8080/identity/swagger-ui.html
```

**Lưu ý:** 
- Context path là `/identity`
- Port mặc định: `8080`

---

## 🔐 BƯỚC 3: XÁC THỰC (AUTHENTICATION)

### Bước 3.1: Đăng nhập để lấy Access Token

1. Tìm **authentication-controller** trong Swagger UI
2. Mở endpoint `POST /auth/token`
3. Click **Try it out**
4. Nhập request body:

```json
{
  "username": "admin",
  "password": "admin"
}
```

5. Click **Execute**
6. Copy `token` từ response:

```json
{
  "code": 1000,
  "message": "Success",
  "result": {
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJidWlsZHBjY2hlY2tlciIsInN1YiI6ImFkbWluIiwiZXhwIjoxNzQwNzQxMjM0LCJpYXQiOjE3NDA3NDExMzQsInNjb3BlIjoiUk9MRV9BRE1JTiJ9...",
    "authenticated": true
  }
}
```

### Bước 3.2: Cấu hình Authorization

1. Click nút **Authorize** 🔓 ở góc trên bên phải Swagger UI
2. Nhập token với format:
```
Bearer eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJidWlsZHBjY2hlY2tlciIsInN1YiI6ImFkbWluIiwiZXhwIjoxNzQwNzQxMjM0LCJpYXQiOjE3NDA3NDExMzQsInNjb3BlIjoiUk9MRV9BRE1JTiJ9...
```
3. Click **Authorize**
4. Click **Close**

✅ **Bây giờ tất cả API calls sẽ tự động thêm Authorization header!**

---

## 🧩 BƯỚC 4: CHUẨN BỊ DỮ LIỆU LINH KIỆN (Component IDs)

Trước khi test API check compatibility, bạn cần có ID của các linh kiện trong database.

### Cách 1: Lấy ID từ các endpoint GET

#### 4.1. Lấy CPU ID
1. Tìm **cpu-controller**
2. Mở `GET /cpus`
3. Click **Try it out** → **Execute**
4. Copy `id` của CPU bạn muốn test

#### 4.2. Lấy Mainboard ID
1. Tìm **mainboard-controller**
2. Mở `GET /mainboards`
3. Click **Try it out** → **Execute**
4. Copy `id` của Mainboard

#### 4.3. Lấy RAM ID
1. Tìm **ram-controller**
2. Mở `GET /rams`
3. Click **Try it out** → **Execute**
4. Copy `id` của RAM

#### 4.4. Lấy VGA ID
1. Tìm **vga-controller**
2. Mở `GET /vgas`
3. Click **Try it out** → **Execute**
4. Copy `id` của VGA

#### 4.5. Lấy SSD ID(s)
1. Tìm **ssd-controller**
2. Mở `GET /ssds`
3. Click **Try it out** → **Execute**
4. Copy `id` của SSD(s)

#### 4.6. Lấy HDD ID(s)
1. Tìm **hdd-controller**
2. Mở `GET /hdds`
3. Click **Try it out** → **Execute**
4. Copy `id` của HDD(s)

#### 4.7. Lấy PSU ID
1. Tìm **psu-controller**
2. Mở `GET /psus`
3. Click **Try it out** → **Execute**
4. Copy `id` của PSU

#### 4.8. Lấy Case ID
1. Tìm **case-controller**
2. Mở `GET /cases`
3. Click **Try it out** → **Execute**
4. Copy `id` của Case

#### 4.9. Lấy Cooler ID
1. Tìm **cooler-controller**
2. Mở `GET /coolers`
3. Click **Try it out** → **Execute**
4. Copy `id` của Cooler

---

## ⚙️ BƯỚC 5: TEST API CHECK COMPATIBILITY

### 5.1. Tìm Build Controller
Trong Swagger UI, scroll xuống tìm **build-controller**

### 5.2. Mở endpoint POST /builds/check-compatibility
Click vào endpoint để expand

### 5.3. Click "Try it out"

### 5.4. Nhập Request Body

#### ✅ **Test Case 1: Build Tương Thích (Compatible Build)**

Điền các ID của linh kiện tương thích với nhau:

```json
{
  "cpuId": "cpu-uuid-here",
  "mainboardId": "mainboard-uuid-here",
  "ramId": "ram-uuid-here",
  "vgaId": "vga-uuid-here",
  "ssdIds": ["ssd-uuid-1", "ssd-uuid-2"],
  "hddIds": ["hdd-uuid-1"],
  "psuId": "psu-uuid-here",
  "caseId": "case-uuid-here",
  "coolerId": "cooler-uuid-here"
}
```

**Ví dụ cụ thể:**
```json
{
  "cpuId": "550e8400-e29b-41d4-a716-446655440000",
  "mainboardId": "550e8400-e29b-41d4-a716-446655440001",
  "ramId": "550e8400-e29b-41d4-a716-446655440002",
  "vgaId": "550e8400-e29b-41d4-a716-446655440003",
  "ssdIds": ["550e8400-e29b-41d4-a716-446655440004"],
  "hddIds": [],
  "psuId": "550e8400-e29b-41d4-a716-446655440006",
  "caseId": "550e8400-e29b-41d4-a716-446655440007",
  "coolerId": "550e8400-e29b-41d4-a716-446655440008"
}
```

#### ❌ **Test Case 2: Build Không Tương Thích (Incompatible Build)**

Cố tình chọn các linh kiện không tương thích (ví dụ: CPU Intel + Mainboard AMD):

```json
{
  "cpuId": "intel-cpu-lga1700-id",
  "mainboardId": "amd-mainboard-am5-id",
  "ramId": "ddr5-ram-id",
  "vgaId": "high-end-vga-id",
  "ssdIds": ["ssd-1-id", "ssd-2-id", "ssd-3-id"],
  "hddIds": ["hdd-1-id"],
  "psuId": "low-wattage-psu-450w-id",
  "caseId": "mini-itx-case-id",
  "coolerId": "tall-cooler-id"
}
```

#### 🔧 **Test Case 3: Partial Build (Một Số Linh Kiện)**

API hỗ trợ kiểm tra build chưa đầy đủ (null-safe):

```json
{
  "cpuId": "cpu-uuid-here",
  "mainboardId": "mainboard-uuid-here",
  "ramId": "ram-uuid-here",
  "vgaId": null,
  "ssdIds": [],
  "hddIds": [],
  "psuId": null,
  "caseId": null,
  "coolerId": null
}
```

### 5.5. Click Execute

### 5.6. Xem Response

#### ✅ Response khi **Compatible:**
```json
{
  "code": 1000,
  "message": "Success",
  "result": {
    "compatible": true,
    "errors": [],
    "warnings": [
      "Chỉ có 1 thanh RAM - Khuyến nghị dùng 2 thanh để kích hoạt Dual Channel"
    ],
    "recommendedPsuWattage": 650
  }
}
```

#### ❌ Response khi **Incompatible:**
```json
{
  "code": 1000,
  "message": "Success",
  "result": {
    "compatible": false,
    "errors": [
      "CPU socket 'LGA1700' không khớp với Mainboard socket 'AM5'",
      "RAM type 'DDR5' không khớp với Mainboard RAM type 'DDR4'",
      "Mainboard có 2 khe M.2 nhưng bạn đã chọn 3 SSD M.2",
      "VGA dài 350mm nhưng Case chỉ hỗ trợ tối đa 320mm",
      "CPU TDP 125W nhưng Cooler chỉ hỗ trợ 95W",
      "PSU chỉ có 450W, không đủ cho hệ thống (khuyến nghị tối thiểu: 650W)",
      "PSU cần 2 PCIe connector 8-pin nhưng chỉ có 1"
    ],
    "warnings": [
      "VGA PCIe 4.0 sẽ chạy ở chế độ PCIe 3.0 (Mainboard chỉ hỗ trợ PCIe 3.0)"
    ],
    "recommendedPsuWattage": 650
  }
}
```

---

## 🧪 BƯỚC 6: CÁC SCENARIOS TEST CHI TIẾT

### Scenario 1: Kiểm tra Socket Mismatch
**Mục đích:** CPU socket không khớp Mainboard socket

**Setup:**
- CPU: Intel Core i9-13900K (Socket LGA1700)
- Mainboard: ASUS ROG Strix X670E (Socket AM5)

**Kết quả mong đợi:**
```json
{
  "compatible": false,
  "errors": [
    "CPU socket 'LGA1700' không khớp với Mainboard socket 'AM5'"
  ]
}
```

---

### Scenario 2: Kiểm tra RAM Type Mismatch
**Mục đích:** RAM type không khớp Mainboard

**Setup:**
- Mainboard: Hỗ trợ DDR4
- RAM: DDR5 5600MHz

**Kết quả mong đợi:**
```json
{
  "compatible": false,
  "errors": [
    "RAM type 'DDR5' không khớp với Mainboard RAM type 'DDR4'"
  ]
}
```

---

### Scenario 3: Kiểm tra VGA Quá Dài
**Mục đích:** VGA không vừa Case

**Setup:**
- VGA: RTX 4090 (dài 355mm)
- Case: Mini-ITX (max VGA length: 280mm)

**Kết quả mong đợi:**
```json
{
  "compatible": false,
  "errors": [
    "VGA dài 355mm nhưng Case chỉ hỗ trợ tối đa 280mm"
  ]
}
```

---

### Scenario 4: Kiểm tra PSU Không Đủ Công Suất
**Mục đích:** PSU không đủ công suất cho build

**Setup:**
- CPU: 125W TDP
- VGA: 350W TDP
- RAM: 10W × 2 = 20W
- SSD: 5W × 2 = 10W
- Overhead: 75W
- **Total:** 580W
- **Recommended:** 580 × 1.2 = 696W
- **PSU thực tế:** 450W

**Kết quả mong đợi:**
```json
{
  "compatible": false,
  "errors": [
    "PSU chỉ có 450W, không đủ cho hệ thống (khuyến nghị tối thiểu: 696W)"
  ],
  "recommendedPsuWattage": 696
}
```

---

### Scenario 5: Kiểm tra Cooler Không Đủ TDP
**Mục đích:** Cooler TDP support thấp hơn CPU TDP

**Setup:**
- CPU: Intel Core i9-13900K (TDP 125W)
- Cooler: Budget Air Cooler (TDP support: 95W)

**Kết quả mong đợi:**
```json
{
  "compatible": false,
  "errors": [
    "CPU TDP 125W vượt quá khả năng làm mát của Cooler (95W)"
  ]
}
```

---

### Scenario 6: Kiểm tra Storage Slots
**Mục đích:** Số lượng M.2/SATA vượt quá số slot

**Setup:**
- Mainboard: 2 khe M.2, 4 port SATA
- SSD: 3 × M.2 SSD
- HDD: 3 × SATA HDD

**Kết quả mong đợi:**
```json
{
  "compatible": false,
  "errors": [
    "Mainboard có 2 khe M.2 nhưng bạn đã chọn 3 SSD M.2"
  ]
}
```

---

### Scenario 7: Kiểm tra PCIe Backward Compatibility (Warning)
**Mục đích:** VGA PCIe mới hơn Mainboard (vẫn chạy được nhưng chậm hơn)

**Setup:**
- Mainboard: PCIe 3.0
- VGA: PCIe 4.0

**Kết quả mong đợi:**
```json
{
  "compatible": true,
  "errors": [],
  "warnings": [
    "VGA PCIe 4.0 sẽ chạy ở chế độ PCIe 3.0 (Mainboard chỉ hỗ trợ PCIe 3.0)"
  ]
}
```

---

### Scenario 8: Kiểm tra Single RAM Warning
**Mục đích:** Chỉ có 1 thanh RAM (không kích hoạt Dual Channel)

**Setup:**
- RAM: 1 × 16GB DDR5

**Kết quả mong đợi:**
```json
{
  "compatible": true,
  "errors": [],
  "warnings": [
    "Chỉ có 1 thanh RAM - Khuyến nghị dùng 2 thanh để kích hoạt Dual Channel"
  ]
}
```

---

## 📊 BẢNG TỔNG KẾT CÁC KIỂM TRA

| Layer | Kiểm tra | Error/Warning | Ảnh hưởng |
|-------|----------|---------------|-----------|
| **1** | CPU Socket ↔ Mainboard Socket | Error | Build không chạy |
| **1** | Mainboard VRM Phase ≥ CPU Min VRM | Error | CPU không ổn định |
| **1** | Mainboard TDP Support ≥ CPU TDP | Error | Mainboard cháy |
| **2** | RAM Type = Mainboard RAM Type | Error | RAM không cắm được |
| **2** | RAM Bus ≤ Mainboard Max Bus | Warning | RAM chạy chậm hơn |
| **2** | RAM Quantity ≤ RAM Slots | Error | Không đủ khe cắm |
| **2** | RAM Total Capacity ≤ Max Capacity | Error | Mainboard không nhận |
| **2** | RAM Quantity = 1 | Warning | Không Dual Channel |
| **3** | Mainboard Form Factor fits Case | Error | Mainboard không vào case |
| **3** | VGA Length ≤ Case Max VGA Length | Error | VGA không vừa case |
| **3** | VGA PCIe Version backward compatible | Warning | VGA chạy chậm hơn |
| **4** | M.2 Count ≤ Mainboard M.2 Slots | Error | Không đủ khe M.2 |
| **4** | SATA Count ≤ Mainboard SATA Ports | Error | Không đủ port SATA |
| **4** | HDD Count ≤ Case Drive Bays | Error | Không đủ khay ổ cứng |
| **5** | CPU TDP ≤ Cooler TDP Support | Error | CPU quá nhiệt |
| **5** | Air Cooler Height ≤ Case Max Height | Error | Cooler không vừa case |
| **5** | AIO Radiator ≤ Case Max Radiator | Error | Radiator không vừa case |
| **6** | PSU Wattage ≥ Recommended Wattage | Error | PSU quá tải |
| **6** | PSU PCIe Connectors ≥ Required | Error | Không đủ dây cắm VGA |
| **6** | PSU SATA Connectors ≥ Required | Error | Không đủ dây cắm ổ |

---

## 🐛 TROUBLESHOOTING

### Lỗi 401 Unauthorized
**Nguyên nhân:** Token hết hạn hoặc chưa đăng nhập

**Giải pháp:**
1. Đăng nhập lại qua `POST /auth/token`
2. Click **Authorize** và nhập token mới
3. Thử lại request

### Lỗi 404 Component Not Found
**Nguyên nhân:** ID linh kiện không tồn tại trong database

**Giải pháp:**
1. Kiểm tra lại ID bằng cách gọi `GET /cpus`, `GET /mainboards`, v.v.
2. Đảm bảo database đã có dữ liệu

### Response trống hoặc null
**Nguyên nhân:** Database chưa có dữ liệu

**Giải pháp:**
1. Tạo dữ liệu mẫu bằng các endpoint POST (cpu, mainboard, ram, v.v.)
2. Hoặc import SQL dump nếu có

### Swagger UI không load
**Nguyên nhân:** Application chưa start hoặc port bị conflict

**Giải pháp:**
```powershell
# Kiểm tra port 8080
netstat -ano | findstr :8080

# Nếu không có gì, start lại application
mvn spring-boot:run
```

---

## 📚 TÀI LIỆU THAM KHẢO

- **Full API Documentation:** http://localhost:8080/identity/swagger-ui.html
- **OpenAPI JSON:** http://localhost:8080/identity/api-docs
- **Compatibility Check Logic:** Xem file `COMPATIBILITY_CHECK_IMPLEMENTATION_SUMMARY.md`
- **Postman Collection:** Xem file `COMPATIBILITY_CHECK_POSTMAN_TESTS.md`

---

## ✅ CHECKLIST HOÀN CHỈNH

- [ ] Start application
- [ ] Truy cập Swagger UI: http://localhost:8080/identity/swagger-ui.html
- [ ] Đăng nhập qua POST /auth/token
- [ ] Cấu hình Authorization với Bearer token
- [ ] Lấy ID các linh kiện từ GET endpoints
- [ ] Test POST /builds/check-compatibility với build tương thích
- [ ] Test với build không tương thích (socket mismatch)
- [ ] Test với partial build (một số linh kiện null)
- [ ] Kiểm tra errors list
- [ ] Kiểm tra warnings list
- [ ] Kiểm tra recommendedPsuWattage

---

## 🎉 KẾT LUẬN

API **POST /builds/check-compatibility** đã được implement đầy đủ với:

✅ **6 Layers Validation:**
1. CPU ↔ Mainboard (Socket, VRM, TDP)
2. RAM compatibility
3. Case + VGA fitment
4. Storage slots
5. Cooler compatibility
6. PSU power & connectors

✅ **Features:**
- Null-safe (hỗ trợ partial build)
- Không throw exception nếu incompatible
- Phân biệt rõ `errors` (critical) và `warnings` (non-critical)
- Tính toán `recommendedPsuWattage` tự động
- Full Swagger documentation với examples

✅ **Testing:**
- Swagger UI testing ready
- Postman collection available
- Multiple test scenarios documented

---

**Happy Testing! 🚀**

