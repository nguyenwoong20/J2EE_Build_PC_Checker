# PC Build Save Feature - Implementation Summary

## ✅ Feature Completed

The PC Build save feature has been successfully implemented following clean architecture principles.

---

## 📁 Files Created

### Entities
1. **`PcBuild.java`** - Main entity for storing PC build configurations
   - `id` (UUID - auto-generated)
   - `name` (String - required)
   - `description` (String - optional)
   - `totalTdp` (Integer - optional)
   - `createdAt` (LocalDateTime - auto-generated)
   - `user` (ManyToOne relationship)
   - `parts` (OneToMany relationship with cascade and orphan removal)

2. **`PcBuildPart.java`** - Entity for storing individual parts in a build
   - `id` (UUID - auto-generated)
   - `partType` (String - CPU, MAINBOARD, RAM, GPU, PSU, CASE, COOLER, SSD, HDD)
   - `partId` (String - UUID reference to the actual part)
   - `quantity` (Integer - default: 1)
   - `build` (ManyToOne relationship)

### Repositories
3. **`PcBuildRepository.java`**
   - Extends `JpaRepository<PcBuild, String>`
   - Custom method: `findByUserId(String userId)`

4. **`PcBuildPartRepository.java`**
   - Extends `JpaRepository<PcBuildPart, String>`

### DTOs
5. **`SaveBuildRequest.java`** (Request DTO)
   ```json
   {
     "name": "Gaming Build",
     "description": "My first PC",
     "parts": {
       "cpu": "uuid-of-cpu",
       "mainboard": "uuid-of-mainboard",
       "ram": "uuid-of-ram",
       "gpu": "uuid-of-gpu",
       "psu": "uuid-of-psu",
       "case": "uuid-of-case",
       "cooler": "uuid-of-cooler",
       "ssd": "uuid-of-ssd",
       "hdd": "uuid-of-hdd"
     }
   }
   ```
   - Validation: `@NotBlank` on name field

6. **`PcBuildResponse.java`** (Response DTO)
   ```json
   {
     "id": "uuid",
     "name": "Gaming Build",
     "description": "My first PC",
     "totalTdp": null,
     "createdAt": "2026-03-09T20:45:00",
     "userId": "user-uuid",
     "parts": {
       "CPU": "uuid-of-cpu",
       "MAINBOARD": "uuid-of-mainboard",
       "RAM": "uuid-of-ram"
     }
   }
   ```

### Service Layer
7. **`BuildService.java`**
   - `saveBuild(SaveBuildRequest)` - Save a new PC build with authenticated user
   - `getBuildById(String)` - Get a specific build by ID (with authorization check)
   - `getMyBuilds()` - Get all builds for the authenticated user
   - `deleteBuild(String)` - Delete a build (with authorization check)

### Controller Layer
8. **`BuildController.java`** (Updated)
   - Added BuildService dependency injection
   - Added 4 new endpoints (see below)

### Exception Handling
9. **`ErrorCode.java`** (Updated)
   - `BUILD_NAME_REQUIRED` (5001)
   - `BUILD_NOT_FOUND` (5002)
   - `BUILD_UNAUTHORIZED_ACCESS` (5003)

---

## 🚀 API Endpoints

### 1. Save a PC Build
**POST** `/api/builds` (Requires Authentication)

**Request:**
```json
{
  "name": "Gaming Build",
  "description": "High-end gaming setup",
  "parts": {
    "cpu": "550e8400-e29b-41d4-a716-446655440000",
    "mainboard": "550e8400-e29b-41d4-a716-446655440001",
    "ram": "550e8400-e29b-41d4-a716-446655440002",
    "gpu": "550e8400-e29b-41d4-a716-446655440003",
    "psu": "550e8400-e29b-41d4-a716-446655440004",
    "case": "550e8400-e29b-41d4-a716-446655440005"
  }
}
```

**Response:**
```json
{
  "code": 1000,
  "message": "Build saved successfully",
  "result": {
    "id": "auto-generated-uuid",
    "name": "Gaming Build",
    "description": "High-end gaming setup",
    "totalTdp": null,
    "createdAt": "2026-03-09T20:45:00",
    "userId": "user-uuid",
    "parts": {
      "CPU": "550e8400-e29b-41d4-a716-446655440000",
      "MAINBOARD": "550e8400-e29b-41d4-a716-446655440001",
      "RAM": "550e8400-e29b-41d4-a716-446655440002",
      "GPU": "550e8400-e29b-41d4-a716-446655440003",
      "PSU": "550e8400-e29b-41d4-a716-446655440004",
      "CASE": "550e8400-e29b-41d4-a716-446655440005"
    }
  }
}
```

### 2. Get My Builds
**GET** `/api/builds` (Requires Authentication)

**Response:**
```json
{
  "code": 1000,
  "message": "Success",
  "result": [
    {
      "id": "uuid-1",
      "name": "Gaming Build",
      "description": "High-end gaming setup",
      "totalTdp": null,
      "createdAt": "2026-03-09T20:45:00",
      "userId": "user-uuid",
      "parts": { ... }
    },
    {
      "id": "uuid-2",
      "name": "Office Build",
      "description": "Budget office PC",
      "totalTdp": null,
      "createdAt": "2026-03-09T21:00:00",
      "userId": "user-uuid",
      "parts": { ... }
    }
  ]
}
```

### 3. Get Build by ID
**GET** `/api/builds/{id}` (Requires Authentication)

