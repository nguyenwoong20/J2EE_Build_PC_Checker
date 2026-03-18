# Form Factor Entity - Quick Start Guide

## Tóm tắt thay đổi
Đã chuyển đổi trường `formFactor` từ String sang Entity độc lập cho HDD và SSD.

## 📋 Files đã tạo mới

### Backend Files
1. **Entity:** `FormFactor.java`
2. **Repository:** `FormFactorRepository.java`
3. **DTO Request:** `FormFactorRequest.java`
4. **DTO Response:** `FormFactorResponse.java`
5. **Mapper:** `FormFactorMapper.java`
6. **Service:** `FormFactorService.java`
7. **Controller:** `FormFactorController.java`

### Documentation Files
1. **SQL Migration:** `FORM_FACTOR_MIGRATION.sql`
2. **API Examples:** `FORM_FACTOR_API_EXAMPLES.md`
3. **Changelog:** `CHANGELOG_FORM_FACTOR_2026-02-14.md`

## 📝 Files đã cập nhật

### Entities
- `Hdd.java` - Thay String thành FormFactor entity
- `Ssd.java` - Thay String thành FormFactor entity

### DTOs
- `HddCreationRequest.java` - Đổi `formFactor` → `formFactorId`
- `HddUpdateRequest.java` - Đổi `formFactor` → `formFactorId`
- `HddResponse.java` - Đổi String → `FormFactorResponse`
- `SsdCreationRequest.java` - Đổi `formFactor` → `formFactorId`
- `SsdUpdateRequest.java` - Đổi `formFactor` → `formFactorId`
- `SsdResponse.java` - Đổi String → `FormFactorResponse`

### Mappers
- `HddMapper.java` - Thêm mapping để ignore formFactor
- `SsdMapper.java` - Thêm mapping để ignore formFactor

### Services
- `HddService.java` - Xử lý FormFactor entity
- `SsdService.java` - Xử lý FormFactor entity

### Exception
- `ErrorCode.java` - Thêm error codes cho FormFactor

## 🚀 Hướng dẫn sử dụng nhanh

### 1. Chạy SQL Migration
```sql
-- Xem file FORM_FACTOR_MIGRATION.sql để có script đầy đủ
mysql -u your_username -p your_database < FORM_FACTOR_MIGRATION.sql
```

### 2. Build & Run Application
```bash
mvn clean install
mvn spring-boot:run
```

### 3. Tạo Form Factors (Postman/Curl)

**Tạo FormFactor 2.5":**
```bash
curl -X POST http://localhost:8080/form-factors \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "id": "FF_2_5",
    "name": "2.5\""
  }'
```

**Tạo FormFactor 3.5":**
```bash
curl -X POST http://localhost:8080/form-factors \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "id": "FF_3_5",
    "name": "3.5\""
  }'
```

**Tạo FormFactor M.2 2280:**
```bash
curl -X POST http://localhost:8080/form-factors \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "id": "M2_2280",
    "name": "M.2 2280"
  }'
```

### 4. Tạo HDD với FormFactor mới
```bash
curl -X POST http://localhost:8080/hdds \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Seagate Barracuda 2TB",
    "formFactorId": "FF_3_5",
    "interfaceTypeId": "SATA_3",
    "capacity": 2000,
    "tdp": 6,
    "description": "High-performance 7200 RPM"
  }'
```

### 5. Tạo SSD với FormFactor mới
```bash
curl -X POST http://localhost:8080/ssds \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Samsung 980 PRO 1TB",
    "ssdTypeId": "NVME",
    "formFactorId": "M2_2280",
    "interfaceTypeId": "PCIE_4",
    "capacity": 1000,
    "tdp": 7,
    "description": "High-speed NVMe SSD"
  }'
```

## 📊 Form Factor IDs

| ID | Name | Sử dụng cho |
|---|---|---|
| `FF_2_5` | 2.5" | HDD, SSD SATA |
| `FF_3_5` | 3.5" | HDD |
| `M2_2280` | M.2 2280 | SSD NVMe/SATA |
| `M2_2260` | M.2 2260 | SSD NVMe/SATA |
| `M2_2242` | M.2 2242 | SSD NVMe/SATA |
| `M2_22110` | M.2 22110 | SSD NVMe/SATA |

## ⚠️ Breaking Changes

### Request Body Changes
```json
// CŨ ❌
{
  "name": "My HDD",
  "formFactor": "3.5\"",
  "interfaceTypeId": "SATA_3"
}

// MỚI ✅
{
  "name": "My HDD",
  "formFactorId": "FF_3_5",
  "interfaceTypeId": "SATA_3"
}
```

### Response Body Changes
```json
// CŨ ❌
{
  "id": "uuid",
  "name": "My HDD",
  "formFactor": "3.5\"",
  "interfaceType": {...}
}

// MỚI ✅
{
  "id": "uuid",
  "name": "My HDD",
  "formFactor": {
    "id": "FF_3_5",
    "name": "3.5\""
  },
  "interfaceType": {...}
}
```

## 🔍 API Endpoints mới

```
GET    /form-factors           - Lấy tất cả form factors
POST   /form-factors           - Tạo form factor mới
GET    /form-factors/{id}      - Lấy form factor theo ID
PUT    /form-factors/{id}      - Cập nhật form factor
DELETE /form-factors/{id}      - Xóa form factor
```

## 📚 Documentation

- **Chi tiết SQL Migration:** [FORM_FACTOR_MIGRATION.sql](./FORM_FACTOR_MIGRATION.sql)
- **API Examples đầy đủ:** [FORM_FACTOR_API_EXAMPLES.md](FORM_FACTOR_API_EXAMPLES.md)
- **Changelog chi tiết:** [CHANGELOG_FORM_FACTOR_2026-02-14.md](CHANGELOG_FORM_FACTOR_2026-02-14.md)

## ✅ Testing Checklist

- [ ] Run SQL migration
- [ ] Build application successfully
- [ ] Create all form factors
- [ ] Create HDD with formFactorId
- [ ] Create SSD with formFactorId
- [ ] Get HDD/SSD and verify FormFactorResponse
- [ ] Update HDD/SSD with new formFactorId
- [ ] Test error cases (invalid formFactorId)

## 🎯 Lợi ích

1. ✅ **Nhất quán dữ liệu** - Form factor được chuẩn hóa
2. ✅ **Dễ bảo trì** - Quản lý tập trung
3. ✅ **Data integrity** - Foreign key constraints
4. ✅ **Mở rộng dễ dàng** - Thêm form factor mới không cần sửa code
5. ✅ **Performance** - Indexed foreign keys

## 💡 Tips

- Luôn tạo FormFactor trước khi tạo HDD/SSD
- Sử dụng ID chuẩn (FF_2_5, FF_3_5, M2_2280, etc.)
- Check documentation để biết thêm examples

---

**Date:** February 14, 2026  
**Version:** 1.0.0

