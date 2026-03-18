# Hướng Dẫn Test API trong Postman - PC Components Module

> 📅 **Updated:** February 13, 2026  
> ⚠️ **Breaking Changes:** SSD, HDD, PSU, Cooler APIs đã được refactor. Xem CHANGELOG_2026-02-13.md

## 🔧 Cấu Hình Ban Đầu

### Base URL
```
http://localhost:8080/identity
```

### Headers cho mọi request (trừ auth)
```
Content-Type: application/json
Authorization: Bearer <your_token_here>
```

---

## 📝 BƯỚC 1: AUTHENTICATION (Đăng nhập)

### 1.1 Tạo User (nếu chưa có)
**POST** `http://localhost:8080/identity/users`

```json
{
  "email": "admin@test.com",
  "password": "admin123",
  "firstname": "Admin",
  "lastname": "User",
  "dateOfBirth": "1990-01-01"
}
```

### 1.2 Đăng nhập để lấy Token
**POST** `http://localhost:8080/identity/auth/token`

**Body:**
```json
{
  "email": "admin@test.com",
  "password": "admin123"
}
```

**Response:**
```json
{
  "code": 1000,
  "result": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "authenticated": true
  }
}
```

**✅ Copy token từ response và dùng cho các request tiếp theo**

---

## �️ BƯỚC 2: SETUP LOOKUP ENTITIES (Tạo dữ liệu tham chiếu)

> **⚠️ Quan trọng:** Phải tạo lookup entities trước khi tạo PC components

### 2.1 Tạo SSD Types
**POST** `http://localhost:8080/identity/ssd-types`

**Body - SATA:**
```json
{
  "id": "SATA",
  "name": "SATA SSD"
}
```

**Body - NVME:**
```json
{
  "id": "NVME",
  "name": "NVMe SSD"
}
```

### 2.2 Tạo SSD Interfaces
**POST** `http://localhost:8080/identity/ssd-interfaces`

**Body - SATA III:**
```json
{
  "id": "SATA_3",
  "name": "SATA III (6Gb/s)"
}
```

**Body - PCIe 4.0:**
```json
{
  "id": "PCIE_4",
  "name": "PCIe 4.0 x4"
}
```

**Body - PCIe 5.0:**
```json
{
  "id": "PCIE_5",
  "name": "PCIe 5.0 x4"
}
```

### 2.3 Tạo HDD Interfaces
**POST** `http://localhost:8080/identity/hdd-interfaces`

**Body - SATA III:**
```json
{
  "id": "SATA_3",
  "name": "SATA III (6Gb/s)"
}
```

**Body - SAS:**
```json
{
  "id": "SAS",
  "name": "SAS (12Gb/s)"
}
```

### 2.4 Tạo PCIe Connectors (Cho PSU)
**POST** `http://localhost:8080/identity/pcie-connectors`

**Body - 2x 8-Pin:**
```json
{
  "id": "2X8PIN",
  "name": "2x 8-Pin (6+2)"
}
```

**Body - 3x 8-Pin:**
```json
{
  "id": "3X8PIN",
  "name": "3x 8-Pin (6+2)"
}
```

**Body - 12VHPWR:**
```json
{
  "id": "12VHPWR",
  "name": "12VHPWR (16-Pin)"
}
```

**Body - 16PIN:**
```json
{
  "id": "16PIN",
  "name": "16-Pin PCIe 5.0"
}
```

### 2.5 Tạo Cooler Types
**POST** `http://localhost:8080/identity/cooler-types`

**Body - Air Cooler:**
```json
{
  "id": "AIR",
  "name": "Air Cooler (Tản khí)"
}
```

**Body - AIO:**
```json
{
  "id": "AIO",
  "name": "AIO Liquid Cooler (Tản nước)"
}
```

### 2.6 Kiểm tra Lookup Entities đã tạo
**GET** `http://localhost:8080/identity/ssd-types`  
**GET** `http://localhost:8080/identity/ssd-interfaces`  
**GET** `http://localhost:8080/identity/hdd-interfaces`  
**GET** `http://localhost:8080/identity/pcie-connectors`  
**GET** `http://localhost:8080/identity/cooler-types`

