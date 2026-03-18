# FORM FACTOR API - POSTMAN EXAMPLES

## Base URL
```
http://localhost:8080
```

---

## 1. Form Factor APIs

### 1.1. Create Form Factor
**Method:** `POST`  
**URL:** `/form-factors`  
**Headers:**
```json
{
  "Content-Type": "application/json",
  "Authorization": "Bearer YOUR_JWT_TOKEN"
}
```

**Request Body:**
```json
{
  "id": "FF_2_5",
  "name": "2.5\""
}
```

**Response:**
```json
{
  "code": 1000,
  "result": {
    "id": "FF_2_5",
    "name": "2.5\""
  }
}
```

---

### 1.2. Get All Form Factors
**Method:** `GET`  
**URL:** `/form-factors`  
**Headers:**
```json
{
  "Authorization": "Bearer YOUR_JWT_TOKEN"
}
```

**Response:**
```json
{
  "code": 1000,
  "result": [
    {
      "id": "FF_2_5",
      "name": "2.5\""
    },
    {
      "id": "FF_3_5",
      "name": "3.5\""
    },
    {
      "id": "M2_2280",
      "name": "M.2 2280"
    }
  ]
}
```

---

### 1.3. Get Form Factor by ID
**Method:** `GET`  
**URL:** `/form-factors/FF_2_5`  
**Headers:**
```json
{
  "Authorization": "Bearer YOUR_JWT_TOKEN"
}
```

**Response:**
```json
{
  "code": 1000,
  "result": {
    "id": "FF_2_5",
    "name": "2.5\""
  }
}
```

---

### 1.4. Update Form Factor
**Method:** `PUT`  
**URL:** `/form-factors/FF_2_5`  
**Headers:**
```json
{
  "Content-Type": "application/json",
  "Authorization": "Bearer YOUR_JWT_TOKEN"
}
```

**Request Body:**
```json
{
  "id": "FF_2_5",
  "name": "2.5 inch"
}
```

**Response:**
```json
{
  "code": 1000,
  "result": {
    "id": "FF_2_5",
    "name": "2.5 inch"
  }
}
```

---

### 1.5. Delete Form Factor
**Method:** `DELETE`  
**URL:** `/form-factors/FF_2_5`  
**Headers:**
```json
{
  "Authorization": "Bearer YOUR_JWT_TOKEN"
}
```

**Response:**
```json
{
  "code": 1000,
  "message": "Form Factor deleted successfully"
}
```

---

## 2. Updated HDD APIs (with FormFactor)

### 2.1. Create HDD (Updated)
**Method:** `POST`  
**URL:** `/hdds`  
**Headers:**
```json
{
  "Content-Type": "application/json",
  "Authorization": "Bearer YOUR_JWT_TOKEN"
}
```

**Request Body:**
```json
{
  "name": "Seagate Barracuda 2TB",
  "formFactorId": "FF_3_5",
  "interfaceTypeId": "SATA_3",
  "capacity": 2000,
  "tdp": 6,
  "description": "High-performance 7200 RPM hard drive"
}
```

**Response:**
```json
{
  "code": 1000,
  "result": {
    "id": "generated-uuid",
    "name": "Seagate Barracuda 2TB",
    "formFactor": {
      "id": "FF_3_5",
      "name": "3.5\""
    },
    "interfaceType": {
      "id": "SATA_3",
      "name": "SATA III"
    },
    "capacity": 2000,
    "tdp": 6,
    "description": "High-performance 7200 RPM hard drive"
  }
}
```

---

### 2.2. Update HDD (Updated)
**Method:** `PUT`  
**URL:** `/hdds/{id}`  
**Headers:**
```json
{
  "Content-Type": "application/json",
  "Authorization": "Bearer YOUR_JWT_TOKEN"
}
```

**Request Body:**
```json
{
  "name": "Seagate Barracuda 2TB Updated",
  "formFactorId": "FF_2_5",
  "interfaceTypeId": "SATA_3",
  "capacity": 2000,
  "tdp": 5,
  "description": "Updated description"
}
```

---

## 3. Updated SSD APIs (with FormFactor)

### 3.1. Create SSD (Updated)
**Method:** `POST`  
**URL:** `/ssds`  
**Headers:**
```json
{
  "Content-Type": "application/json",
  "Authorization": "Bearer YOUR_JWT_TOKEN"
}
```

**Request Body:**
```json
{
  "name": "Samsung 980 PRO 1TB",
  "ssdTypeId": "NVME",
  "formFactorId": "M2_2280",
  "interfaceTypeId": "PCIE_4",
  "capacity": 1000,
  "tdp": 7,
  "description": "High-speed NVMe SSD with PCIe 4.0"
}
```

**Response:**
```json
{
  "code": 1000,
  "result": {
    "id": "generated-uuid",
    "name": "Samsung 980 PRO 1TB",
    "ssdType": {
      "id": "NVME",
      "name": "NVMe"
    },
    "formFactor": {
      "id": "M2_2280",
      "name": "M.2 2280"
    },
    "interfaceType": {
      "id": "PCIE_4",
      "name": "PCIe 4.0 x4"
    },
    "capacity": 1000,
    "tdp": 7,
    "description": "High-speed NVMe SSD with PCIe 4.0"
  }
}
```

