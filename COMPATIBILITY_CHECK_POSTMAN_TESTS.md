# 🧪 COMPATIBILITY CHECK - Postman Testing Guide

## 📋 Setup

1. Import vào Postman collection
2. Set environment variable: `BASE_URL = http://localhost:8080/identity`
3. Login để lấy JWT token
4. Set token vào Authorization header

---

## 🔐 Step 1: Login to Get Token

```http
POST {{BASE_URL}}/auth/token
Content-Type: application/json

{
  "email": "haoaboutme@gmail.com",
  "password": "admin"
}
```

**Response:**
```json
{
  "code": 1000,
  "result": {
    "token": "eyJhbGciOiJIUzUxMiJ9...",
    "authenticated": true
  }
}
```

Copy token và set vào Authorization tab: `Bearer <token>`

---

## 📦 Step 2: Get Component IDs

### Get CPUs
```http
GET {{BASE_URL}}/cpus
Authorization: Bearer <token>
```

### Get Mainboards
```http
GET {{BASE_URL}}/mainboards
Authorization: Bearer <token>
```

### Get RAMs
```http
GET {{BASE_URL}}/rams
Authorization: Bearer <token>
```

### Get VGAs
```http
GET {{BASE_URL}}/vgas
Authorization: Bearer <token>
```

### Get SSDs
```http
GET {{BASE_URL}}/ssds
Authorization: Bearer <token>
```

### Get HDDs
```http
GET {{BASE_URL}}/hdds
Authorization: Bearer <token>
```

### Get PSUs
```http
GET {{BASE_URL}}/psus
Authorization: Bearer <token>
```

### Get Cases
```http
GET {{BASE_URL}}/cases
Authorization: Bearer <token>
```

### Get Coolers
```http
GET {{BASE_URL}}/coolers
Authorization: Bearer <token>
```

---

## 🧪 Test Cases

### Test 1: Empty Build (Baseline)
```http
POST {{BASE_URL}}/builds/check-compatibility
Authorization: Bearer <token>
Content-Type: application/json

{
  "cpuId": null,
  "mainboardId": null,
  "ramId": null,
  "vgaId": null,
  "ssdIds": [],
  "hddIds": [],
  "psuId": null,
  "caseId": null,
  "coolerId": null
}
```

**Expected Response:**
```json
{
  "code": 1000,
  "result": {
    "compatible": true,
    "errors": [],
    "warnings": [],
    "recommendedPsuWattage": 90
  }
}
```

---

### Test 2: CPU + Mainboard (Compatible - Same Socket)
```http
POST {{BASE_URL}}/builds/check-compatibility
Authorization: Bearer <token>
Content-Type: application/json

{
  "cpuId": "<AMD-Ryzen-CPU-ID>",
  "mainboardId": "<AMD-AM5-Mainboard-ID>",
  "ramId": null,
  "vgaId": null,
  "ssdIds": [],
  "hddIds": [],
  "psuId": null,
  "caseId": null,
  "coolerId": null
}
```

**Expected Response:**
```json
{
  "compatible": true,
  "errors": [],
  "warnings": [],
  "recommendedPsuWattage": <calculated>
}
```

---

### Test 3: CPU + Mainboard (INCOMPATIBLE - Different Socket)
```http
POST {{BASE_URL}}/builds/check-compatibility
Authorization: Bearer <token>
Content-Type: application/json

{
  "cpuId": "<Intel-LGA1700-CPU-ID>",
  "mainboardId": "<AMD-AM5-Mainboard-ID>",
  "ramId": null,
  "vgaId": null,
  "ssdIds": [],
  "hddIds": [],
  "psuId": null,
  "caseId": null,
  "coolerId": null
}
```

**Expected Response:**
```json
{
  "compatible": false,
  "errors": [
    "CPU socket 'LGA1700' không khớp với Mainboard socket 'AM5'"
  ],
  "warnings": [],
  "recommendedPsuWattage": <calculated>
}
```

---

### Test 4: RAM Type Mismatch (DDR5 RAM + DDR4 Mainboard)
```http
POST {{BASE_URL}}/builds/check-compatibility
Authorization: Bearer <token>
Content-Type: application/json

{
  "cpuId": "<compatible-cpu-id>",
  "mainboardId": "<DDR4-mainboard-id>",
  "ramId": "<DDR5-ram-id>",
  "vgaId": null,
  "ssdIds": [],
  "hddIds": [],
  "psuId": null,
  "caseId": null,
  "coolerId": null
}
```

