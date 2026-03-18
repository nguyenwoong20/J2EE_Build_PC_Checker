# ErrorCode & Service Update Summary
**Date:** February 14, 2026

## 📋 Overview
Đã cập nhật tất cả Services để sử dụng `AppException` với `ErrorCode` chuẩn thay vì `RuntimeException`. Thêm các error codes còn thiếu cho việc kiểm tra duplicate entities.

---

## ✅ Error Codes Đã Thêm

### 1. Socket (2404)
```java
SOCKET_ALREADY_EXISTS(2404, "Socket already exists", HttpStatus.BAD_REQUEST)
```

### 2. RamType (2504)
```java
RAM_TYPE_ALREADY_EXISTS(2504, "RAM Type already exists", HttpStatus.BAD_REQUEST)
```

### 3. PcieVersion (2604)
```java
PCIE_VERSION_ALREADY_EXISTS(2604, "PCIe Version already exists", HttpStatus.BAD_REQUEST)
```

### 4. SsdType (2714)
```java
SSD_TYPE_ALREADY_EXISTS(2714, "SSD Type already exists", HttpStatus.BAD_REQUEST)
```

### 5. PcieConnector (2914)
```java
PCIE_CONNECTOR_ALREADY_EXISTS(2914, "PCIe Connector already exists", HttpStatus.BAD_REQUEST)
```

### 6. CoolerType (3114)
```java
COOLER_TYPE_ALREADY_EXISTS(3114, "Cooler Type already exists", HttpStatus.BAD_REQUEST)
```

### 7. InterfaceType (3124)
```java
INTERFACE_TYPE_ALREADY_EXISTS(3124, "Interface Type already exists", HttpStatus.BAD_REQUEST)
```

### 8. FormFactor (3134)
```java
FORM_FACTOR_ALREADY_EXISTS(3134, "Form Factor already exists", HttpStatus.BAD_REQUEST)
```

---

## 🔧 Services Đã Cập Nhật

### 1. SocketService ✅
- ✅ Thêm import `AppException` và `ErrorCode`
- ✅ Đổi `RuntimeException` → `AppException(ErrorCode.SOCKET_ALREADY_EXISTS)`
- ✅ Đổi `RuntimeException` → `AppException(ErrorCode.SOCKET_NOT_FOUND)` (tất cả methods)

### 2. RamTypeService ✅
- ✅ Thêm import `AppException` và `ErrorCode`
- ✅ Đổi `RuntimeException` → `AppException(ErrorCode.RAM_TYPE_ALREADY_EXISTS)`
- ✅ Đổi `RuntimeException` → `AppException(ErrorCode.RAM_TYPE_NOT_FOUND)` (tất cả methods)
- ✅ Fix duplicate `deleteRamType` method

### 3. PcieVersionService ✅
- ✅ Thêm import `AppException` và `ErrorCode`
- ✅ Đổi `RuntimeException` → `AppException(ErrorCode.PCIE_VERSION_ALREADY_EXISTS)`
- ✅ Đổi `RuntimeException` → `AppException(ErrorCode.PCIE_VERSION_NOT_FOUND)` (tất cả methods)

### 4. SsdTypeService ✅
- ✅ Thêm import `AppException` và `ErrorCode`
- ✅ Đổi `RuntimeException` → `AppException(ErrorCode.SSD_TYPE_ALREADY_EXISTS)`
- ✅ Đổi `RuntimeException` → `AppException(ErrorCode.SSD_TYPE_NOT_FOUND)` (tất cả methods)

### 5. PcieConnectorService ✅
- ✅ Thêm import `AppException` và `ErrorCode`
- ✅ Đổi `RuntimeException` → `AppException(ErrorCode.PCIE_CONNECTOR_ALREADY_EXISTS)`
- ✅ Đổi `RuntimeException` → `AppException(ErrorCode.PCIE_CONNECTOR_NOT_FOUND)` (tất cả methods)

### 6. CoolerTypeService ✅
- ✅ Thêm import `AppException` và `ErrorCode`
- ✅ Đổi `RuntimeException` → `AppException(ErrorCode.COOLER_TYPE_ALREADY_EXISTS)`
- ✅ Đổi `RuntimeException` → `AppException(ErrorCode.COOLER_TYPE_NOT_FOUND)` (tất cả methods)

### 7. InterfaceTypeService ✅
- ✅ Thêm import `AppException` và `ErrorCode`
- ✅ Đổi `RuntimeException` → `AppException(ErrorCode.INTERFACE_TYPE_ALREADY_EXISTS)`
- ✅ Đổi `RuntimeException` → `AppException(ErrorCode.INTERFACE_TYPE_NOT_FOUND)` (tất cả methods)