---

## 💾 BƯỚC 3: TEST SSD APIs (UPDATED)

### 3.1 Tạo SSD mới (NVMe)
**POST** `http://localhost:8080/identity/ssds`

**Body:**
```json
{
  "name": "Samsung 990 PRO 1TB",
  "ssdTypeId": "NVME",
  "formFactor": "M.2 2280",
  "ssdInterfaceId": "PCIE_4",
  "capacity": 1000,
  "tdp": 7,
  "description": "High-performance NVMe SSD with excellent endurance"
}
```

**Response thành công:**
```json
{
  "code": 1000,
  "result": {
    "id": "abc-123-xyz",
    "name": "Samsung 990 PRO 1TB",
    "ssdType": {
      "id": "NVME",
      "name": "NVMe SSD"
    },
    "formFactor": "M.2 2280",
    "ssdInterface": {
      "id": "PCIE_4",
      "name": "PCIe 4.0 x4"
    },
    "capacity": 1000,
    "tdp": 7,
    "description": "High-performance NVMe SSD with excellent endurance"
  }
}
```

### 3.2 Tạo SSD SATA
**POST** `http://localhost:8080/identity/ssds`

```json
{
  "name": "Samsung 870 EVO 500GB",
  "ssdTypeId": "SATA",
  "formFactor": "2.5 inch",
  "ssdInterfaceId": "SATA_3",
  "capacity": 500,
  "tdp": 3,
  "description": "Reliable SATA SSD for everyday use"
}
```

### 3.3 Test duplicate name validation
**POST** `http://localhost:8080/identity/ssds`

```json
{
  "name": "Samsung 990 PRO 1TB",
  "ssdTypeId": "NVME",
  "formFactor": "M.2 2280",
  "ssdInterfaceId": "PCIE_4",
  "capacity": 1000,
  "tdp": 7
}
```

**Expected Error:**
```json
{
  "code": 2708,
  "message": "SSD name already exists"
}
```

### 3.4 Lấy tất cả SSD
**GET** `http://localhost:8080/identity/ssds`

### 3.5 Lấy SSD theo ID
**GET** `http://localhost:8080/identity/ssds/{id}`

### 3.6 Cập nhật SSD
**PUT** `http://localhost:8080/identity/ssds/{id}`

```json
{
  "name": "Samsung 990 PRO 2TB",
  "ssdTypeId": "NVME",
  "capacity": 2000,
  "description": "Updated to 2TB version"
}
```

### 3.7 Xóa SSD
**DELETE** `http://localhost:8080/identity/ssds/{id}`

---

## 💿 BƯỚC 4: TEST HDD APIs (UPDATED)

### 4.1 Tạo HDD 3.5"
**POST** `http://localhost:8080/identity/hdds`

```json
{
  "name": "Seagate Barracuda 2TB",
  "formFactor": "3.5 inch",
  "hddInterfaceId": "SATA_3",
  "capacity": 2000,
  "tdp": 6,
  "description": "High capacity storage for data"
}
```

**Response thành công:**
```json
{
  "code": 1000,
  "result": {
    "id": "hdd-123",
    "name": "Seagate Barracuda 2TB",
    "formFactor": "3.5 inch",
    "hddInterface": {
      "id": "SATA_3",
      "name": "SATA III (6Gb/s)"
    },
    "capacity": 2000,
    "tdp": 6,
    "description": "High capacity storage for data"
  }
}
```

### 4.2 Tạo HDD Laptop (2.5")
**POST** `http://localhost:8080/identity/hdds`

```json
{
  "name": "WD Blue 1TB Mobile",
  "formFactor": "2.5 inch",
  "hddInterfaceId": "SATA_3",
  "capacity": 1000,
  "tdp": 4,
  "description": "Compact HDD for laptops"
}
```

### 4.3 Test duplicate name validation
**POST** `http://localhost:8080/identity/hdds`