**Expected Response:**
```json
{
  "compatible": false,
  "errors": [
    "RAM type 'DDR5' không khớp với Mainboard RAM type 'DDR4'"
  ],
  "warnings": [],
  "recommendedPsuWattage": <calculated>
}
```

---

### Test 5: Single RAM Warning
```http
POST {{BASE_URL}}/builds/check-compatibility
Authorization: Bearer <token>
Content-Type: application/json

{
  "cpuId": "<cpu-id>",
  "mainboardId": "<mainboard-id>",
  "ramId": "<ram-1x16gb-id>",
  "vgaId": null,
  "ssdIds": [],
  "hddIds": [],
  "psuId": null,
  "caseId": null,
  "coolerId": null
}
```

**Expected Response:**
```json
{
  "compatible": true,
  "errors": [],
  "warnings": [
    "Chỉ có 1 thanh RAM - Khuyến nghị dùng 2 thanh để kích hoạt Dual Channel"
  ],
  "recommendedPsuWattage": <calculated>
}
```

---

### Test 6: VGA Too Long for Case
```http
POST {{BASE_URL}}/builds/check-compatibility
Authorization: Bearer <token>
Content-Type: application/json

{
  "cpuId": "<cpu-id>",
  "mainboardId": "<mainboard-id>",
  "ramId": null,
  "vgaId": "<rtx-4090-350mm-id>",
  "ssdIds": [],
  "hddIds": [],
  "psuId": null,
  "caseId": "<mini-itx-case-280mm-id>",
  "coolerId": null
}
```

**Expected Response:**
```json
{
  "compatible": false,
  "errors": [
    "VGA dài 350 mm vượt quá giới hạn Case (280 mm)"
  ],
  "warnings": [],
  "recommendedPsuWattage": <calculated>
}
```

---

### Test 7: No VGA + No iGPU Warning
```http
POST {{BASE_URL}}/builds/check-compatibility
Authorization: Bearer <token>
Content-Type: application/json

{
  "cpuId": "<cpu-without-igpu-id>",
  "mainboardId": "<mainboard-id>",
  "ramId": null,
  "vgaId": null,
  "ssdIds": [],
  "hddIds": [],
  "psuId": null,
  "caseId": null,
  "coolerId": null
}
```

**Expected Response:**
```json
{
  "compatible": true,
  "errors": [],
  "warnings": [
    "Không có VGA và CPU không có iGPU - Hệ thống không thể xuất hình"
  ],
  "recommendedPsuWattage": <calculated>
}
```

---

### Test 8: Too Many M.2 SSDs
```http
POST {{BASE_URL}}/builds/check-compatibility
Authorization: Bearer <token>
Content-Type: application/json

{
  "cpuId": "<cpu-id>",
  "mainboardId": "<mainboard-2-m2-slots-id>",
  "ramId": null,
  "vgaId": null,
  "ssdIds": [
    "<m2-ssd-1-id>",
    "<m2-ssd-2-id>",
    "<m2-ssd-3-id>"
  ],
  "hddIds": [],
  "psuId": null,
  "caseId": null,
  "coolerId": null
}
```

**Expected Response:**
```json
{
  "compatible": false,
  "errors": [
    "Số lượng SSD M.2 (3) vượt quá số khe M.2 của Mainboard (2)"
  ],
  "warnings": [],
  "recommendedPsuWattage": <calculated>
}
```

---

### Test 9: Cooler TDP Insufficient
```http
POST {{BASE_URL}}/builds/check-compatibility
Authorization: Bearer <token>
Content-Type: application/json

{
  "cpuId": "<i9-13900k-253w-id>",
  "mainboardId": "<compatible-mainboard-id>",
  "ramId": null,
  "vgaId": null,
  "ssdIds": [],
  "hddIds": [],
  "psuId": null,
  "caseId": null,
  "coolerId": "<air-cooler-150w-id>"
}
```

**Expected Response:**
```json
{
  "compatible": false,
  "errors": [
    "Cooler chỉ hỗ trợ TDP 150W, không đủ cho CPU 253W"
  ],
  "warnings": [],
  "recommendedPsuWattage": <calculated>
}
```

