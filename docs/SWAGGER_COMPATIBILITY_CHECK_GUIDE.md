# 📘 SWAGGER UI - Compatibility Check API Documentation

## 🚀 Quick Access

**Swagger UI URL:**
```
http://localhost:8080/identity/swagger-ui.html
```

**OpenAPI Docs:**
```
http://localhost:8080/identity/api-docs
```

---

## 🎯 What's New in v2.0.0

### 🆕 Build Compatibility Section
Trong Swagger UI, bạn sẽ thấy section mới:

**"Build Compatibility"** - APIs for checking PC build compatibility with automated validation

Contains:
- `POST /builds/check-compatibility` - Check PC Build Compatibility ⚡

---

## 🔐 How to Use Swagger UI

### Step 1: Start Application
```bash
cd C:\Project\BEBuildPCChecker\J2EE_Build_PC_Checker
.\mvnw.cmd spring-boot:run
```

### Step 2: Open Swagger UI
Navigate to: http://localhost:8080/identity/swagger-ui.html

### Step 3: Authenticate
1. Click **"Authorize"** button (🔓 icon at top right)
2. Get JWT token from login:
   ```
   POST /auth/token
   {
     "email": "haoaboutme@gmail.com",
     "password": "admin"
   }
   ```
3. Copy the `token` from response
4. Paste into Swagger Authorization dialog (without "Bearer" prefix)
5. Click **"Authorize"**
6. Click **"Close"**

Now all API calls will include JWT token automatically! 🎉

---

## 🧪 Testing Compatibility Check in Swagger

### Find the API
1. Scroll to **"Build Compatibility"** section
2. Click on `POST /builds/check-compatibility`
3. Click **"Try it out"**

### Edit Request Body
The UI shows example request with all fields:

```json
{
  "cpuId": "uuid-cpu-id",
  "mainboardId": "uuid-mainboard-id",
  "ramId": "uuid-ram-id",
  "vgaId": "uuid-vga-id",
  "ssdIds": ["uuid-ssd-1", "uuid-ssd-2"],
  "hddIds": ["uuid-hdd-1"],
  "psuId": "uuid-psu-id",
  "caseId": "uuid-case-id",
  "coolerId": "uuid-cooler-id"
}
```

### Get Real Component IDs
Before testing, you need real UUIDs:

1. Expand **"Cpus"** section → `GET /cpus` → Try it out → Execute
2. Copy a CPU `id` from response
3. Repeat for other components (mainboards, rams, vgas, etc.)

### Test with Real Data
Replace UUIDs in request body:

```json
{
  "cpuId": "abc123-real-cpu-id",
  "mainboardId": "def456-real-mainboard-id",
  "ramId": "ghi789-real-ram-id",
  "vgaId": null,
  "ssdIds": [],
  "hddIds": [],
  "psuId": null,
  "caseId": null,
  "coolerId": null
}
```

Click **"Execute"** 🚀

---

## 📊 Understanding the Response

### Example 1: Compatible Build ✅
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

**Interpretation:**
- ✅ Build is compatible (no critical errors)
- ⚠️ Has 1 warning (optimization suggestion)
- 💡 Recommended PSU: 650W

---

### Example 2: Incompatible Build ❌
```json
{
  "code": 1000,
  "message": "Success",
  "result": {
    "compatible": false,
    "errors": [
      "CPU socket 'LGA1700' không khớp với Mainboard socket 'AM5'",
      "PSU chỉ có 450W, không đủ cho hệ thống (khuyến nghị tối thiểu: 650W)"
    ],
    "warnings": [],
    "recommendedPsuWattage": 650
  }
}
```

**Interpretation:**
- ❌ Build is NOT compatible (has errors)
- 🚫 2 critical errors preventing build
- 💡 Need 650W PSU instead of 450W

---

## 🎨 Swagger UI Features

### Interactive Documentation
- ✅ See all request/response schemas
- ✅ Try APIs directly in browser
- ✅ See example values
- ✅ View error responses
- ✅ Download OpenAPI spec

### Compatibility Check API Details

**Endpoint Info:**
```
POST /builds/check-compatibility
Tag: Build Compatibility
Security: Bearer JWT
```

**Request Schema:**
- All fields are optional (nullable)
- Supports partial builds
- UUIDs for component selection

**Response Schema:**
```
{
  compatible: boolean
  errors: string[]        // Critical issues
  warnings: string[]      // Optimization tips
  recommendedPsuWattage: integer
}
```

**Validation Layers Documented:**
1. CPU ↔ Mainboard (Socket, VRM, TDP)
2. RAM (Type, Bus, Slots, Capacity)
3. Case + VGA (Size, Length, PCIe, iGPU, Bottleneck)
4. Storage (M.2, SATA slots, Drive bays)
5. Cooler (TDP support, Height/Radiator)
6. Power (Wattage, Connectors)

