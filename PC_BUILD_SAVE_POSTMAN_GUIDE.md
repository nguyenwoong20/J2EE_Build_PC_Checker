# PC Build Save API - Postman Collection

## 🔐 Authentication Required

All PC Build endpoints require JWT authentication. Include the token in the Authorization header:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

## 📝 API Endpoints

### 1️⃣ Save PC Build

**Endpoint:** `POST /identity/builds`

**Headers:**
```
Authorization: Bearer {your-jwt-token}
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "Gaming Build 2026",
  "description": "Intel i9 + RTX 4090 Gaming Setup",
  "parts": {
    "cpu": "550e8400-e29b-41d4-a716-446655440000",
    "mainboard": "550e8400-e29b-41d4-a716-446655440001",
    "ram": "550e8400-e29b-41d4-a716-446655440002",
    "gpu": "550e8400-e29b-41d4-a716-446655440003",
    "psu": "550e8400-e29b-41d4-a716-446655440004",
    "case": "550e8400-e29b-41d4-a716-446655440005",
    "cooler": "550e8400-e29b-41d4-a716-446655440006",
    "ssd": "550e8400-e29b-41d4-a716-446655440007"
  }
}
```

**Success Response (200):**
```json
{
  "code": 1000,
  "message": "Build saved successfully",
  "result": {
    "id": "7c9e8b5a-1234-5678-90ab-cdef12345678",
    "name": "Gaming Build 2026",
    "description": "Intel i9 + RTX 4090 Gaming Setup",
    "totalTdp": null,
    "createdAt": "2026-03-09T20:45:00",
    "userId": "user-123-uuid",
    "parts": {
      "CPU": "550e8400-e29b-41d4-a716-446655440000",
      "MAINBOARD": "550e8400-e29b-41d4-a716-446655440001",
      "RAM": "550e8400-e29b-41d4-a716-446655440002",
      "GPU": "550e8400-e29b-41d4-a716-446655440003",
      "PSU": "550e8400-e29b-41d4-a716-446655440004",
      "CASE": "550e8400-e29b-41d4-a716-446655440005",
      "COOLER": "550e8400-e29b-41d4-a716-446655440006",
      "SSD": "550e8400-e29b-41d4-a716-446655440007"
    }
  }
}
```

**Error Response - Missing Name (400):**
```json
{
  "code": 5001,
  "message": "Build name is required"
}
```

**Error Response - Unauthenticated (401):**
```json
{
  "code": 1007,
  "message": "Unauthenticated"
}
```

---

### 2️⃣ Get All My Builds

**Endpoint:** `GET /identity/builds`

**Headers:**
```
Authorization: Bearer {your-jwt-token}
```

**Success Response (200):**
```json
{
  "code": 1000,
  "message": null,
  "result": [
    {
      "id": "7c9e8b5a-1234-5678-90ab-cdef12345678",
      "name": "Gaming Build 2026",
      "description": "Intel i9 + RTX 4090 Gaming Setup",
      "totalTdp": null,
      "createdAt": "2026-03-09T20:45:00",
      "userId": "user-123-uuid",
      "parts": {
        "CPU": "550e8400-e29b-41d4-a716-446655440000",
        "MAINBOARD": "550e8400-e29b-41d4-a716-446655440001",
        "RAM": "550e8400-e29b-41d4-a716-446655440002"
      }
    },
    {
      "id": "8d0f9c6b-2345-6789-01bc-def123456789",
      "name": "Office Build",
      "description": "Budget Office PC",
      "totalTdp": null,
      "createdAt": "2026-03-09T21:00:00",
      "userId": "user-123-uuid",
      "parts": {
        "CPU": "650e8400-e29b-41d4-a716-446655440000",
        "MAINBOARD": "650e8400-e29b-41d4-a716-446655440001"
      }
    }
  ]
}
```

---

### 3️⃣ Get Build by ID

**Endpoint:** `GET /identity/builds/{buildId}`

**Headers:**
```
Authorization: Bearer {your-jwt-token}
```

**Example:** `GET /identity/builds/7c9e8b5a-1234-5678-90ab-cdef12345678`

**Success Response (200):**
```json
{
  "code": 1000,
  "message": null,
  "result": {
    "id": "7c9e8b5a-1234-5678-90ab-cdef12345678",
    "name": "Gaming Build 2026",
    "description": "Intel i9 + RTX 4090 Gaming Setup",
    "totalTdp": null,
    "createdAt": "2026-03-09T20:45:00",
    "userId": "user-123-uuid",
    "parts": {
      "CPU": "550e8400-e29b-41d4-a716-446655440000",
      "MAINBOARD": "550e8400-e29b-41d4-a716-446655440001",
      "RAM": "550e8400-e29b-41d4-a716-446655440002"
    }
  }
}
```