---

### Test 10: PSU Wattage Insufficient
```http
POST {{BASE_URL}}/builds/check-compatibility
Authorization: Bearer <token>
Content-Type: application/json

{
  "cpuId": "<high-end-cpu-id>",
  "mainboardId": "<mainboard-id>",
  "ramId": "<ram-id>",
  "vgaId": "<rtx-4090-450w-id>",
  "ssdIds": ["<ssd-id>"],
  "hddIds": [],
  "psuId": "<psu-450w-id>",
  "caseId": "<case-id>",
  "coolerId": "<cooler-id>"
}
```

**Expected Response:**
```json
{
  "compatible": false,
  "errors": [
    "PSU chỉ có 450W, không đủ cho hệ thống (khuyến nghị tối thiểu: 850W)"
  ],
  "warnings": [],
  "recommendedPsuWattage": 850
}
```

---

### Test 11: Full Compatible Build
```http
POST {{BASE_URL}}/builds/check-compatibility
Authorization: Bearer <token>
Content-Type: application/json

{
  "cpuId": "<ryzen-7-7800x3d-id>",
  "mainboardId": "<asus-b650-id>",
  "ramId": "<ddr5-32gb-2x16-id>",
  "vgaId": "<rtx-4070-id>",
  "ssdIds": ["<m2-nvme-1tb-id>"],
  "hddIds": ["<hdd-2tb-id>"],
  "psuId": "<psu-750w-gold-id>",
  "caseId": "<atx-mid-tower-id>",
  "coolerId": "<aio-280mm-id>"
}
```

**Expected Response:**
```json
{
  "compatible": true,
  "errors": [],
  "warnings": [],
  "recommendedPsuWattage": 550
}
```

---

### Test 12: Multiple Issues
```http
POST {{BASE_URL}}/builds/check-compatibility
Authorization: Bearer <token>
Content-Type: application/json

{
  "cpuId": "<intel-i9-13900k-id>",
  "mainboardId": "<amd-b650-id>",
  "ramId": "<ddr5-1x32gb-id>",
  "vgaId": "<rtx-4090-id>",
  "ssdIds": [
    "<m2-ssd-1>",
    "<m2-ssd-2>",
    "<m2-ssd-3>"
  ],
  "hddIds": [],
  "psuId": "<psu-450w-id>",
  "caseId": "<mini-itx-case-id>",
  "coolerId": "<air-cooler-120w-id>"
}
```

**Expected Response:**
```json
{
  "compatible": false,
  "errors": [
    "CPU socket 'LGA1700' không khớp với Mainboard socket 'AM5'",
    "Số lượng SSD M.2 (3) vượt quá số khe M.2 của Mainboard (2)",
    "VGA dài 350 mm vượt quá giới hạn Case (280 mm)",
    "Cooler chỉ hỗ trợ TDP 120W, không đủ cho CPU 253W",
    "PSU chỉ có 450W, không đủ cho hệ thống (khuyến nghị tối thiểu: 900W)"
  ],
  "warnings": [
    "Chỉ có 1 thanh RAM - Khuyến nghị dùng 2 thanh để kích hoạt Dual Channel"
  ],
  "recommendedPsuWattage": 900
}
```

---

## 🎯 Checklist for Complete Testing

- [ ] Test 1: Empty build
- [ ] Test 2: Compatible CPU + Mainboard
- [ ] Test 3: Socket mismatch
- [ ] Test 4: RAM type mismatch
- [ ] Test 5: Single RAM warning
- [ ] Test 6: VGA too long
- [ ] Test 7: No VGA + No iGPU
- [ ] Test 8: Too many M.2 SSDs
- [ ] Test 9: Cooler TDP insufficient
- [ ] Test 10: PSU wattage insufficient
- [ ] Test 11: Full compatible build
- [ ] Test 12: Multiple issues

---

## 📝 Notes

1. Replace `<xxx-id>` with actual UUIDs from your database
2. All tests should return HTTP 200 OK
3. No exceptions should be thrown
4. Check both `errors` and `warnings` arrays
5. Verify `recommendedPsuWattage` is calculated correctly

---

**Version:** 1.0.0  
**Created:** 2026-02-28

