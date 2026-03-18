# ✅ FIX SWAGGER MAP DISPLAY - SHOW PROPER KEYS

## ❌ Vấn Đề

Swagger UI hiển thị `additionalProp1`, `additionalProp2`, `additionalProp3` cho Map field thay vì các key thực tế như `cpu`, `mainboard`, `ram`.

**Trước khi fix:**
```json
{
  "name": "string",
  "description": "string",
  "parts": {
    "additionalProp1": "string",
    "additionalProp2": "string",
    "additionalProp3": "string"
  }
}
```

---

## ✅ Giải Pháp

Thêm Swagger annotations với example cụ thể:

1. **Trong DTO** - Thêm `@Schema` với example
2. **Trong Controller** - Thêm `@RequestBody` với example

---

## 🔧 Code Changes

### 1. SaveBuildRequest.java (DTO)

**Thêm import:**
```java
import io.swagger.v3.oas.annotations.media.Schema;
```

**Thêm @Schema annotations:**
```java
@NotBlank(message = "BUILD_NAME_REQUIRED")
@Schema(description = "Build name", example = "Gaming Build 2026")
String name;

@Schema(description = "Build description", example = "Intel i9 + RTX 4090 Gaming Setup")
String description;

@Schema(
    description = "Map of part types to part IDs. Keys: cpu, mainboard, ram, gpu, psu, case, cooler, ssd, hdd",
    example = "{\"cpu\": \"550e8400-e29b-41d4-a716-446655440000\", \"mainboard\": \"550e8400-e29b-41d4-a716-446655440001\", \"ram\": \"550e8400-e29b-41d4-a716-446655440002\", \"gpu\": \"550e8400-e29b-41d4-a716-446655440003\", \"psu\": \"550e8400-e29b-41d4-a716-446655440004\"}"
)
Map<String, String> parts;
```

### 2. BuildController.java

**Thêm @RequestBody annotation với example:**
```java
@PostMapping
public ApiResponse<PcBuildResponse> saveBuild(
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "PC Build configuration with name, description, and parts map",
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = SaveBuildRequest.class),
            examples = @ExampleObject(
                name = "Sample Build Request",
                value = """
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
                            "ssd": "550e8400-e29b-41d4-a716-446655440007",
                            "hdd": "550e8400-e29b-41d4-a716-446655440008"
                          }
                        }
                        """
            )
        )
    )
    @Valid @RequestBody SaveBuildRequest request) {
    // ... method implementation
}
```

**Thêm response example:**
```java
@io.swagger.v3.oas.annotations.responses.ApiResponse(
    responseCode = "200",
    description = "Build saved successfully",
    content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = ApiResponse.class),
        examples = @ExampleObject(
            name = "Success Response",
            value = """
                    {
                      "code": 1000,
                      "message": "Build saved successfully",
                      "result": {
                        "id": "7c9e8b5a-1234-5678-90ab-cdef12345678",
                        "name": "Gaming Build 2026",
                        "description": "Intel i9 + RTX 4090 Gaming Setup",
                        "totalTdp": null,
                        "createdAt": "2026-03-09T20:45:00",
                        "userId": "user-uuid",
                        "parts": {
                          "CPU": "550e8400-e29b-41d4-a716-446655440000",
                          "MAINBOARD": "550e8400-e29b-41d4-a716-446655440001",
                          "RAM": "550e8400-e29b-41d4-a716-446655440002",
                          "GPU": "550e8400-e29b-41d4-a716-446655440003",
                          "PSU": "550e8400-e29b-41d4-a716-446655440004"
                        }
                      }
                    }
                    """
        )
    )
)
```

---

## ✅ Sau Khi Fix

**Swagger UI sẽ hiển thị:**
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
    "ssd": "550e8400-e29b-41d4-a716-446655440007",
    "hdd": "550e8400-e29b-41d4-a716-446655440008"
  }
}
```

---

## 📝 Part Keys Supported

| Key | Description | Example UUID |
|-----|-------------|--------------|
| `cpu` | Processor | 550e8400-e29b-41d4-a716-446655440000 |
| `mainboard` | Motherboard | 550e8400-e29b-41d4-a716-446655440001 |
| `ram` | Memory | 550e8400-e29b-41d4-a716-446655440002 |
| `gpu` | Graphics Card | 550e8400-e29b-41d4-a716-446655440003 |
| `psu` | Power Supply | 550e8400-e29b-41d4-a716-446655440004 |
| `case` | PC Case | 550e8400-e29b-41d4-a716-446655440005 |
| `cooler` | CPU Cooler | 550e8400-e29b-41d4-a716-446655440006 |
| `ssd` | Solid State Drive | 550e8400-e29b-41d4-a716-446655440007 |
| `hdd` | Hard Disk Drive | 550e8400-e29b-41d4-a716-446655440008 |

**Note:** All keys sẽ được convert sang UPPERCASE khi lưu vào database

---

## 🎯 Tại Sao Cần Fix Này?

### Vấn đề với Map trong Swagger
- Swagger/OpenAPI không tự động biết key names của Map
- Mặc định hiển thị generic keys: `additionalProp1, additionalProp2, additionalProp3`
- Developer không biết cần truyền key gì

### Giải pháp
- Dùng `@Schema(example = "...")` trong DTO
- Dùng `@ExampleObject` trong Controller
- Swagger UI sẽ hiển thị example với key thực tế

---

## 🧪 Testing

### Swagger UI
```
http://localhost:8080/identity/swagger-ui.html
```

1. Khởi động app: `.\mvnw.cmd spring-boot:run`
2. Mở Swagger UI
3. Click "Authorize" và nhập JWT token
4. Vào endpoint **POST /builds**
5. Kiểm tra Request Body - Bây giờ sẽ thấy:
   ```
   cpu, mainboard, ram, gpu, psu, case, cooler, ssd, hdd
   ```
   thay vì `additionalProp1, additionalProp2, additionalProp3`

---

## ✅ Build Status

```
[INFO] BUILD SUCCESS ✅
[INFO] Total time: 8.688 s
[INFO] 207 source files compiled
```

---

## 📋 Summary

✅ Thêm `@Schema` annotation vào `SaveBuildRequest.java`  
✅ Thêm `@RequestBody` annotation với example vào Controller  
✅ Thêm response example cho endpoint  
✅ Swagger UI giờ hiển thị đúng key names  
✅ Build thành công  

**Kết quả:** Swagger UI giờ hiển thị đúng tên các part types thay vì `additionalProp1, 2, 3`!