```json
{
  "name": "Seagate Barracuda 2TB",
  "formFactor": "3.5 inch",
  "hddInterfaceId": "SATA_3",
  "capacity": 2000,
  "tdp": 6
}
```

**Expected Error:**
```json
{
  "code": 2807,
  "message": "HDD name already exists"
}
```

### 4.4 Lấy tất cả HDD
**GET** `http://localhost:8080/identity/hdds`

### 4.5 Lấy HDD theo ID
**GET** `http://localhost:8080/identity/hdds/{id}`

### 4.6 Cập nhật HDD
**PUT** `http://localhost:8080/identity/hdds/{id}`

```json
{
  "capacity": 4000,
  "description": "Upgraded to 4TB"
}
```

### 4.7 Xóa HDD
**DELETE** `http://localhost:8080/identity/hdds/{id}`

---

## ⚡ BƯỚC 5: TEST PSU APIs (UPDATED)

### 5.1 Tạo PSU với PCIe Connector
**POST** `http://localhost:8080/identity/psus`

```json
{
  "name": "Corsair RM850x",
  "wattage": 850,
  "efficiency": "80+ Gold",
  "pcieConnectorId": "3X8PIN",
  "sataConnector": 8,
  "description": "High-quality modular PSU for gaming builds"
}
```

**Response thành công:**
```json
{
  "code": 1000,
  "result": {
    "id": "psu-123",
    "name": "Corsair RM850x",
    "wattage": 850,
    "efficiency": "80+ Gold",
    "pcieConnector": {
      "id": "3X8PIN",
      "name": "3x 8-Pin (6+2)"
    },
    "sataConnector": 8,
    "description": "High-quality modular PSU for gaming builds"
  }
}
```

### 5.2 Tạo PSU không có PCIe Connector (nullable)
**POST** `http://localhost:8080/identity/psus`

```json
{
  "name": "Thermaltake Smart 600W",
  "wattage": 600,
  "efficiency": "80+ Bronze",
  "sataConnector": 6,
  "description": "Budget-friendly PSU without PCIe connectors"
}
```

### 5.3 Tạo PSU với 12VHPWR (RTX 4090)
**POST** `http://localhost:8080/identity/psus`

```json
{
  "name": "Corsair HX1500i",
  "wattage": 1500,
  "efficiency": "80+ Platinum",
  "pcieConnectorId": "12VHPWR",
  "sataConnector": 10,
  "description": "High-end PSU for RTX 4090 builds"
}
```

### 5.4 Test duplicate name validation
**POST** `http://localhost:8080/identity/psus`

```json
{
  "name": "Corsair RM850x",
  "wattage": 850,
  "efficiency": "80+ Gold",
  "sataConnector": 8
}
```

**Expected Error:**
```json
{
  "code": 2906,
  "message": "PSU name already exists"
}
```

### 5.5 Lấy tất cả PSU
**GET** `http://localhost:8080/identity/psus`

### 5.6 Lấy PSU theo ID
**GET** `http://localhost:8080/identity/psus/{id}`

### 5.7 Cập nhật PSU
**PUT** `http://localhost:8080/identity/psus/{id}`

```json
{
  "wattage": 1000,
  "pcieConnectorId": "12VHPWR"
}
```

### 5.8 Xóa PSU
**DELETE** `http://localhost:8080/identity/psus/{id}`

---

## 🏠 BƯỚC 6: TEST CASE APIs (UPDATED)

### 6.1 Tạo Case mới (ATX)
**POST** `http://localhost:8080/identity/cases`

```json
{
  "name": "NZXT H510 Elite",
  "size": "ATX",
  "maxVgaLengthMm": 381,
  "maxCoolerHeightMm": 165,
  "maxRadiatorSize": 360,
  "drive35Slot": 2,
  "drive25Slot": 4,
  "description": "Premium mid-tower case with tempered glass"
}
```