### 8. FormFactorService ✅
- ✅ Thêm import `AppException` và `ErrorCode`
- ✅ Đổi `RuntimeException` → `AppException(ErrorCode.FORM_FACTOR_ALREADY_EXISTS)`
- ✅ Đổi `RuntimeException` → `AppException(ErrorCode.FORM_FACTOR_NOT_FOUND)` (tất cả methods)

---

## 📊 Services Đã Có Check Trùng Tên (Không cần thay đổi)

Các service sau đã có sẵn check duplicate name và sử dụng `AppException`:

1. ✅ **CpuService** - Check `CPU_NAME_ALREADY_EXISTS`
2. ✅ **MainboardService** - Check `MAINBOARD_NAME_ALREADY_EXISTS`
3. ✅ **RamService** - Check `RAM_NAME_ALREADY_EXISTS`
4. ✅ **VgaService** - Check `VGA_NAME_ALREADY_EXISTS`
5. ✅ **PsuService** - Check `PSU_NAME_ALREADY_EXISTS`
6. ✅ **CaseService** - Check `CASE_NAME_ALREADY_EXISTS`
7. ✅ **CoolerService** - Check `COOLER_NAME_ALREADY_EXISTS`
8. ✅ **HddService** - Check `HDD_NAME_ALREADY_EXISTS`
9. ✅ **SsdService** - Check `SSD_NAME_ALREADY_EXISTS`

---

## 🔍 Error Code Categories

### Entities with Name Field (Check Duplicate Name)
- CPU (2008)
- Mainboard (2112)
- RAM (2209)
- VGA (2307)
- SSD (2708)
- HDD (2807)
- PSU (2906)
- Case (3009)
- Cooler (3105)

### Type/Lookup Entities (Check Duplicate ID)
- Socket (2404)
- RamType (2504)
- PcieVersion (2604)
- SsdType (2714)
- PcieConnector (2914)
- CoolerType (3114)
- InterfaceType (3124)
- FormFactor (3134)

---

## 📝 Pattern Changes

### Before (RuntimeException)
```java
if (repository.existsById(request.getId())) {
    throw new RuntimeException("Entity already exists with id: " + request.getId());
}

Entity entity = repository.findById(id)
    .orElseThrow(() -> new RuntimeException("Entity not found with id: " + id));
```

### After (AppException with ErrorCode)
```java
if (repository.existsById(request.getId())) {
    throw new AppException(ErrorCode.ENTITY_ALREADY_EXISTS);
}

Entity entity = repository.findById(id)
    .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
```

---

## ✅ Benefits

1. **Consistent Error Handling** - Tất cả services đều sử dụng cùng một pattern
2. **Better Error Messages** - Error codes có sẵn HTTP status và message chuẩn
3. **Easier Maintenance** - Dễ dàng thay đổi error message ở một nơi
4. **Type Safety** - Compile-time checking cho error codes
5. **API Documentation** - Dễ document các error codes cho API consumers
6. **Internationalization Ready** - Có thể dễ dàng thêm multi-language support

---

## 🚀 Next Steps

1. ✅ **Build Success** - Project đã compile thành công
2. ⏳ **Testing** - Test các endpoints để verify error handling
3. ⏳ **Documentation** - Update API documentation với error codes mới
4. ⏳ **Frontend Integration** - Update frontend để handle error codes mới

---

## 📚 Reference

### Error Code Structure
- **1xxx** - Authentication & Authorization
- **2xxx** - Entity Validation & Business Logic
  - **20xx** - CPU
  - **21xx** - Mainboard
  - **22xx** - RAM
  - **23xx** - VGA
  - **24xx** - Socket
  - **25xx** - RamType
  - **26xx** - PcieVersion
  - **27xx** - SSD
  - **28xx** - HDD
  - **29xx** - PSU & PcieConnector
- **3xxx** - Case, Cooler, Types
  - **30xx** - Case
  - **31xx** - Cooler & CoolerType
  - **31xx** - InterfaceType & FormFactor

---

## 🐛 Issues Fixed

1. ✅ Fixed duplicate `deleteRamType` method in `RamTypeService`
2. ✅ Added missing `ALREADY_EXISTS` error codes for all type entities
3. ✅ Standardized all Service exception handling
4. ✅ Fixed FormFactor service to use proper error codes

---

**Status:** ✅ **COMPLETED**  
**Build Status:** ✅ **SUCCESS**  
**Last Updated:** February 14, 2026

