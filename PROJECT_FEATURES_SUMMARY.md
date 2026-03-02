# 📋 TỔNG HỢP CHỨC NĂNG DỰ ÁN BUILD PC CHECKER

## 🎯 TỔNG QUAN
**Build PC Checker** là hệ thống REST API kiểm tra tương thích linh kiện máy tính, được xây dựng với Spring Boot 3 + Java 17.

**URL:** http://localhost:8080/identity
**Swagger UI:** http://localhost:8080/identity/swagger-ui.html

---

## 📦 CÁC MODULE CHỨC NĂNG

### 1️⃣ **AUTHENTICATION & AUTHORIZATION** 🔐

#### Authentication Controller (`/auth`)
```
POST   /auth/token                      - Đăng nhập (username/password) → JWT Token
POST   /auth/introspect                 - Kiểm tra token hợp lệ
POST   /auth/logout                     - Đăng xuất
POST   /auth/refresh                    - Refresh token mới
POST   /auth/outbound/authentication    - OAuth2 Login (Google)
POST   /auth/send-verification-email    - Gửi email xác thực
POST   /auth/verify-email               - Xác thực email với mã OTP
```

#### User Management (`/users`)
```
POST   /users                - Tạo user mới
GET    /users                - Lấy danh sách tất cả users
GET    /users/me             - Lấy thông tin user hiện tại
GET    /users/{userId}       - Lấy user theo ID
PUT    /users/{userId}       - Cập nhật user
PUT    /users/me             - Cập nhật thông tin cá nhân
PUT    /users/change-password - Đổi mật khẩu
```

#### Role & Permission Management
```
/roles        - CRUD Role
/permissions  - CRUD Permission
```

---

### 2️⃣ **PC COMPONENTS MANAGEMENT** 💻

Tất cả controller đều có đầy đủ CRUD operations:
- `POST /{resource}` - Tạo mới
- `GET /{resource}` - Lấy tất cả
- `GET /{resource}/{id}` - Lấy theo ID
- `PUT /{resource}/{id}` - Cập nhật
- `DELETE /{resource}/{id}` - Xóa

#### Main Components (Linh Kiện Chính)
```
/cpus         - CPU (Bộ vi xử lý)
              • Socket, TDP, minVrmPhase, cores, threads, baseClock, boostClock
              
/mainboards   - Mainboard (Bo mạch chủ)
              • Socket, chipset, formFactor, ramType, ramSlots, maxRamCapacity
              • maxRamBus, m2Slots, sataSlots, pcieVersion, vrmPhase, cpuTdpSupport
              
/rams         - RAM (Bộ nhớ)
              • RamType (DDR4/DDR5), capacity, bus, tdp
              
/vgas         - VGA/GPU (Card đồ họa)
              • Length, tdp, pcieVersion, pcieConnectorId, memoryCapacity
              
/ssds         - SSD (Ổ cứng thể rắn)
              • SsdType (M.2/SATA), capacity, tdp, read/write speed
              
/hdds         - HDD (Ổ cứng cơ)
              • InterfaceType (SATA), capacity, tdp, rpm
              
/psus         - PSU (Nguồn máy tính)
              • Wattage, efficiency, pcie6pinCount, pcie8pinCount, pcie12vhpwrCount
              • sataConnectorCount, modular
              
/cases        - Case (Vỏ máy tính)
              • CaseSize, formFactors (list), maxVgaLength, maxCoolerHeight
              • maxRadiatorSupport, driveBays2_5, driveBays3_5
              
/coolers      - Cooler (Tản nhiệt)
              • CoolerType (AIR/AIO), tdpSupport, height, radiatorSize, fanCount
```

