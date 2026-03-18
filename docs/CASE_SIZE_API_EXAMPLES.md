# Case Size API Testing Examples

## Base URL
```
http://localhost:8080
```

## Authentication
Tất cả các request cần có Authorization header (nếu được bật security):
```
Authorization: Bearer {your-jwt-token}
```

---

## 1. CASE SIZE CRUD Operations

### 1.1. Create Case Size - ATX
```http
POST /case-sizes
Content-Type: application/json

{
  "id": "ATX",
  "name": "ATX Full Tower"
}
```

**Expected Response (200 OK):**
```json
{
  "code": 1000,
  "message": "Success",
  "result": {
    "id": "ATX",
    "name": "ATX Full Tower"
  }
}
```

### 1.2. Create Case Size - mATX
```http
POST /case-sizes
Content-Type: application/json

{
  "id": "mATX",
  "name": "Micro ATX"
}
```

### 1.3. Create Case Size - ITX
```http
POST /case-sizes
Content-Type: application/json

{
  "id": "ITX",
  "name": "Mini ITX"
}
```

### 1.4. Get All Case Sizes
```http
GET /case-sizes
```

**Expected Response (200 OK):**
```json
{
  "code": 1000,
  "message": "Success",
  "result": [
    {
      "id": "ATX",
      "name": "ATX Full Tower"
    },
    {
      "id": "mATX",
      "name": "Micro ATX"
    },
    {
      "id": "ITX",
      "name": "Mini ITX"
    }
  ]
}
```

### 1.5. Get Case Size by ID
```http
GET /case-sizes/ATX
```

**Expected Response (200 OK):**
```json
{
  "code": 1000,
  "message": "Success",
  "result": {
    "id": "ATX",
    "name": "ATX Full Tower"
  }
}
```

### 1.6. Update Case Size
```http
PUT /case-sizes/ATX
Content-Type: application/json

{
  "id": "ATX",
  "name": "ATX Full Tower (Updated)"
}
```

### 1.7. Delete Case Size
```http
DELETE /case-sizes/ATX
```

**Expected Response (200 OK):**
```json
{
  "code": 1000,
  "message": "Case Size deleted successfully"
}
```

---

## 2. PC CASE Operations (Updated)

### 2.1. Create PC Case with CaseSize
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
  "description": "Compact mid-tower ATX case with tempered glass"
}
```

**Expected Response (200 OK):**
```json
{
  "code": 1000,
  "message": "Success",
  "result": {
    "id": "generated-uuid-here",
    "name": "NZXT H510",
    "size": {
      "id": "ATX",
      "name": "ATX Full Tower"
    },
    "maxVgaLengthMm": 381,
    "maxCoolerHeightMm": 165,
    "maxRadiatorSize": 280,
    "drive35Slot": 2,
    "drive25Slot": 3,
    "description": "Compact mid-tower ATX case with tempered glass"
  }
}
```

### 2.2. Create PC Case - Corsair 4000D
```http
POST /cases
Content-Type: application/json

{
  "name": "Corsair 4000D Airflow",
  "sizeId": "ATX",
  "maxVgaLengthMm": 360,
  "maxCoolerHeightMm": 170,
  "maxRadiatorSize": 360,
  "drive35Slot": 2,
  "drive25Slot": 2,
  "description": "High airflow ATX case"
}
```

### 2.3. Create PC Case - Mini ITX
```http
POST /cases
Content-Type: application/json

{
  "name": "NZXT H210",
  "sizeId": "ITX",
  "maxVgaLengthMm": 325,
  "maxCoolerHeightMm": 165,
  "maxRadiatorSize": 240,
  "drive35Slot": 1,
  "drive25Slot": 2,
  "description": "Compact Mini-ITX case"
}
```

### 2.4. Get All PC Cases
```http
GET /cases
```

**Expected Response:**
```json
{
  "code": 1000,
  "message": "Success",
  "result": [
    {
      "id": "uuid-1",
      "name": "NZXT H510",
      "size": {
        "id": "ATX",
        "name": "ATX Full Tower"
      },
      "maxVgaLengthMm": 381,
      "maxCoolerHeightMm": 165,
      "maxRadiatorSize": 280,
      "drive35Slot": 2,
      "drive25Slot": 3,
      "description": "Compact mid-tower ATX case with tempered glass"
    }
  ]
}
```

### 2.5. Update PC Case - Change Size
```http
PUT /cases/{case-id}
Content-Type: application/json

