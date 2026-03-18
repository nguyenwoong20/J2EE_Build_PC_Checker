# 🚀 QUICK REFERENCE - TEST BUILD COMPATIBILITY

## 📍 BƯỚC 1: MỞ SWAGGER
```
http://localhost:8080/identity/swagger-ui.html
```

## 🔐 BƯỚC 2: ĐĂNG NHẬP

**POST /auth/token**
```json
{
  "username": "admin",
  "password": "admin"
}
```

**Copy token → Click "Authorize" 🔓 → Nhập:**
```
Bearer eyJhbGciOiJIUzUxMiJ9...
```

## 🔍 BƯỚC 3: LẤY COMPONENT IDs

### Lấy CPU ID
**GET /cpus** → Copy `id` của CPU bạn muốn

### Lấy Mainboard ID  
**GET /mainboards** → Copy `id`

### Lấy RAM ID
**GET /rams** → Copy `id`

### Lấy VGA ID
**GET /vgas** → Copy `id`

### Lấy SSD IDs
**GET /ssds** → Copy list `id`

### Lấy HDD IDs
**GET /hdds** → Copy list `id`

### Lấy PSU ID
**GET /psus** → Copy `id`

### Lấy Case ID
**GET /cases** → Copy `id`

### Lấy Cooler ID
**GET /coolers** → Copy `id`

## ✅ BƯỚC 4: TEST COMPATIBILITY

**POST /builds/check-compatibility**

### Template Request:
```json
{
  "cpuId": "paste-cpu-id-here",
  "mainboardId": "paste-mainboard-id-here",
  "ramId": "paste-ram-id-here",
  "vgaId": "paste-vga-id-here",
  "ssdIds": ["paste-ssd-id-1", "paste-ssd-id-2"],
  "hddIds": ["paste-hdd-id-1"],
  "psuId": "paste-psu-id-here",
  "caseId": "paste-case-id-here",
  "coolerId": "paste-cooler-id-here"
}
```

### Ví dụ:
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

## 📊 RESPONSE

### ✅ Compatible Build:
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

### ❌ Incompatible Build:
```json
{
  "code": 1000,
  "message": "Success",
  "result": {
    "compatible": false,
    "errors": [
      "CPU socket 'LGA1700' không khớp với Mainboard socket 'AM5'",
      "RAM type 'DDR5' không khớp với Mainboard RAM type 'DDR4'",
      "PSU chỉ có 450W, không đủ cho hệ thống (khuyến nghị tối thiểu: 650W)"
    ],
    "warnings": [],
    "recommendedPsuWattage": 650
  }
}
```

## 🧪 TEST SCENARIOS NHANH

### Test 1: Socket Mismatch
Chọn CPU Intel (LGA1700) + Mainboard AMD (AM5)
→ Expect: Error "CPU socket không khớp"

### Test 2: RAM Type Mismatch  
Chọn Mainboard DDR4 + RAM DDR5
→ Expect: Error "RAM type không khớp"

### Test 3: PSU Không Đủ
Chọn CPU cao cấp + VGA mạnh + PSU 450W
→ Expect: Error "PSU không đủ công suất"

### Test 4: VGA Quá Dài
Chọn VGA RTX 4090 (355mm) + Mini-ITX Case (max 280mm)
→ Expect: Error "VGA dài vượt quá Case max"

### Test 5: PCIe Backward Compatible
Chọn Mainboard PCIe 3.0 + VGA PCIe 4.0
→ Expect: Warning "VGA sẽ chạy ở PCIe 3.0"

### Test 6: Single RAM Warning
Chọn chỉ 1 thanh RAM
→ Expect: Warning "Khuyến nghị dùng 2 thanh Dual Channel"

## 🐛 TROUBLESHOOTING

### ❌ 401 Unauthorized
→ Token hết hạn, đăng nhập lại ở POST /auth/token

### ❌ 404 Not Found (Component)
→ ID không tồn tại, check lại bằng GET endpoints

### ❌ Empty Response
→ Database chưa có data, tạo components trước

### ❌ Swagger không load
→ Check app đã start: `netstat -ano | findstr :8080`

## 📝 CHECKLIST

- [ ] Start app: `mvn spring-boot:run`
- [ ] Mở Swagger: http://localhost:8080/identity/swagger-ui.html
- [ ] Login: POST /auth/token (admin/admin)
- [ ] Authorize: Click 🔓, nhập `Bearer <token>`
- [ ] Lấy IDs: GET /cpus, /mainboards, /rams, v.v.
- [ ] Test: POST /builds/check-compatibility
- [ ] Kiểm tra response: compatible, errors, warnings, recommendedPsuWattage

## 📚 TÀI LIỆU ĐẦY ĐỦ

- **TESTING_BUILD_COMPATIBILITY_SWAGGER_GUIDE.md** - Hướng dẫn chi tiết
- **PROJECT_FEATURES_SUMMARY.md** - Tổng hợp chức năng
- **COMPATIBILITY_CHECK_IMPLEMENTATION_SUMMARY.md** - Implementation details

---

**All Done! Happy Testing! 🎉**