---

## 🔍 Swagger UI Sections

### Your API is organized into sections:

1. **Authentication** - Login, token, logout
2. **User Management** - User CRUD operations
3. **🆕 Build Compatibility** - Compatibility check ⚡
4. **Cpus** - CPU management
5. **Mainboards** - Mainboard management
6. **Rams** - RAM management
7. **Vgas** - VGA management
8. **Psus** - PSU management
9. **Cases** - Case management
10. **Coolers** - Cooler management
11. **Ssds** - SSD management
12. **Hdds** - HDD management
13. **Other Components** - Sockets, RAM types, etc.

---

## 💡 Pro Tips

### Tip 1: Use "Try it out" Feature
- Click "Try it out" on any endpoint
- Edit request directly in UI
- Click "Execute" to send request
- See real response immediately

### Tip 2: Check Schema Details
- Click on schema names to expand
- See all field types and descriptions
- View example values
- Understand required vs optional fields

### Tip 3: Copy as cURL
- After executing, scroll down
- Find "Curl" section
- Copy command to use in terminal

### Tip 4: Download OpenAPI Spec
- Top of page: `/api-docs` link
- Download JSON specification
- Import into Postman/Insomnia

### Tip 5: Filter Endpoints
- Use search box at top
- Type "compatibility" to find build check API
- Type "cpu" to find CPU endpoints

---

## 🎯 Testing Scenarios in Swagger

### Scenario 1: Empty Build (Baseline)
```json
{
  "cpuId": null,
  "mainboardId": null,
  "ramId": null,
  "vgaId": null,
  "ssdIds": [],
  "hddIds": [],
  "psuId": null,
  "caseId": null,
  "coolerId": null
}
```
Expected: `compatible: true`, minimal PSU recommendation

---

### Scenario 2: CPU + Mainboard Only
```json
{
  "cpuId": "<get-from-GET-cpus>",
  "mainboardId": "<get-from-GET-mainboards>",
  "ramId": null,
  "vgaId": null,
  "ssdIds": [],
  "hddIds": [],
  "psuId": null,
  "caseId": null,
  "coolerId": null
}
```
Test socket compatibility!

---

### Scenario 3: Full Build
Fill all fields with real IDs and see comprehensive validation!

---

## 🐛 Troubleshooting

### Problem: "401 Unauthorized"
**Solution:** 
1. Click "Authorize" button
2. Login via `/auth/token` endpoint first
3. Copy token and authorize again

### Problem: Can't find compatibility endpoint
**Solution:**
1. Make sure application is running
2. Refresh Swagger UI page
3. Check console for startup errors

### Problem: "404 Not Found"
**Solution:**
- Verify endpoint path: `/builds/check-compatibility` (not `/api/builds/`)
- Check if server is running on port 8080

### Problem: Empty response or errors
**Solution:**
- Ensure you're using valid UUIDs from database
- Check if components exist (GET endpoints first)
- Verify JWT token is still valid

---

## 📚 Additional Resources

### Related Documentation
- [Compatibility Check Quick Start](COMPATIBILITY_CHECK_QUICKSTART.md)
- [Complete API Guide](COMPATIBILITY_CHECK_API_GUIDE.md)
- [Postman Testing Guide](COMPATIBILITY_CHECK_POSTMAN_TESTS.md)

### Swagger Resources
- OpenAPI Specification: https://swagger.io/specification/
- Swagger UI Guide: https://swagger.io/tools/swagger-ui/

---

## 🎉 Features in Swagger UI

### What You'll See:

✅ **Rich Documentation**
- Detailed endpoint descriptions
- Request/response examples
- Schema definitions
- Validation rules explained

✅ **Interactive Testing**
- Try APIs in browser
- No need for Postman
- Automatic JWT inclusion
- Real-time response preview

✅ **Code Generation**
- Copy as cURL
- Export OpenAPI spec
- Generate client code

✅ **Schema Explorer**
- Expandable models
- Field descriptions
- Data types
- Nullable indicators

---

## 🚀 Getting Started Checklist

- [ ] Start application: `mvnw spring-boot:run`
- [ ] Open Swagger: http://localhost:8080/identity/swagger-ui.html
- [ ] Login via `/auth/token`
- [ ] Click "Authorize" and paste token
- [ ] Navigate to "Build Compatibility" section
- [ ] Try `/builds/check-compatibility` endpoint
- [ ] Get component IDs from other endpoints
- [ ] Test with real data
- [ ] Explore validation results

---

**Swagger UI Version:** 5.31.0  
**API Version:** 2.0.0  
**Updated:** February 28, 2026  

**🎊 Happy Testing with Swagger UI! 🎊**