#### Reference Data (Dữ Liệu Tham Chiếu / Enum)
```
/sockets          - Socket (LGA1700, AM5, LGA1200, AM4...)
/ram-types        - RAM Type (DDR3, DDR4, DDR5)
/ssd-types        - SSD Type (M.2, SATA)
/form-factors     - Form Factor (ATX, Micro-ATX, Mini-ITX, E-ATX)
/case-sizes       - Case Size (Full Tower, Mid Tower, Mini Tower, SFF)
/cooler-types     - Cooler Type (AIR, AIO)
/pcie-versions    - PCIe Version (3.0, 4.0, 5.0)
/pcie-connectors  - PCIe Connector (6-pin, 8-pin, 12VHPWR)
/interface-types  - Interface Type (SATA, M.2, NVMe)
```

---

### 3️⃣ **⭐ BUILD COMPATIBILITY CHECK** 🔧

#### Build Controller (`/builds`)
```
POST   /builds/check-compatibility   - Kiểm tra tương thích linh kiện PC
```

**Request Body:**
```json
{
  "cpuId": "uuid",
  "mainboardId": "uuid",
  "ramId": "uuid",
  "vgaId": "uuid",
  "ssdIds": ["uuid1", "uuid2"],
  "hddIds": ["uuid1"],
  "psuId": "uuid",
  "caseId": "uuid",
  "coolerId": "uuid"
}
```

**Response:**
```json
{
  "code": 1000,
  "message": "Success",
  "result": {
    "compatible": true/false,
    "errors": ["error message 1", "error message 2"],
    "warnings": ["warning message 1"],
    "recommendedPsuWattage": 650
  }
}
```

#### Validation Layers (6 Tầng Kiểm Tra)

**Layer 1: CPU ↔ Mainboard**
- ✅ Socket phải khớp
- ✅ Mainboard VRM Phase ≥ CPU minVrmPhase
- ✅ Mainboard cpuTdpSupport ≥ CPU TDP

**Layer 2: RAM**
- ✅ RAM type == Mainboard ramType
- ⚠️ RAM bus <= Mainboard maxRamBus (warning nếu vượt)
- ✅ Số lượng RAM <= ramSlots
- ✅ Tổng dung lượng RAM <= maxRamCapacity
- ⚠️ Warning nếu chỉ có 1 thanh RAM (không Dual Channel)

**Layer 3: Case + VGA**
- ✅ Mainboard formFactor phải được Case hỗ trợ
- ✅ VGA length <= Case maxVgaLength
- ⚠️ PCIe backward compatibility (warning nếu VGA mới hơn Mainboard)

**Layer 4: Storage**
- ✅ Số lượng M.2 SSD <= Mainboard m2Slots
- ✅ Số lượng SATA (SSD + HDD) <= Mainboard sataSlots
- ✅ Số lượng HDD <= Case driveBays3_5
- ✅ Số lượng SSD SATA <= Case driveBays2_5

**Layer 5: Cooler**
- ✅ CPU TDP <= Cooler tdpSupport
- ✅ Nếu AIR: Cooler height <= Case maxCoolerHeight
- ✅ Nếu AIO: Cooler radiatorSize <= Case maxRadiatorSupport

**Layer 6: Power (PSU)**
- ✅ Tính tổng TDP: CPU + VGA + RAM + SSD + HDD + 75W overhead
- ✅ Recommended PSU = Total TDP × 1.2
- ✅ PSU wattage >= recommended
- ✅ PSU có đủ PCIe connector cho VGA
- ✅ PSU có đủ SATA connector cho storage

---

## 🔐 AUTHENTICATION FLOW

### 1. Đăng nhập
```http
POST /auth/token
Content-Type: application/json

{
  "username": "admin",
  "password": "admin"
}
```

**Response:**
```json
{
  "code": 1000,
  "message": "Success",
  "result": {
    "token": "eyJhbGciOiJIUzUxMiJ9...",
    "authenticated": true
  }
}
```

### 2. Sử dụng Token
```http
GET /users/me
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

### 3. Swagger Authorization
1. Click nút **Authorize** 🔓
2. Nhập: `Bearer <your-token>`
3. Click Authorize
4. Tất cả request sẽ tự động thêm token

---

## 📊 DATABASE SCHEMA

### Core Tables
```
users
roles
permissions
user_roles
role_permissions

sockets
ram_types
ssd_types
form_factors
case_sizes
cooler_types
pcie_versions
pcie_connectors
interface_types

