# 🔍 COMPATIBILITY CHECK API - Complete Guide

## 📋 Overview

API để kiểm tra tương thích linh kiện PC build.

**Endpoint:** `POST /builds/check-compatibility`

**Features:**
- ✅ Kiểm tra tương thích đầy đủ giữa tất cả linh kiện
- ✅ Hỗ trợ partial build (không bắt buộc chọn đủ tất cả)
- ✅ Tính toán công suất khuyến nghị cho PSU
- ✅ Phân biệt rõ **errors** (không build được) và **warnings** (cảnh báo)
- ✅ KHÔNG throw exception nếu không tương thích

---

## 🎯 Request Format

### Endpoint
```
POST http://localhost:8080/identity/builds/check-compatibility
Content-Type: application/json
```

### Request Body
```json
{
  "cpuId": "uuid-cpu-id",
  "mainboardId": "uuid-mainboard-id",
  "ramId": "uuid-ram-id",
  "vgaId": "uuid-vga-id",
  "ssdIds": ["uuid-ssd-1", "uuid-ssd-2"],
  "hddIds": ["uuid-hdd-1"],
  "psuId": "uuid-psu-id",
  "caseId": "uuid-case-id",
  "coolerId": "uuid-cooler-id"
}
```

**Lưu ý:** Tất cả các field đều **optional** (có thể null hoặc empty).

---

## 📤 Response Format

### Success Response (Compatible Build)
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

### Success Response (Incompatible Build)
```json
{
  "code": 1000,
  "message": "Success",
  "result": {
    "compatible": false,
    "errors": [
      "CPU socket 'AM5' không khớp với Mainboard socket 'LGA1700'",
      "RAM type 'DDR5' không khớp với Mainboard RAM type 'DDR4'",
      "PSU chỉ có 450W, không đủ cho hệ thống (khuyến nghị tối thiểu: 550W)"
    ],
    "warnings": [
      "Có thể xảy ra bottleneck: CPU score (9500) và VGA score (15000) chênh lệch > 40%"
    ],
    "recommendedPsuWattage": 550
  }
}
```

---

## 🧠 Compatibility Checking Logic

### LAYER 1: CPU ↔ MAINBOARD (CORE)
**Critical Checks:**
1. ✅ Socket phải khớp chính xác
2. ✅ Mainboard VRM phase >= CPU minimum VRM requirement
3. ✅ Mainboard TDP support >= CPU TDP

**Errors:**
- "CPU socket '%s' không khớp với Mainboard socket '%s'"
- "Mainboard VRM (%d phase) không đủ cho CPU (yêu cầu tối thiểu %d phase)"
- "Mainboard chỉ hỗ trợ TDP %dW, không đủ cho CPU %dW"

---

### LAYER 2: RAM
**Checks:**
1. ✅ RAM type (DDR4/DDR5) phải khớp với Mainboard
2. ✅ RAM bus <= Mainboard max RAM bus
3. ✅ Số lượng RAM <= số khe RAM của Mainboard
4. ✅ Tổng dung lượng RAM <= giới hạn của Mainboard

**Errors:**
- "RAM type '%s' không khớp với Mainboard RAM type '%s'"
- "RAM bus %d MHz vượt quá giới hạn Mainboard (%d MHz)"
- "Số lượng RAM (%d thanh) vượt quá số khe của Mainboard (%d khe)"
- "Tổng dung lượng RAM (%d GB) vượt quá giới hạn Mainboard (%d GB)"

**Warnings:**
- "Chỉ có 1 thanh RAM - Khuyến nghị dùng 2 thanh để kích hoạt Dual Channel"

---

### LAYER 3: CASE + VGA
**Checks:**
1. ✅ Case size phải hỗ trợ Mainboard size (ATX > mATX > ITX)
2. ✅ Chiều dài VGA phải vừa Case
3. ✅ PCIe version compatibility (backward compatible)
4. ✅ Kiểm tra iGPU nếu không có VGA
5. ✅ Kiểm tra bottleneck CPU-VGA (score difference > 40%)

**Errors:**
- "Mainboard size '%s' không được Case size '%s' hỗ trợ"
- "VGA dài %d mm vượt quá giới hạn Case (%d mm)"

**Warnings:**
- "VGA PCIe %s sẽ chạy ở tốc độ thấp hơn do Mainboard chỉ hỗ trợ %s (backward compatible)"
- "Không có VGA và CPU không có iGPU - Hệ thống không thể xuất hình"
- "Có thể xảy ra bottleneck: CPU score (%d) và VGA score (%d) chênh lệch > 40%%"

---

### LAYER 4: STORAGE
**Checks:**
1. ✅ Số lượng SSD M.2 <= M.2 slots của Mainboard
2. ✅ Tổng số ổ SATA (SSD SATA + HDD) <= SATA ports của Mainboard
3. ✅ Số lượng HDD 3.5" <= số khay 3.5" của Case
4. ✅ Số lượng ổ 2.5" <= số khay 2.5" của Case

**Errors:**
- "Số lượng SSD M.2 (%d) vượt quá số khe M.2 của Mainboard (%d)"
- "Tổng số ổ SATA (%d SSD + %d HDD = %d) vượt quá số cổng SATA của Mainboard (%d)"
- "Số lượng HDD 3.5\" (%d) vượt quá số khay 3.5\" của Case (%d)"
- "Số lượng ổ 2.5\" (%d) vượt quá số khay 2.5\" của Case (%d)"