{
  "sizeId": "mATX",
  "maxVgaLengthMm": 350
}
```

**Expected Response:**
```json
{
  "code": 1000,
  "message": "Success",
  "result": {
    "id": "case-id",
    "name": "NZXT H510",
    "size": {
      "id": "mATX",
      "name": "Micro ATX"
    },
    "maxVgaLengthMm": 350,
    "maxCoolerHeightMm": 165,
    "maxRadiatorSize": 280,
    "drive35Slot": 2,
    "drive25Slot": 3,
    "description": "Compact mid-tower ATX case with tempered glass"
  }
}
```

---

## 3. Error Scenarios

### 3.1. Create Case Size - Missing ID
```http
POST /case-sizes
Content-Type: application/json

{
  "name": "ATX Full Tower"
}
```

**Expected Response (400 BAD REQUEST):**
```json
{
  "code": 3141,
  "message": "Case Size ID is required"
}
```

### 3.2. Create Case Size - Missing Name
```http
POST /case-sizes
Content-Type: application/json

{
  "id": "ATX"
}
```

**Expected Response (400 BAD REQUEST):**
```json
{
  "code": 3142,
  "message": "Case Size name is required"
}
```

### 3.3. Create Case Size - Duplicate ID
```http
POST /case-sizes
Content-Type: application/json

{
  "id": "ATX",
  "name": "ATX Duplicate"
}
```

**Expected Response (400 BAD REQUEST):**
```json
{
  "code": 3144,
  "message": "Case Size already exists"
}
```

### 3.4. Get Case Size - Not Found
```http
GET /case-sizes/INVALID_ID
```

**Expected Response (404 NOT FOUND):**
```json
{
  "code": 3143,
  "message": "Case Size not found"
}
```

### 3.5. Create PC Case - Invalid Size ID
```http
POST /cases
Content-Type: application/json

{
  "name": "Test Case",
  "sizeId": "INVALID_SIZE",
  "maxVgaLengthMm": 350,
  "maxCoolerHeightMm": 160,
  "maxRadiatorSize": 240,
  "drive35Slot": 2,
  "drive25Slot": 2
}
```

**Expected Response (404 NOT FOUND):**
```json
{
  "code": 3143,
  "message": "Case Size not found"
}
```

### 3.6. Create PC Case - Missing Required Fields
```http
POST /cases
Content-Type: application/json

{
  "name": "Test Case"
}
```

**Expected Response (400 BAD REQUEST):**
```json
{
  "code": 3002,
  "message": "Case Size is required"
}
```

---

## 4. Complete Test Flow

### Step 1: Setup - Create Case Sizes
```bash
# Create ATX
curl -X POST http://localhost:8080/case-sizes \
  -H "Content-Type: application/json" \
  -d '{"id":"ATX","name":"ATX Full Tower"}'

# Create mATX
curl -X POST http://localhost:8080/case-sizes \
  -H "Content-Type: application/json" \
  -d '{"id":"mATX","name":"Micro ATX"}'

# Create ITX
curl -X POST http://localhost:8080/case-sizes \
  -H "Content-Type: application/json" \
  -d '{"id":"ITX","name":"Mini ITX"}'
```

### Step 2: Verify Case Sizes Created
```bash
curl -X GET http://localhost:8080/case-sizes
```

### Step 3: Create PC Cases
```bash
# ATX Case
curl -X POST http://localhost:8080/cases \
  -H "Content-Type: application/json" \
  -d '{
    "name":"NZXT H510",
    "sizeId":"ATX",
    "maxVgaLengthMm":381,
    "maxCoolerHeightMm":165,
    "maxRadiatorSize":280,
    "drive35Slot":2,
    "drive25Slot":3,
    "description":"Compact ATX case"
  }'

# ITX Case
curl -X POST http://localhost:8080/cases \
  -H "Content-Type: application/json" \
  -d '{
    "name":"NZXT H210",
    "sizeId":"ITX",
    "maxVgaLengthMm":325,
    "maxCoolerHeightMm":165,
    "maxRadiatorSize":240,
    "drive35Slot":1,
    "drive25Slot":2,
    "description":"Mini ITX case"
  }'
```

### Step 4: Verify PC Cases Created with Size Info
```bash
curl -X GET http://localhost:8080/cases
```

---

## 5. Notes

- Tất cả error codes đã được định nghĩa trong `ErrorCode.java`
- Response luôn trả về nested object `CaseSizeResponse` trong `CaseResponse.size`
- Khi update PC Case, chỉ cần cung cấp `sizeId` nếu muốn thay đổi size
- Không thể xóa `CaseSize` nếu còn `PcCase` đang sử dụng (foreign key constraint)