**Error Response - Not Found (404):**
```json
{
  "code": 5002,
  "message": "Build not found"
}
```

**Error Response - Unauthorized Access (403):**
```json
{
  "code": 5003,
  "message": "You don't have permission to access this build"
}
```

---

### 4️⃣ Delete Build

**Endpoint:** `DELETE /identity/builds/{buildId}`

**Headers:**
```
Authorization: Bearer {your-jwt-token}
```

**Example:** `DELETE /identity/builds/7c9e8b5a-1234-5678-90ab-cdef12345678`

**Success Response (200):**
```json
{
  "code": 1000,
  "message": "Build deleted successfully",
  "result": null
}
```

**Error Response - Not Found (404):**
```json
{
  "code": 5002,
  "message": "Build not found"
}
```

**Error Response - Unauthorized Access (403):**
```json
{
  "code": 5003,
  "message": "You don't have permission to access this build"
}
```

---

## 🧪 Complete Test Flow

### Prerequisites
Get valid part UUIDs by calling the GET endpoints for components:
- `GET /identity/cpus` - Get CPU list
- `GET /identity/mainboards` - Get Mainboard list
- `GET /identity/rams` - Get RAM list
- `GET /identity/vgas` - Get GPU list
- `GET /identity/psus` - Get PSU list
- `GET /identity/cases` - Get Case list
- `GET /identity/coolers` - Get Cooler list
- `GET /identity/ssds` - Get SSD list
- `GET /identity/hdds` - Get HDD list

### Test Scenario

#### 1. Login
```
POST http://localhost:8080/identity/auth/token

{
  "username": "admin",
  "password": "admin"
}
```
→ Copy the `token` from response

#### 2. Save a Build
```
POST http://localhost:8080/identity/builds
Authorization: Bearer {token}

{
  "name": "My First Build",
  "description": "Testing the save feature",
  "parts": {
    "cpu": "{real-cpu-uuid}",
    "mainboard": "{real-mainboard-uuid}"
  }
}
```
→ Copy the build `id` from response

#### 3. Get All My Builds
```
GET http://localhost:8080/identity/builds
Authorization: Bearer {token}
```
→ Should see the build you just created

#### 4. Get Specific Build
```
GET http://localhost:8080/identity/builds/{build-id}
Authorization: Bearer {token}
```
→ Should return the specific build

#### 5. Delete the Build
```
DELETE http://localhost:8080/identity/builds/{build-id}
Authorization: Bearer {token}
```
→ Build should be deleted

#### 6. Verify Deletion
```
GET http://localhost:8080/identity/builds
Authorization: Bearer {token}
```
→ Build should no longer appear in the list

---

## 🔍 Validation Tests

### Test 1: Missing Name (Should Fail)
```json
POST /identity/builds

{
  "description": "No name provided",
  "parts": {}
}
```
Expected: `400 Bad Request` with error code `5001`

### Test 2: Empty Name (Should Fail)
```json
POST /identity/builds

{
  "name": "",
  "description": "Empty name",
  "parts": {}
}
```
Expected: `400 Bad Request` with error code `5001`

### Test 3: No Authentication (Should Fail)
```
POST /identity/builds
(No Authorization header)

{
  "name": "Test Build",
  "parts": {}
}
```
Expected: `401 Unauthorized` with error code `1007`

### Test 4: Access Another User's Build (Should Fail)
1. Login as User A and create a build
2. Login as User B
3. Try to access User A's build
Expected: `403 Forbidden` with error code `5003`

### Test 5: Save Minimal Build (Should Succeed)
```json
POST /identity/builds

{
  "name": "Minimal Build"
}
```
Expected: `200 OK` - Build saved with no parts

### Test 6: Save Build with Only CPU (Should Succeed)
```json
POST /identity/builds

{
  "name": "CPU Only Build",
  "description": "Just testing CPU",
  "parts": {
    "cpu": "{valid-cpu-uuid}"
  }
}
```
Expected: `200 OK` - Build saved with one part

---

## 📋 Part Type Keys

The following keys are recognized for the `parts` map:

| Key | Description | Example UUID |
|-----|-------------|--------------|
| `cpu` | Processor | `550e8400-e29b-41d4-a716-446655440000` |
| `mainboard` | Motherboard | `550e8400-e29b-41d4-a716-446655440001` |
| `ram` | Memory | `550e8400-e29b-41d4-a716-446655440002` |
| `gpu` | Graphics Card | `550e8400-e29b-41d4-a716-446655440003` |
| `psu` | Power Supply | `550e8400-e29b-41d4-a716-446655440004` |
| `case` | PC Case | `550e8400-e29b-41d4-a716-446655440005` |
| `cooler` | CPU Cooler | `550e8400-e29b-41d4-a716-446655440006` |
| `ssd` | Solid State Drive | `550e8400-e29b-41d4-a716-446655440007` |
| `hdd` | Hard Disk Drive | `550e8400-e29b-41d4-a716-446655440008` |

**Note:** Part type keys are converted to UPPERCASE when stored in the database.

---

## 🔗 Integration with Existing Features

### Compatibility Check Integration
After saving a build, you can check its compatibility:

1. Save the build with `POST /identity/builds`
2. Get the build details with `GET /identity/builds/{id}`
3. Use the part UUIDs from the build to check compatibility:

```json
POST /identity/builds/check-compatibility

{
  "cpuId": "{cpu-uuid-from-build}",
  "mainboardId": "{mainboard-uuid-from-build}",
  "ramId": "{ram-uuid-from-build}",
  "vgaId": "{gpu-uuid-from-build}",
  "psuId": "{psu-uuid-from-build}",
  "caseId": "{case-uuid-from-build}",
  "coolerId": "{cooler-uuid-from-build}"
}
```

---

## 🎯 Implementation Notes

### Part Type Conversion
- Request uses lowercase keys: `"cpu"`, `"mainboard"`, `"ram"`
- Storage uses uppercase: `"CPU"`, `"MAINBOARD"`, `"RAM"`
- Response returns uppercase for consistency

### UUID Generation
- All IDs are auto-generated UUIDs
- No need to provide IDs in the request

### Timestamps
- `createdAt` is automatically set when saving

### Cascade Operations
- Deleting a build automatically deletes all associated parts
- No orphaned parts in the database

### Authorization
- Users can only access their own builds
- Attempting to access another user's build returns 403 Forbidden

---

## 📊 Database Tables

### pc_build
```
+-------------+-------------+---------+
| Column      | Type        | Null    |
+-------------+-------------+---------+
| id          | VARCHAR(36) | NO (PK) |
| name        | VARCHAR(255)| NO      |
| description | TEXT        | YES     |
| total_tdp   | INT         | YES     |
| created_at  | DATETIME    | YES     |
| user_id     | VARCHAR(36) | NO (FK) |
+-------------+-------------+---------+
```

### pc_build_part
```
+-------------+-------------+---------+
| Column      | Type        | Null    |
+-------------+-------------+---------+
| id          | VARCHAR(36) | NO (PK) |
| part_type   | VARCHAR(50) | NO      |
| part_id     | VARCHAR(36) | NO      |
| quantity    | INT         | NO      |
| build_id    | VARCHAR(36) | NO (FK) |
+-------------+-------------+---------+
```

---

## ✅ Testing Checklist

- [ ] Can save a build with valid name
- [ ] Can save a build with all part types
- [ ] Can save a build with only some parts
- [ ] Can save a build with no parts
- [ ] Cannot save without authentication
- [ ] Cannot save with blank name
- [ ] Can retrieve all my builds
- [ ] Can retrieve specific build by ID
- [ ] Cannot retrieve another user's build
- [ ] Can delete my own build
- [ ] Cannot delete another user's build
- [ ] Parts are deleted when build is deleted

---

## 🚀 Quick Start

1. **Start the application:**
   ```bash
   ./mvnw.cmd spring-boot:run
   ```

2. **Access Swagger UI:**
   ```
   http://localhost:8080/identity/swagger-ui.html
   ```

3. **Authenticate:**
   - Click "Authorize" button
   - Login to get JWT token
   - Enter token in the dialog

4. **Test the endpoints:**
   - Expand "build-controller"
   - Try the "POST /builds" endpoint
   - Try the "GET /builds" endpoint

---

## 💡 Tips

- Use the existing check-compatibility endpoint to validate builds before saving
- Store real component UUIDs from your database
- Build name is required, description is optional
- All parts in the map are optional
- Quantity is automatically set to 1 for all parts
- Part type keys are case-insensitive in request (converted to uppercase)