**Response:**
```json
{
  "code": 1000,
  "message": "Success",
  "result": {
    "id": "uuid",
    "name": "Gaming Build",
    "description": "High-end gaming setup",
    "totalTdp": null,
    "createdAt": "2026-03-09T20:45:00",
    "userId": "user-uuid",
    "parts": { ... }
  }
}
```

**Error Response (Unauthorized):**
```json
{
  "code": 5003,
  "message": "You don't have permission to access this build"
}
```

### 4. Delete Build
**DELETE** `/api/builds/{id}` (Requires Authentication)

**Response:**
```json
{
  "code": 1000,
  "message": "Build deleted successfully"
}
```

---

## 🔐 Security

- All endpoints require JWT authentication
- Users can only access their own builds
- Authorization checks are implemented in the service layer
- Authenticated user is retrieved from `SecurityContextHolder`

---

## 🏗️ Architecture

```
Controller Layer (BuildController)
    ↓
Service Layer (BuildService)
    ↓
Repository Layer (PcBuildRepository, PcBuildPartRepository)
    ↓
Database (MySQL)
```

### Transaction Management
- `saveBuild()` is marked as `@Transactional` to ensure atomic operations
- `deleteBuild()` is marked as `@Transactional` with cascade deletion of parts

---

## 📊 Database Schema

### Table: `pc_build`
```sql
CREATE TABLE pc_build (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    total_tdp INT,
    created_at DATETIME,
    user_id VARCHAR(36) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(id)
);
```

### Table: `pc_build_part`
```sql
CREATE TABLE pc_build_part (
    id VARCHAR(36) PRIMARY KEY,
    part_type VARCHAR(50) NOT NULL,
    part_id VARCHAR(36) NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    build_id VARCHAR(36) NOT NULL,
    FOREIGN KEY (build_id) REFERENCES pc_build(id) ON DELETE CASCADE
);
```

---

## ✅ Features Implemented

- ✅ Create PC Build with multiple parts
- ✅ Auto-generate UUID for entities
- ✅ Auto-set created timestamp
- ✅ Associate build with authenticated user
- ✅ Store parts with type and quantity
- ✅ Retrieve user's builds
- ✅ Retrieve specific build by ID
- ✅ Delete build with cascade deletion
- ✅ Authorization checks (users can only access their own builds)
- ✅ Clean architecture (Controller → Service → Repository → Entity)
- ✅ Proper validation (@NotBlank on build name)
- ✅ Constructor injection with Lombok
- ✅ Proper error handling with custom error codes
- ✅ Transaction management
- ✅ Bidirectional relationship with cascade operations

---

## 🧪 Testing with Swagger

1. Go to: `http://localhost:8080/identity/swagger-ui.html`
2. Authenticate with JWT token (click "Authorize" button)
3. Test the endpoints under "build-controller"

---

## 🧪 Testing with Postman

### Step 1: Login to get JWT token
```
POST http://localhost:8080/identity/auth/token
Content-Type: application/json

{
  "username": "your-username",
  "password": "your-password"
}
```

### Step 2: Save a build
```
POST http://localhost:8080/identity/builds
Authorization: Bearer {your-jwt-token}
Content-Type: application/json

{
  "name": "My Gaming PC",
  "description": "High-end gaming build",
  "parts": {
    "cpu": "valid-cpu-uuid",
    "mainboard": "valid-mainboard-uuid",
    "ram": "valid-ram-uuid"
  }
}
```

### Step 3: Get all my builds
```
GET http://localhost:8080/identity/builds
Authorization: Bearer {your-jwt-token}
```

### Step 4: Get specific build
```
GET http://localhost:8080/identity/builds/{build-id}
Authorization: Bearer {your-jwt-token}
```

### Step 5: Delete a build
```
DELETE http://localhost:8080/identity/builds/{build-id}
Authorization: Bearer {your-jwt-token}
```

---

## ⚙️ Technology Stack Used

- **Spring Boot 3** - Framework
- **Java 17** - Programming language
- **Spring Security with JWT** - Authentication & Authorization
- **Spring Data JPA** - Data access layer
- **Hibernate** - ORM
- **MySQL** - Database
- **Lombok** - Boilerplate code reduction
- **Maven** - Build tool
- **Swagger/OpenAPI** - API documentation

---

## 🎯 Key Design Decisions

1. **UUID as Primary Key**: Using String UUID for better scalability and distributed systems
2. **Bidirectional Relationship**: PcBuild ↔ PcBuildPart with proper cascade operations
3. **Part Type as String**: Flexible approach storing part type as uppercase string (CPU, MAINBOARD, etc.)
4. **Map Structure in DTO**: Simple key-value pairs for parts in request/response
5. **Authorization in Service**: Security checks at service layer, not just controller
6. **Cascade Delete**: When a build is deleted, all its parts are automatically deleted
7. **Lazy Loading**: User relationship uses LAZY fetch to optimize performance

---

## 🔄 Future Enhancements (Optional)

- Calculate and store `totalTdp` automatically when saving
- Add update build endpoint
- Add pagination for `getMyBuilds()`
- Add filtering and sorting capabilities
- Add build sharing feature
- Add build cloning feature
- Validate that part IDs exist before saving
- Add build statistics and analytics

---

## ✅ Build Status

**Compilation**: ✅ SUCCESS

All code compiles successfully with no errors. The project is ready for testing and deployment.