cpus
mainboards
rams
vgas
ssds
hdds
psus
cases
coolers
```

---

## 🛠 CÔNG NGHỆ SỬ DỤNG

- **Framework:** Spring Boot 3.x
- **Java:** 17
- **Database:** MySQL
- **Security:** JWT (HS512)
- **Documentation:** Swagger/OpenAPI 3
- **Build Tool:** Maven
- **ORM:** Spring Data JPA + Hibernate
- **Validation:** Jakarta Validation
- **Email:** Spring Mail (SMTP Gmail)

---

## 📖 POSTMAN / SWAGGER

### Swagger UI
```
http://localhost:8080/identity/swagger-ui.html
```

### API Docs JSON
```
http://localhost:8080/identity/api-docs
```

---

## 🚀 QUICK START

### 1. Khởi động ứng dụng
```powershell
cd C:\Project\BEBuildPCChecker\J2EE_Build_PC_Checker
mvn spring-boot:run
```

### 2. Truy cập Swagger
```
http://localhost:8080/identity/swagger-ui.html
```

### 3. Đăng nhập
- POST /auth/token
- Username: `admin`
- Password: `admin`

### 4. Authorize trong Swagger
- Click **Authorize**
- Nhập: `Bearer <token>`

### 5. Test API
- Tạo linh kiện (CPU, Mainboard, RAM, v.v.)
- Test compatibility check

---

## 📚 FILE TÀI LIỆU QUAN TRỌNG

```
✅ TESTING_BUILD_COMPATIBILITY_SWAGGER_GUIDE.md  - Hướng dẫn test chi tiết
✅ COMPATIBILITY_CHECK_IMPLEMENTATION_SUMMARY.md - Implementation summary
✅ COMPATIBILITY_CHECK_POSTMAN_TESTS.md          - Postman test collection
✅ COMPATIBILITY_CHECK_QUICKSTART.md             - Quick start guide
✅ SWAGGER_GUIDE.md                              - Swagger setup guide
✅ PC_PARTS_ENTITIES_REFERENCE.md                - Entity reference
```

---

## 🎯 USE CASES

### Use Case 1: Người dùng xây dựng PC mới
1. Chọn CPU → API trả về list Mainboard tương thích
2. Chọn Mainboard → API trả về list RAM tương thích
3. Tiếp tục chọn các linh kiện
4. Gọi `/builds/check-compatibility` để kiểm tra
5. Nếu có lỗi → điều chỉnh lựa chọn
6. Nếu OK → tiến hành mua hàng

### Use Case 2: Admin quản lý linh kiện
1. Đăng nhập với role ADMIN
2. Thêm/sửa/xóa linh kiện qua CRUD APIs
3. Quản lý reference data (Socket, RAM Type, v.v.)

### Use Case 3: Kiểm tra PC hiện có
1. Nhập thông tin các linh kiện hiện tại
2. Gọi API check compatibility
3. Nhận cảnh báo nếu có vấn đề
4. Lên kế hoạch nâng cấp

---

## 🔥 HIGHLIGHTS

✅ **6-Layer Validation** - Kiểm tra đầy đủ tất cả khía cạnh tương thích
✅ **Null-Safe** - Hỗ trợ partial build (chưa chọn đủ linh kiện)
✅ **No Exception** - Không throw exception nếu incompatible
✅ **Error vs Warning** - Phân biệt rõ critical errors và warnings
✅ **Auto PSU Calculation** - Tính tự động công suất nguồn khuyến nghị
✅ **Full Swagger Docs** - Documentation đầy đủ với examples
✅ **JWT Security** - Bảo mật với JWT token
✅ **Email Verification** - Xác thực email đăng ký
✅ **Role-Based Access** - Phân quyền ADMIN/USER

---

## 📞 CONTACT & SUPPORT

Nếu có vấn đề, check các file tài liệu trong project hoặc xem logs:
```powershell
# Xem logs ứng dụng
tail -f logs/application.log
```

---

**Happy Building! 🚀**

