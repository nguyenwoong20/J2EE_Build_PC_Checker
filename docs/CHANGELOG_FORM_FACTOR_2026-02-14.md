# CHANGELOG - Form Factor Entity Migration
**Date:** February 14, 2026

## Overview
Chuyển đổi trường `formFactor` từ String thành Entity độc lập trong các bảng HDD và SSD để tăng tính nhất quán và dễ quản lý dữ liệu.

---

## Changes Summary

### 1. New Entity Created
- **FormFactor** - Entity mới để quản lý các form factor chung cho HDD và SSD

### 2. Database Schema Changes
- Tạo bảng `form_factor` mới
- Thay đổi cột `form_factor` (String) thành `form_factor_id` (Foreign Key) trong bảng `hdd`
- Thay đổi cột `form_factor` (String) thành `form_factor_id` (Foreign Key) trong bảng `ssd`

### 3. Files Created

#### Entity
- `src/main/java/com/j2ee/buildpcchecker/entity/FormFactor.java`

#### Repository
- `src/main/java/com/j2ee/buildpcchecker/repository/FormFactorRepository.java`

#### DTOs
- `src/main/java/com/j2ee/buildpcchecker/dto/request/FormFactorRequest.java`
- `src/main/java/com/j2ee/buildpcchecker/dto/response/FormFactorResponse.java`

#### Mapper
- `src/main/java/com/j2ee/buildpcchecker/mapper/FormFactorMapper.java`

#### Service
- `src/main/java/com/j2ee/buildpcchecker/service/FormFactorService.java`

#### Controller
- `src/main/java/com/j2ee/buildpcchecker/controller/FormFactorController.java`

#### Documentation
- `FORM_FACTOR_MIGRATION.sql` - SQL migration script
- `FORM_FACTOR_API_EXAMPLES.md` - API documentation và examples

---

## 4. Files Modified

### Entity Changes
**File:** `src/main/java/com/j2ee/buildpcchecker/entity/Hdd.java`
```java
// BEFORE:
@Column(name = "form_factor", nullable = false)
String formFactor; // 3.5" / 2.5"

// AFTER:
@ManyToOne
@JoinColumn(name = "form_factor_id", nullable = false)
FormFactor formFactor; // 3.5" / 2.5"
```

**File:** `src/main/java/com/j2ee/buildpcchecker/entity/Ssd.java`
```java
// BEFORE:
@Column(name = "form_factor", nullable = false)
String formFactor; // 2.5", M.2 2280

// AFTER:
@ManyToOne
@JoinColumn(name = "form_factor_id", nullable = false)
FormFactor formFactor; // 2.5", M.2 2280
```

### DTO Changes
**File:** `src/main/java/com/j2ee/buildpcchecker/dto/request/HddCreationRequest.java`
```java
// BEFORE:
@NotBlank(message = "HDD_FORM_FACTOR_REQUIRED")
String formFactor; // 3.5" / 2.5"

// AFTER:
@NotBlank(message = "FORM_FACTOR_ID_REQUIRED")
String formFactorId; // FF_2_5 / FF_3_5
```

**File:** `src/main/java/com/j2ee/buildpcchecker/dto/request/HddUpdateRequest.java`
```java
// BEFORE:
String formFactor;

// AFTER:
String formFactorId;
```

**File:** `src/main/java/com/j2ee/buildpcchecker/dto/response/HddResponse.java`
```java
// BEFORE:
String formFactor;

// AFTER:
FormFactorResponse formFactor;
```

**File:** `src/main/java/com/j2ee/buildpcchecker/dto/request/SsdCreationRequest.java`
```java
// BEFORE:
@NotBlank(message = "SSD_FORM_FACTOR_REQUIRED")
String formFactor; // 2.5", M.2 2280

// AFTER:
@NotBlank(message = "FORM_FACTOR_ID_REQUIRED")
String formFactorId; // FF_2_5 / M2_2280
```

**File:** `src/main/java/com/j2ee/buildpcchecker/dto/request/SsdUpdateRequest.java`
```java
// BEFORE:
String formFactor;

// AFTER:
String formFactorId;
```

**File:** `src/main/java/com/j2ee/buildpcchecker/dto/response/SsdResponse.java`
```java
// BEFORE:
String formFactor;

// AFTER:
FormFactorResponse formFactor;
```

### Mapper Changes
**File:** `src/main/java/com/j2ee/buildpcchecker/mapper/HddMapper.java`
```java
// Added mapping to ignore formFactor (will be set in service)
@Mapping(target = "formFactor", ignore = true)
```

**File:** `src/main/java/com/j2ee/buildpcchecker/mapper/SsdMapper.java`
```java
// Added mapping to ignore formFactor (will be set in service)
@Mapping(target = "formFactor", ignore = true)
```

### Service Changes
**File:** `src/main/java/com/j2ee/buildpcchecker/service/HddService.java`
- Added `FormFactorRepository` dependency
- Updated `createHdd()` method to fetch and set FormFactor entity
- Updated `updateHdd()` method to handle FormFactor updates

**File:** `src/main/java/com/j2ee/buildpcchecker/service/SsdService.java`
- Added `FormFactorRepository` dependency
- Updated `createSsd()` method to fetch and set FormFactor entity
- Updated `updateSsd()` method to handle FormFactor updates