**Response thành công:**
```json
{
  "code": 1000,
  "result": {
    "id": "case-123",
    "name": "NZXT H510 Elite",
    "size": "ATX",
    "maxVgaLengthMm": 381,
    "maxCoolerHeightMm": 165,
    "maxRadiatorSize": 360,
    "drive35Slot": 2,
    "drive25Slot": 4,
    "description": "Premium mid-tower case with tempered glass"
  }
}
```

### 6.2 Tạo Case Mini-ITX
**POST** `http://localhost:8080/identity/cases`

```json
{
  "name": "Cooler Master NR200",
  "size": "Mini-ITX",
  "maxVgaLengthMm": 330,
  "maxCoolerHeightMm": 155,
  "maxRadiatorSize": 240,
  "drive35Slot": 0,
  "drive25Slot": 3,
  "description": "Compact ITX case for small builds"
}
```

### 6.3 Test duplicate name validation
**POST** `http://localhost:8080/identity/cases`

```json
{
  "name": "NZXT H510 Elite",
  "size": "ATX",
  "maxVgaLengthMm": 381,
  "maxCoolerHeightMm": 165,
  "maxRadiatorSize": 360,
  "drive35Slot": 2,
  "drive25Slot": 4
}
```

**Expected Error:**
```json
{
  "code": 3009,
  "message": "Case name already exists"
}
```

### 6.4 Lấy tất cả Case
**GET** `http://localhost:8080/identity/cases`

### 6.5 Lấy Case theo ID
**GET** `http://localhost:8080/identity/cases/{id}`

### 6.6 Cập nhật Case
**PUT** `http://localhost:8080/identity/cases/{id}`

```json
{
  "maxRadiatorSize": 420,
  "description": "Supports up to 420mm radiator"
}
```

### 6.7 Xóa Case
**DELETE** `http://localhost:8080/identity/cases/{id}`

---

## 🌡️ BƯỚC 7: TEST COOLER APIs (UPDATED)

### 7.1 Tạo Cooler AIO (Nước)
**POST** `http://localhost:8080/identity/coolers`

```json
{
  "name": "NZXT Kraken X63",
  "coolerTypeId": "AIO",
  "radiatorSize": 280,
  "heightMm": null,
  "tdpSupport": 300,
  "description": "280mm AIO liquid cooler with RGB"
}
```

**Response thành công:**
```json
{
  "code": 1000,
  "result": {
    "id": "cooler-123",
    "name": "NZXT Kraken X63",
    "coolerType": {
      "id": "AIO",
      "name": "AIO Liquid Cooler (Tản nước)"
    },
    "radiatorSize": 280,
    "heightMm": null,
    "tdpSupport": 300,
    "description": "280mm AIO liquid cooler with RGB"
  }
}
```

### 7.2 Tạo Cooler Air (Khí)
**POST** `http://localhost:8080/identity/coolers`

```json
{
  "name": "Noctua NH-D15",
  "coolerTypeId": "AIR",
  "radiatorSize": null,
  "heightMm": 165,
  "tdpSupport": 250,
  "description": "Premium dual-tower air cooler"
}
```

### 7.3 Tạo Cooler AIO 360mm
**POST** `http://localhost:8080/identity/coolers`

```json
{
  "name": "Corsair iCUE H150i Elite",
  "coolerTypeId": "AIO",
  "radiatorSize": 360,
  "heightMm": null,
  "tdpSupport": 350,
  "description": "High-performance 360mm AIO"
}
```

### 7.4 Test duplicate name validation
**POST** `http://localhost:8080/identity/coolers`

```json
{
  "name": "NZXT Kraken X63",
  "coolerTypeId": "AIO",
  "radiatorSize": 280,
  "tdpSupport": 300
}
```

**Expected Error:**
```json
{
  "code": 3105,
  "message": "Cooler name already exists"
}
```

### 7.5 Lấy tất cả Cooler
**GET** `http://localhost:8080/identity/coolers`

### 7.6 Lấy Cooler theo ID
**GET** `http://localhost:8080/identity/coolers/{id}`

### 7.7 Cập nhật Cooler
**PUT** `http://localhost:8080/identity/coolers/{id}`