---

### LAYER 5: COOLER
**Checks:**
1. ✅ Cooler TDP support >= CPU TDP
2. ✅ AIR cooler: Height <= Case max cooler height
3. ✅ AIO cooler: Radiator size <= Case max radiator support

**Errors:**
- "Cooler chỉ hỗ trợ TDP %dW, không đủ cho CPU %dW"
- "Tản khí cao %d mm vượt quá giới hạn Case (%d mm)"
- "Case không hỗ trợ radiator %d mm (giới hạn: %d mm)"

---

### LAYER 6: POWER (FINAL)
**Calculations:**
```
Total TDP = CPU.tdp 
          + VGA.tdp (if exists)
          + (RAM.tdp × RAM.quantity)
          + SUM(SSD.tdp)
          + SUM(HDD.tdp)
          + 75W (system overhead)

Recommended PSU Wattage = Total TDP × 1.2
```

**Checks:**
1. ✅ PSU wattage >= Recommended wattage
2. ✅ PSU có đủ PCIe power connector cho VGA
3. ✅ PSU có đủ SATA power connector cho ổ SATA

**Errors:**
- "PSU chỉ có %dW, không đủ cho hệ thống (khuyến nghị tối thiểu: %dW)"
- "PSU không có đủ connector '%s' cho VGA"
- "PSU chỉ có %d đầu SATA, không đủ cho %d ổ SATA"

---

## 📝 Test Examples

### Example 1: Partial Build (Only CPU + Mainboard)
```json
POST /builds/check-compatibility
{
  "cpuId": "cpu-ryzen-5-7600x",
  "mainboardId": "mainboard-asus-b650"
}
```

**Response:**
```json
{
  "compatible": true,
  "errors": [],
  "warnings": [],
  "recommendedPsuWattage": 162  // (105W CPU + 75W overhead) × 1.2
}
```

---

### Example 2: Full Build with Compatibility Issues
```json
POST /builds/check-compatibility
{
  "cpuId": "cpu-intel-i9-13900k",
  "mainboardId": "mainboard-amd-b650",
  "ramId": "ram-ddr5-32gb",
  "vgaId": "vga-rtx-4090",
  "ssdIds": ["ssd-samsung-980-pro"],
  "psuId": "psu-450w",
  "caseId": "case-itx-mini",
  "coolerId": "cooler-air-120mm"
}
```

**Response:**
```json
{
  "compatible": false,
  "errors": [
    "CPU socket 'LGA1700' không khớp với Mainboard socket 'AM5'",
    "VGA dài 350 mm vượt quá giới hạn Case (280 mm)",
    "PSU chỉ có 450W, không đủ cho hệ thống (khuyến nghị tối thiểu: 850W)",
    "Cooler chỉ hỗ trợ TDP 150W, không đủ cho CPU 253W"
  ],
  "warnings": [],
  "recommendedPsuWattage": 850
}
```

---

### Example 3: Compatible Build with Warnings
```json
POST /builds/check-compatibility
{
  "cpuId": "cpu-ryzen-7-7800x3d",
  "mainboardId": "mainboard-asus-b650",
  "ramId": "ram-ddr5-16gb-1stick",
  "vgaId": "vga-rtx-4090",
  "psuId": "psu-1000w",
  "caseId": "case-atx-full-tower",
  "coolerId": "cooler-aio-360mm"
}
```

**Response:**
```json
{
  "compatible": true,
  "errors": [],
  "warnings": [
    "Chỉ có 1 thanh RAM - Khuyến nghị dùng 2 thanh để kích hoạt Dual Channel",
    "Có thể xảy ra bottleneck: CPU score (8000) và VGA score (18000) chênh lệch > 40%"
  ],
  "recommendedPsuWattage": 600
}
```

---

## 🔐 Authentication

API này yêu cầu JWT token trong header:

```
Authorization: Bearer <your-jwt-token>
```

---

## 🚨 Error Handling

API này **KHÔNG throw exception** khi build không tương thích.

Tất cả các vấn đề tương thích được báo cáo qua:
- `errors[]` - Vấn đề nghiêm trọng, build không thể hoạt động
- `warnings[]` - Cảnh báo, build vẫn hoạt động nhưng không tối ưu

Response luôn là **200 OK** với `code: 1000`.

---

## 📚 Architecture

```
BuildController
    ↓
CompatibilityService
    ↓
    ├─→ CpuMainboardChecker   (LAYER 1)
    ├─→ RamChecker            (LAYER 2)
    ├─→ CaseChecker           (LAYER 3)
    ├─→ StorageChecker        (LAYER 4)
    ├─→ CoolerChecker         (LAYER 5)
    └─→ PowerChecker          (LAYER 6)
```

Mỗi checker độc lập, dễ maintain và extend.

---

## 🎯 Future Enhancements

- [ ] Detailed bottleneck analysis
- [ ] Multiple cooling solutions check
- [ ] RGB compatibility check
- [ ] Noise level estimation
- [ ] Temperature estimation
- [ ] Build cost calculation

---

**Version:** 1.0.0  
**Created:** 2026-02-28  
**Author:** Senior Backend Engineer