### Exception Changes
**File:** `src/main/java/com/j2ee/buildpcchecker/exception/ErrorCode.java`
```java
// Added new error codes:
FORM_FACTOR_ID_REQUIRED(3131, "Form Factor ID is required", HttpStatus.BAD_REQUEST),
FORM_FACTOR_NAME_REQUIRED(3132, "Form Factor name is required", HttpStatus.BAD_REQUEST),
FORM_FACTOR_NOT_FOUND(3133, "Form Factor not found", HttpStatus.NOT_FOUND)
```

---

## 5. API Endpoints

### New Endpoints
- `POST /form-factors` - Create new form factor
- `GET /form-factors` - Get all form factors
- `GET /form-factors/{id}` - Get form factor by ID
- `PUT /form-factors/{id}` - Update form factor
- `DELETE /form-factors/{id}` - Delete form factor

### Modified Endpoints
**HDD Endpoints:**
- `POST /hdds` - Now requires `formFactorId` instead of `formFactor`
- `PUT /hdds/{id}` - Now uses `formFactorId` instead of `formFactor`
- `GET /hdds/{id}` - Now returns `FormFactorResponse` object instead of String

**SSD Endpoints:**
- `POST /ssds` - Now requires `formFactorId` instead of `formFactor`
- `PUT /ssds/{id}` - Now uses `formFactorId` instead of `formFactor`
- `GET /ssds/{id}` - Now returns `FormFactorResponse` object instead of String

---

## 6. Form Factor Values

### Standard Form Factors
| ID | Name | Description | Used By |
|---|---|---|---|
| `FF_2_5` | 2.5" | 2.5 inch form factor | HDD, SSD (SATA) |
| `FF_3_5` | 3.5" | 3.5 inch form factor | HDD |
| `M2_2280` | M.2 2280 | M.2 2280mm | SSD (NVMe/SATA) |
| `M2_2260` | M.2 2260 | M.2 2260mm | SSD (NVMe/SATA) |
| `M2_2242` | M.2 2242 | M.2 2242mm | SSD (NVMe/SATA) |
| `M2_22110` | M.2 22110 | M.2 22110mm | SSD (NVMe/SATA) |

---

## 7. Migration Steps

### Database Migration
1. Create `form_factor` table
2. Insert standard form factor values
3. Add `form_factor_id` column to `hdd` table
4. Migrate existing `hdd.form_factor` data to `form_factor_id`
5. Add foreign key constraint to `hdd.form_factor_id`
6. Drop old `hdd.form_factor` column
7. Add `form_factor_id` column to `ssd` table
8. Migrate existing `ssd.form_factor` data to `form_factor_id`
9. Add foreign key constraint to `ssd.form_factor_id`
10. Drop old `ssd.form_factor` column

**See:** `FORM_FACTOR_MIGRATION.sql` for complete SQL script

### Application Code
1. Build the project: `mvn clean install`
2. Run the application: `mvn spring-boot:run`
3. Test new endpoints using Postman (see `FORM_FACTOR_API_EXAMPLES.md`)

---

## 8. Testing Checklist

- [ ] Create Form Factor
- [ ] Get all Form Factors
- [ ] Get Form Factor by ID
- [ ] Update Form Factor
- [ ] Delete Form Factor
- [ ] Create HDD with formFactorId
- [ ] Update HDD with formFactorId
- [ ] Get HDD and verify FormFactorResponse
- [ ] Create SSD with formFactorId
- [ ] Update SSD with formFactorId
- [ ] Get SSD and verify FormFactorResponse
- [ ] Test error handling (form factor not found)
- [ ] Test validation (missing formFactorId)

---

## 9. Breaking Changes

⚠️ **WARNING: This is a BREAKING CHANGE**

### For API Consumers
1. **Request Changes:**
   - HDD/SSD creation now requires `formFactorId` (String) instead of `formFactor` (String)
   - HDD/SSD update now uses `formFactorId` (String) instead of `formFactor` (String)

2. **Response Changes:**
   - HDD/SSD responses now return `formFactor` as an object with `id` and `name` fields
   - Old: `"formFactor": "2.5\""`
   - New: `"formFactor": { "id": "FF_2_5", "name": "2.5\"" }`

### Migration Guide for API Consumers
```javascript
// OLD REQUEST
{
  "name": "My HDD",
  "formFactor": "3.5\"",  // ❌ Old way
  "interfaceTypeId": "SATA_3",
  "capacity": 2000,
  "tdp": 6
}

// NEW REQUEST
{
  "name": "My HDD",
  "formFactorId": "FF_3_5",  // ✅ New way
  "interfaceTypeId": "SATA_3",
  "capacity": 2000,
  "tdp": 6
}
```

---

## 10. Benefits

1. **Data Consistency:** Standardized form factor values across the system
2. **Easier Maintenance:** Central management of form factor data
3. **Better Validation:** Foreign key constraints ensure data integrity
4. **Internationalization Ready:** Can easily add translations for form factor names
5. **Flexibility:** Easy to add new form factors without code changes
6. **Query Optimization:** Better database query performance with indexed foreign keys

---

## 11. Future Enhancements

- [ ] Add form factor dimensions (width, height, depth)
- [ ] Add form factor compatibility matrix
- [ ] Add form factor images/icons
- [ ] Support for custom form factors
- [ ] Multi-language support for form factor names

---

## 12. Contact & Support

If you encounter any issues during migration, please contact the development team or refer to:
- `FORM_FACTOR_MIGRATION.sql` - Database migration script
- `FORM_FACTOR_API_EXAMPLES.md` - API documentation
- Project README.md - General project information

---

**Last Updated:** February 14, 2026  
**Version:** 1.0.0  
**Author:** Development Team