```json
{
  "tdpSupport": 320,
  "description": "Updated TDP support"
}
```

### 7.8 Xóa Cooler
**DELETE** `http://localhost:8080/identity/coolers/{id}`
}
```

### 7.8 Xóa Cooler
**DELETE** `http://localhost:8080/identity/coolers/{id}`

---

## 🎯 CHECKLIST TEST ĐẦY ĐỦ

### ✅ Lookup Entities (PHẢI TEST TRƯỚC)
- [ ] POST/GET - Tạo và lấy tất cả SSD Types (SATA, NVME)
- [ ] POST/GET - Tạo và lấy tất cả SSD Interfaces (SATA_3, PCIE_4, PCIE_5)
- [ ] POST/GET - Tạo và lấy tất cả HDD Interfaces (SATA_3, SAS)
- [ ] POST/GET - Tạo và lấy tất cả PCIe Connectors (2X8PIN, 3X8PIN, 12VHPWR, 16PIN)
- [ ] POST/GET - Tạo và lấy tất cả Cooler Types (AIR, AIO)

### ✅ SSD
- [ ] POST - Tạo SSD NVME (ssdTypeId: NVME, ssdInterfaceId: PCIE_4)
- [ ] POST - Tạo SSD SATA (ssdTypeId: SATA, ssdInterfaceId: SATA_3)
- [ ] POST - Test duplicate name (Expected: Code 2708)
- [ ] POST - Test missing required fields (Expected: Code 2701-2707)
- [ ] GET - Lấy tất cả SSD
- [ ] GET - Lấy SSD theo ID
- [ ] PUT - Cập nhật SSD
- [ ] DELETE - Xóa SSD

### ✅ HDD
- [ ] POST - Tạo HDD 3.5" (hddInterfaceId: SATA_3)
- [ ] POST - Tạo HDD 2.5" (hddInterfaceId: SATA_3)
- [ ] POST - Test duplicate name (Expected: Code 2807)
- [ ] POST - Test missing required fields (Expected: Code 2801-2806)
- [ ] GET - Lấy tất cả HDD
- [ ] GET - Lấy HDD theo ID
- [ ] PUT - Cập nhật HDD
- [ ] DELETE - Xóa HDD

### ✅ PSU
- [ ] POST - Tạo PSU với PCIe Connector (pcieConnectorId: 3X8PIN)
- [ ] POST - Tạo PSU không có PCIe Connector (pcieConnectorId: null)
- [ ] POST - Tạo PSU với 12VHPWR cho RTX 4090
- [ ] POST - Test duplicate name (Expected: Code 2906)
- [ ] POST - Test missing required fields (Expected: Code 2901-2905)
- [ ] GET - Lấy tất cả PSU
- [ ] GET - Lấy PSU theo ID
- [ ] PUT - Cập nhật PSU
- [ ] DELETE - Xóa PSU

### ✅ Case
- [ ] POST - Tạo Case ATX (maxCoolerHeightMm field)
- [ ] POST - Tạo Case Mini-ITX
- [ ] POST - Test duplicate name (Expected: Code 3009)
- [ ] POST - Test missing required fields (Expected: Code 3001-3008)
- [ ] GET - Lấy tất cả Case
- [ ] GET - Lấy Case theo ID
- [ ] PUT - Cập nhật Case
- [ ] DELETE - Xóa Case

### ✅ Cooler
- [ ] POST - Tạo Cooler AIO (coolerTypeId: AIO, radiatorSize: 280)
- [ ] POST - Tạo Cooler Air (coolerTypeId: AIR, heightMm: 165)
- [ ] POST - Tạo Cooler AIO 360mm (radiatorSize: 360)
- [ ] POST - Test duplicate name (Expected: Code 3105)
- [ ] POST - Test missing required fields (Expected: Code 3101-3104)
- [ ] GET - Lấy tất cả Cooler
- [ ] GET - Lấy Cooler theo ID
- [ ] PUT - Cập nhật Cooler
- [ ] DELETE - Xóa Cooler

