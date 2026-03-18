# ⚡ COMPATIBILITY CHECK - Quick Start Guide

## 🎯 What is This?

API để kiểm tra tương thích linh kiện PC trong 1 request duy nhất.

**Endpoint:** `POST /builds/check-compatibility`

---

## 🚀 Quick Test (3 Steps)

### Step 1: Login & Get Token
```bash
curl -X POST http://localhost:8080/identity/auth/token \
  -H "Content-Type: application/json" \
  -d '{
    "email": "haoaboutme@gmail.com",
    "password": "admin"
  }'
```

Copy `token` từ response.

---

### Step 2: Get Component IDs
```bash
# Get CPU list
curl http://localhost:8080/identity/cpus \
  -H "Authorization: Bearer YOUR_TOKEN"

# Get Mainboard list
curl http://localhost:8080/identity/mainboards \
  -H "Authorization: Bearer YOUR_TOKEN"
```

Copy các `id` bạn muốn test.

---

### Step 3: Check Compatibility
```bash
curl -X POST http://localhost:8080/identity/builds/check-compatibility \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "cpuId": "your-cpu-id",
    "mainboardId": "your-mainboard-id",
    "ramId": "your-ram-id",
    "vgaId": null,
    "ssdIds": [],
    "hddIds": [],
    "psuId": null,
    "caseId": null,
    "coolerId": null
  }'
```

---

## 📤 Response Examples

### ✅ Compatible Build
```json
{
  "code": 1000,
  "result": {
    "compatible": true,
    "errors": [],
    "warnings": [],
    "recommendedPsuWattage": 450
  }
}
```

### ❌ Incompatible Build
```json
{
  "code": 1000,
  "result": {
    "compatible": false,
    "errors": [
      "CPU socket 'LGA1700' không khớp với Mainboard socket 'AM5'"
    ],
    "warnings": [],
    "recommendedPsuWattage": 450
  }
}
```

---

## 🎯 What Gets Checked?

| Layer | What | Example Error |
|-------|------|--------------|
| 1️⃣ CPU-MB | Socket, VRM, TDP | "Socket không khớp" |
| 2️⃣ RAM | Type, Bus, Slots | "RAM type không khớp" |
| 3️⃣ Case+VGA | Size, Length, PCIe | "VGA quá dài" |
| 4️⃣ Storage | M.2, SATA slots | "Không đủ M.2 slot" |
| 5️⃣ Cooler | TDP, Height | "Cooler không đủ TDP" |
| 6️⃣ Power | Wattage, Connectors | "PSU không đủ công suất" |

---

## 💡 Pro Tips

### Tip 1: Partial Build Support
Không cần chọn đủ tất cả linh kiện. API sẽ check những gì có.

```json
{
  "cpuId": "xxx",
  "mainboardId": "yyy"
  // Rest can be null or omitted
}
```

### Tip 2: Error vs Warning
- **errors** = Build không hoạt động ❌
- **warnings** = Build hoạt động nhưng không tối ưu ⚠️

### Tip 3: PSU Recommendation
API tự tính công suất khuyến nghị:
```
recommendedPsuWattage = (Total TDP) × 1.2
```

---

## 📚 Full Documentation

- **API Guide:** [COMPATIBILITY_CHECK_API_GUIDE.md](COMPATIBILITY_CHECK_API_GUIDE.md)
- **Postman Tests:** [COMPATIBILITY_CHECK_POSTMAN_TESTS.md](COMPATIBILITY_CHECK_POSTMAN_TESTS.md)
- **Implementation:** [COMPATIBILITY_CHECK_IMPLEMENTATION_SUMMARY.md](COMPATIBILITY_CHECK_IMPLEMENTATION_SUMMARY.md)

---

## 🐛 Troubleshooting

### Problem: "401 Unauthorized"
→ Token expired. Login lại để lấy token mới.

### Problem: "404 Not Found"
→ Check endpoint: `/builds/check-compatibility` (không có `/api/`)

### Problem: Response có errors nhưng compatible=true
→ Không thể xảy ra. Nếu có errors thì compatible=false.

---

**Quick Start Complete!** 🎉

For detailed testing scenarios, see [COMPATIBILITY_CHECK_POSTMAN_TESTS.md](COMPATIBILITY_CHECK_POSTMAN_TESTS.md)

