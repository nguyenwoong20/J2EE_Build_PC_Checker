# Case Size Migration Summary

## Ngày: 2026-02-21

## Mục đích
Chuyển đổi trường `size` trong `PcCase` từ `String` sang một entity riêng biệt `CaseSize` để:
- Tránh lỗi nhập liệu không đúng định dạng
- Đảm bảo tính nhất quán dữ liệu
- Dễ dàng quản lý các giá trị size hợp lệ (ATX, mATX, ITX)

## Các file đã tạo mới

### 1. Entity
- **CaseSize.java** - Entity cho Case Size
  - `id`: String (PK) - Ví dụ: ATX, mATX, ITX
  - `name`: String - Tên đầy đủ

### 2. Repository
- **CaseSizeRepository.java** - JPA Repository cho CaseSize

### 3. DTO (Data Transfer Objects)
- **CaseSizeRequest.java** - Request DTO cho create/update CaseSize
- **CaseSizeResponse.java** - Response DTO cho CaseSize

### 4. Mapper
- **CaseSizeMapper.java** - MapStruct mapper cho CaseSize

### 5. Service
- **CaseSizeService.java** - Business logic cho CaseSize
  - `createCaseSize()` - Tạo mới Case Size
  - `getAllCaseSizes()` - Lấy tất cả Case Sizes
  - `getCaseSizeById()` - Lấy Case Size theo ID
  - `updateCaseSize()` - Cập nhật Case Size
  - `deleteCaseSize()` - Xóa Case Size

### 6. Controller
- **CaseSizeController.java** - REST API endpoints cho CaseSize
  - POST `/case-sizes` - Tạo mới
  - GET `/case-sizes` - Lấy tất cả
  - GET `/case-sizes/{id}` - Lấy theo ID
  - PUT `/case-sizes/{id}` - Cập nhật
  - DELETE `/case-sizes/{id}` - Xóa

## Các file đã chỉnh sửa

### 1. Entity
- **PcCase.java**
  - Thay đổi: `String size` → `@ManyToOne CaseSize size`
  - Thêm: `@JoinColumn(name = "size_id", nullable = false)`

### 2. ErrorCode
- **ErrorCode.java**
  - Thêm: `CASE_SIZE_ID_REQUIRED` (3141)
  - Thêm: `CASE_SIZE_NAME_REQUIRED` (3142)
  - Thêm: `CASE_SIZE_NOT_FOUND` (3143)
  - Thêm: `CASE_SIZE_ALREADY_EXISTS` (3144)

### 3. DTO
- **CaseCreationRequest.java**
  - Thay đổi: `String size` → `String sizeId`

- **CaseUpdateRequest.java**
  - Thay đổi: `String size` → `String sizeId`

- **CaseResponse.java**
  - Thay đổi: `String size` → `CaseSizeResponse size`

### 4. Mapper
- **CaseMapper.java**
  - Thêm: `@Mapping(target = "size", ignore = true)` cho `toPcCase()`
  - Thêm: `@Mapping(target = "size", ignore = true)` cho `updatePcCase()`

### 5. Service
- **CaseService.java**
  - Thêm dependency: `CaseSizeRepository`
  - Cập nhật `createCase()`: Lấy CaseSize từ repository và set vào PcCase
  - Cập nhật `updateCase()`: Xử lý update CaseSize nếu sizeId được cung cấp
  - Thay đổi: Tất cả `RuntimeException` → `AppException` với ErrorCode tương ứng

## Cấu trúc Database

### Bảng mới: `case_size`
```sql
CREATE TABLE case_size (
    id VARCHAR PRIMARY KEY,    -- ATX, mATX, ITX
    name VARCHAR NOT NULL      -- Full form / Mini Tower / Mid Tower
);
```

### Thay đổi bảng: `pc_case`
```sql
-- Trước:
size VARCHAR NOT NULL

-- Sau:
size_id VARCHAR NOT NULL,
FOREIGN KEY (size_id) REFERENCES case_size(id)
```