---

## ⚠️ LỖI THƯỜNG GẶP (UPDATED)

### 1. Error 401 Unauthorized
```json
{
  "code": 1007,
  "message": "Unauthenticated"
}
```
**Giải pháp:** 
- Token hết hạn (5 phút) → Đăng nhập lại
- Chưa thêm token vào Header Authorization

### 2. Error 400 Bad Request - SSD Validation (Codes: 2701-2729)
```json
{
  "code": 2701,
  "message": "SSD_NAME_REQUIRED"
}
```
**Các trường bắt buộc SSD:**
- `name` (Code 2701) - Tên SSD
- `ssdTypeId` (Code 2702) - Reference đến SsdType entity (SATA/NVME)
- `formFactor` (Code 2703) - Form factor: M.2 2280, 2.5 inch, etc.
- `ssdInterfaceId` (Code 2704) - Reference đến SsdInterface entity (SATA_3, PCIE_4, PCIE_5)
- `capacity` (Code 2705) - Dung lượng (GB)
- `tdp` (Code 2706) - TDP (W)

**Duplicate name error:**
```json
{
  "code": 2708,
  "message": "SSD name already exists"
}
```

### 3. Error 400 Bad Request - HDD Validation (Codes: 2801-2819)
```json
{
  "code": 2801,
  "message": "HDD_NAME_REQUIRED"
}
```
**Các trường bắt buộc HDD:**
- `name` (Code 2801) - Tên HDD
- `formFactor` (Code 2802) - Form factor: 3.5 inch, 2.5 inch
- `hddInterfaceId` (Code 2803) - Reference đến HddInterface entity (SATA_3, SAS)
- `capacity` (Code 2804) - Dung lượng (GB)
- `tdp` (Code 2805) - TDP (W)

**Duplicate name error:**
```json
{
  "code": 2807,
  "message": "HDD name already exists"
}
```

### 4. Error 400 Bad Request - PSU Validation (Codes: 2901-2919)
```json
{
  "code": 2901,
  "message": "PSU_NAME_REQUIRED"
}
```
**Các trường bắt buộc PSU:**
- `name` (Code 2901) - Tên PSU
- `wattage` (Code 2902) - Công suất (W)
- `efficiency` (Code 2903) - Hiệu suất: 80+ Bronze, Gold, Platinum, Titanium
- `pcieConnectorId` (Code 2904) - Reference đến PcieConnector entity (NULLABLE - cho PSU budget)
- `sataConnector` (Code 2905) - Số lượng SATA connector

**Duplicate name error:**
```json
{
  "code": 2906,
  "message": "PSU name already exists"
}
```

### 5. Error 400 Bad Request - Case Validation (Codes: 3001-3099)
```json
{
  "code": 3001,
  "message": "CASE_NAME_REQUIRED"
}
```
**Các trường bắt buộc Case:**
- `name` (Code 3001) - Tên Case
- `size` (Code 3002) - Size: ATX, Micro-ATX, Mini-ITX
- `maxVgaLengthMm` (Code 3003) - Độ dài VGA tối đa (mm)
- `maxCoolerHeightMm` (Code 3004) - Chiều cao cooler tối đa (mm) - **⚠️ Đổi từ maxCoolerHeight**
- `maxRadiatorSize` (Code 3005) - Kích thước radiator tối đa (mm)
- `drive35Slot` (Code 3006) - Số slot 3.5"
- `drive25Slot` (Code 3007) - Số slot 2.5"

**Duplicate name error:**
```json
{
  "code": 3009,
  "message": "Case name already exists"
}
```

### 6. Error 400 Bad Request - Cooler Validation (Codes: 3101-3119)
```json
{
  "code": 3101,
  "message": "COOLER_NAME_REQUIRED"
}
```
**Các trường bắt buộc Cooler:**
- `name` (Code 3101) - Tên Cooler
- `coolerTypeId` (Code 3102) - Reference đến CoolerType entity (AIR/AIO) - **⚠️ Đổi từ type**
- `radiatorSize` (Code 3103) - Kích thước radiator (mm) - NULL nếu là Air cooler
- `heightMm` (Code 3104) - Chiều cao (mm) - NULL nếu là AIO cooler
- `tdpSupport` (Code 3105) - TDP hỗ trợ (W)