---

### 3.2. Create SSD SATA Example
**Method:** `POST`  
**URL:** `/ssds`  
**Headers:**
```json
{
  "Content-Type": "application/json",
  "Authorization": "Bearer YOUR_JWT_TOKEN"
}
```

**Request Body:**
```json
{
  "name": "Samsung 870 EVO 500GB",
  "ssdTypeId": "SATA",
  "formFactorId": "FF_2_5",
  "interfaceTypeId": "SATA_3",
  "capacity": 500,
  "tdp": 3,
  "description": "Reliable SATA SSD"
}
```

**Response:**
```json
{
  "code": 1000,
  "result": {
    "id": "generated-uuid",
    "name": "Samsung 870 EVO 500GB",
    "ssdType": {
      "id": "SATA",
      "name": "SATA"
    },
    "formFactor": {
      "id": "FF_2_5",
      "name": "2.5\""
    },
    "interfaceType": {
      "id": "SATA_3",
      "name": "SATA III"
    },
    "capacity": 500,
    "tdp": 3,
    "description": "Reliable SATA SSD"
  }
}
```

---

### 3.3. Update SSD (Updated)
**Method:** `PUT`  
**URL:** `/ssds/{id}`  
**Headers:**
```json
{
  "Content-Type": "application/json",
  "Authorization": "Bearer YOUR_JWT_TOKEN"
}
```

**Request Body:**
```json
{
  "name": "Samsung 980 PRO 1TB Updated",
  "ssdTypeId": "NVME",
  "formFactorId": "M2_2280",
  "interfaceTypeId": "PCIE_5",
  "capacity": 1000,
  "tdp": 8,
  "description": "Upgraded to PCIe 5.0"
}
```

---

## 4. Complete Form Factor Data Setup

### 4.1. Setup All Form Factors
Execute these requests in order:

**Request 1: Create FF_2_5**
```json
POST /form-factors
{
  "id": "FF_2_5",
  "name": "2.5\""
}
```

**Request 2: Create FF_3_5**
```json
POST /form-factors
{
  "id": "FF_3_5",
  "name": "3.5\""
}
```

**Request 3: Create M2_2280**
```json
POST /form-factors
{
  "id": "M2_2280",
  "name": "M.2 2280"
}
```

**Request 4: Create M2_2260**
```json
POST /form-factors
{
  "id": "M2_2260",
  "name": "M.2 2260"
}
```

**Request 5: Create M2_2242**
```json
POST /form-factors
{
  "id": "M2_2242",
  "name": "M.2 2242"
}
```

**Request 6: Create M2_22110**
```json
POST /form-factors
{
  "id": "M2_22110",
  "name": "M.2 22110"
}
```

---

## 5. Common Use Cases

### 5.1. Create Complete PC Build Storage Setup

**Step 1: Create a 3.5" HDD for mass storage**
```json
POST /hdds
{
  "name": "WD Blue 4TB",
  "formFactorId": "FF_3_5",
  "interfaceTypeId": "SATA_3",
  "capacity": 4000,
  "tdp": 5,
  "description": "Mass storage HDD"
}
```

**Step 2: Create a 2.5" SATA SSD for OS**
```json
POST /ssds
{
  "name": "Crucial MX500 1TB",
  "ssdTypeId": "SATA",
  "formFactorId": "FF_2_5",
  "interfaceTypeId": "SATA_3",
  "capacity": 1000,
  "tdp": 4,
  "description": "OS drive"
}
```

**Step 3: Create an M.2 NVMe SSD for fast storage**
```json
POST /ssds
{
  "name": "WD Black SN850X 2TB",
  "ssdTypeId": "NVME",
  "formFactorId": "M2_2280",
  "interfaceTypeId": "PCIE_4",
  "capacity": 2000,
  "tdp": 8,
  "description": "High-speed gaming storage"
}
```

---

## 6. Error Responses

### 6.1. Form Factor Not Found
```json
{
  "code": 3133,
  "message": "Form Factor not found"
}
```

### 6.2. Form Factor Already Exists
```json
{
  "code": 1002,
  "message": "Form Factor already exists with id: FF_2_5"
}
```

### 6.3. Invalid Form Factor ID
```json
{
  "code": 3131,
  "message": "Form Factor ID is required"
}
```

---

## Notes

1. **Form Factor IDs** are predefined and should match the database values
2. **Common Form Factors:**
   - `FF_2_5`: 2.5" (for SSDs and laptop HDDs)
   - `FF_3_5`: 3.5" (for desktop HDDs)
   - `M2_2280`: M.2 2280 (most common M.2 size for SSDs)
   - `M2_2260`: M.2 2260 (shorter M.2 size)
   - `M2_2242`: M.2 2242 (compact M.2 size)
   - `M2_22110`: M.2 22110 (longer M.2 size)

3. **Migration Steps:**
   - First, create all Form Factor entries
   - Then update existing HDD/SSD records to use formFactorId
   - Finally, create new HDD/SSD records with formFactorId

4. **Authorization:** All endpoints require a valid JWT token in the Authorization header