## Error Codes

| Code | Message | HTTP Status |
|------|---------|-------------|
| 3141 | Case Size ID is required | 400 BAD_REQUEST |
| 3142 | Case Size name is required | 400 BAD_REQUEST |
| 3143 | Case Size not found | 404 NOT_FOUND |
| 3144 | Case Size already exists | 400 BAD_REQUEST |

## API Examples

### 1. Tạo Case Size mới
```http
POST /case-sizes
Content-Type: application/json

{
  "id": "ATX",
  "name": "ATX Full Tower"
}
```

### 2. Lấy tất cả Case Sizes
```http
GET /case-sizes
```

### 3. Tạo PC Case với size mới
```http
POST /cases
Content-Type: application/json

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

### 4. Cập nhật PC Case với size khác
```http
PUT /cases/{id}
Content-Type: application/json

{
  "sizeId": "mATX",
  "maxVgaLengthMm": 350
}
```

## Migration Steps

### Bước 1: Tạo bảng case_size và insert dữ liệu
```sql
-- Tạo bảng
CREATE TABLE case_size (
    id VARCHAR PRIMARY KEY,
    name VARCHAR NOT NULL
);

-- Insert các giá trị phổ biến
INSERT INTO case_size (id, name) VALUES 
('ATX', 'ATX Full Tower'),
('mATX', 'Micro ATX'),
('ITX', 'Mini ITX');
```

### Bước 2: Migrate dữ liệu từ pc_case
```sql
-- Thêm cột mới
ALTER TABLE pc_case ADD COLUMN size_id VARCHAR;

-- Migrate dữ liệu (điều chỉnh theo giá trị thực tế trong DB)
UPDATE pc_case 
SET size_id = 'ATX' 
WHERE size = 'ATX' OR size = 'atx' OR size LIKE '%ATX%';

UPDATE pc_case 
SET size_id = 'mATX' 
WHERE size = 'mATX' OR size = 'matx' OR size LIKE '%mATX%';

UPDATE pc_case 
SET size_id = 'ITX' 
WHERE size = 'ITX' OR size = 'itx' OR size LIKE '%ITX%';

-- Thêm foreign key constraint
ALTER TABLE pc_case 
ADD CONSTRAINT fk_pc_case_size 
FOREIGN KEY (size_id) REFERENCES case_size(id);

-- Đặt NOT NULL cho size_id
ALTER TABLE pc_case ALTER COLUMN size_id SET NOT NULL;

-- Xóa cột cũ
ALTER TABLE pc_case DROP COLUMN size;
```

## Testing Checklist

- [ ] Tạo Case Size mới qua API
- [ ] Lấy danh sách Case Sizes
- [ ] Lấy Case Size theo ID
- [ ] Cập nhật Case Size
- [ ] Xóa Case Size
- [ ] Tạo PC Case với sizeId hợp lệ
- [ ] Tạo PC Case với sizeId không tồn tại (expect error CASE_SIZE_NOT_FOUND)
- [ ] Cập nhật PC Case với sizeId mới
- [ ] Kiểm tra response trả về CaseSizeResponse trong CaseResponse

## Notes

- Tất cả error handling đã sử dụng `AppException` với `ErrorCode` thay vì hardcode message
- MapStruct sẽ tự động map nested object `CaseSize` thành `CaseSizeResponse` trong response
- Khi update Case, nếu không cung cấp `sizeId`, giá trị cũ sẽ được giữ nguyên
- Validation message keys đã được thêm vào DTO Request classes

## Compatibility

- **Backend**: Hoàn toàn tương thích với cấu trúc hiện tại (tương tự Socket, RamType, PcieVersion)
- **Database**: Cần chạy migration script trước khi deploy
- **API**: Breaking change - client phải thay đổi từ `"size": "ATX"` sang `"sizeId": "ATX"`