**Duplicate name error:**
```json
{
  "code": 3105,
  "message": "Cooler name already exists"
}
```

### 7. Error 404 Not Found
```json
{
  "code": 2707,
  "message": "SSD not found"
}
```
**Các NOT_FOUND error codes:**
- SSD: 2707
- HDD: 2806
- PSU: 2907
- Case: 3008
- Cooler: 3106

**Giải pháp:** Kiểm tra ID có tồn tại không (dùng GET all để xem danh sách)

### 8. Error 404 - Lookup Entity Not Found
```json
{
  "code": 2729,
  "message": "SSD Type not found"
}
```
**⚠️ Quan trọng:** Phải tạo lookup entities trước khi tạo PC components!
- SsdType not found: Code 2729
- SsdInterface not found: Code 2730
- HddInterface not found: Code 2819
- PcieConnector not found: Code 2919
- CoolerType not found: Code 3119

**Giải pháp:** Chạy BƯỚC 2 (Setup Lookup Entities) trước khi test PC components

### 9. Error 500 Server Error
**Giải pháp:**
- Kiểm tra MySQL đã chạy chưa
- Kiểm tra connection string trong `application.yaml`
- Xem log trong console Spring Boot
- Kiểm tra foreign key constraints (lookup entities phải tồn tại)

---

## 💡 TIPS (UPDATED)

1. **Lưu Token:** Dùng Environment Variables trong Postman:
   - Tạo variable `token` 
   - Set value từ login response
   - Dùng `{{token}}` trong Authorization header

2. **Lưu Lookup Entity IDs:** Sau khi tạo lookup entities (SSD Types, HDD Interfaces, etc.), lưu lại ID của chúng để dùng trong PC component requests:
   ```
   ssdTypeId: "NVME", "SATA"
   ssdInterfaceId: "SATA_3", "PCIE_4", "PCIE_5"
   hddInterfaceId: "SATA_3", "SAS"
   pcieConnectorId: "2X8PIN", "3X8PIN", "12VHPWR", "16PIN"
   coolerTypeId: "AIR", "AIO"
   ```

3. **Lưu Component IDs:** Sau khi tạo PC component, copy ID để test GET/PUT/DELETE

4. **Test theo thứ tự:** 
   - **BƯỚC 1:** Authentication (Login)
   - **BƯỚC 2:** Setup Lookup Entities (SSD Types, Interfaces, etc.) - **⚠️ BẮT BUỘC**
   - **BƯỚC 3-7:** Test PC Components (SSD, HDD, PSU, Case, Cooler)
   - **PHẢI test lookup entities trước khi test PC components!**

5. **Kiểm tra Error Code Ranges:**
   - SSD: 2701-2729
   - HDD: 2801-2819
   - PSU: 2901-2919
   - Case: 3001-3099
   - Cooler: 3101-3119

6. **Kiểm tra Database:** Dùng MySQL Workbench để xem:
   - Lookup tables: `ssd_type`, `ssd_interface`, `hdd_interface`, `pcie_connector`, `cooler_type`
   - PC component tables: `ssd`, `hdd`, `psu`, `case`, `cooler`
   - Foreign key relationships: `ssd.ssd_type_id` → `ssd_type.id`

7. **Postman Collection:** Import tất cả requests vào 1 collection theo thứ tự:
   ```
   📁 PC Checker API
     📁 0. Authentication
       - POST Login
     📁 1. Lookup Entities
       - POST/GET SSD Types
       - POST/GET SSD Interfaces
       - POST/GET HDD Interfaces
       - POST/GET PCIe Connectors
       - POST/GET Cooler Types
     📁 2. SSD APIs
     📁 3. HDD APIs
     📁 4. PSU APIs
     📁 5. Case APIs
     📁 6. Cooler APIs
   ```

8. **Test Duplicate Name:** Mỗi entity đều có duplicate name validation:
   - SSD: Code 2708
   - HDD: Code 2807
   - PSU: Code 2906
   - Case: Code 3009
   - Cooler: Code 3105

---

## 🚀 BƯỚC TIẾP THEO

Sau khi test xong CRUD cơ bản, bạn có thể:

### 1. Test Advanced Validation
- Test với dữ liệu không hợp lệ (empty string, null, negative numbers)
- Test với invalid lookup entity IDs (Expected: NOT_FOUND errors)
- Test với duplicate names (Expected: NAME_ALREADY_EXISTS errors)
- Test với missing required fields từng field một

### 2. Test Lookup Entity Management
- Test CRUD cho tất cả lookup entities
- Test delete lookup entity đang được reference (Expected: Foreign key constraint error)
- Test update lookup entity name và kiểm tra PC component response

### 3. Test Data Consistency
- Tạo 10+ entities mỗi loại để test scalability
- Kiểm tra foreign key constraints hoạt động đúng
- Query database trực tiếp để verify data integrity

### 4. Test Search/Filter (nếu có implement)
- Filter SSD by type (NVME/SATA)
- Filter Cooler by type (AIR/AIO)
- Search by name (partial match)

### 5. Test Compatibility Checking (nếu có implement)
- Kiểm tra cooler height phù hợp với case
- Kiểm tra PSU PCIe connector phù hợp với VGA requirements
- Kiểm tra radiator size phù hợp với case

### 6. Performance Testing
- Test pagination cho large datasets
- Test concurrent requests (multiple users)
- Monitor database query performance

### 7. Migration Testing
- Backup database trước khi migrate
- Run migration scripts để thêm lookup tables
- Migrate existing data từ String columns sang entity references
- Test rollback scenario nếu migration fail

---

## 📊 BREAKING CHANGES SUMMARY

> **⚠️ Chi tiết đầy đủ trong [CHANGELOG_2026-02-13.md](CHANGELOG_2026-02-13.md)**

### Removed Fields
- **SSD:** `readSpeed`, `writeSpeed` (MB/s) - Không cần thiết cho compatibility checking
- **HDD:** `rpm`, `cacheMb` - Không ảnh hưởng đến compatibility
- **PSU:** `modularType` - Không liên quan đến performance checking

### Renamed Fields
- **SSD:** `type` → `ssdTypeId` (String → Reference to SsdType entity)
- **SSD:** `interfaceType` → `ssdInterfaceId` (String → Reference to SsdInterface entity)
- **HDD:** `interfaceType` → `hddInterfaceId` (String → Reference to HddInterface entity)
- **PSU:** `pcieConnector` → `pcieConnectorId` (String → Reference to PcieConnector entity, now NULLABLE)
- **Cooler:** `type` → `coolerTypeId` (String → Reference to CoolerType entity)
- **Case:** `maxCoolerHeight` → `maxCoolerHeightMm` (Consistency with naming convention)

### New Endpoints
- GET/POST `/ssd-types` - Manage SSD types (SATA, NVME)
- GET/POST `/ssd-interfaces` - Manage SSD interfaces (SATA_3, PCIE_4, PCIE_5)
- GET/POST `/hdd-interfaces` - Manage HDD interfaces (SATA_3, SAS)
- GET/POST `/pcie-connectors` - Manage PSU PCIe connectors (2X8PIN, 3X8PIN, 12VHPWR, 16PIN)
- GET/POST `/cooler-types` - Manage cooler types (AIR, AIO)

### Error Code Changes
- **Added 54 new error codes** (2701-3119) for validation and duplicate checking
- All validation errors now use ErrorCode enum (not hardcoded messages)
- Each entity has consistent error code range and naming pattern

---

**Happy Testing! 🎉**

**Last Updated:** February 13, 2026  
**See:** [CHANGELOG_2026-02-13.md](CHANGELOG_2026-02-13.md) for complete refactoring details
