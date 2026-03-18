# Postman API Request Examples - Complete Guide

## 📋 Mục lục
1. [PcCase (Case)](#1-pccase-case)
2. [SSD](#2-ssd)
3. [HDD](#3-hdd)
4. [PSU](#4-psu)
5. [Cooler](#5-cooler)
6. [Cooler Type](#6-cooler-type)
7. [SSD Type](#7-ssd-type)
8. [Interface Type](#8-interface-type)
9. [PCIe Connector](#9-pcie-connector)

---

## 1. PcCase (Case)

### 📌 Endpoint
**POST** `http://localhost:8080/cases`

### 📝 Request Body

#### Example 1: ATX Full Tower
```json
{
  "name": "NZXT H710i",
  "size": "ATX",
  "maxVgaLengthMm": 413,
  "maxCoolerHeightMm": 185,
  "maxRadiatorSize": 360,
  "drive35Slot": 6,
  "drive25Slot": 4,
  "description": "Full tower case with excellent airflow and RGB lighting"
}
```

#### Example 2: mATX Mid Tower
```json
{
  "name": "Cooler Master MasterBox Q300L",
  "size": "mATX",
  "maxVgaLengthMm": 360,
  "maxCoolerHeightMm": 159,
  "maxRadiatorSize": 240,
  "drive35Slot": 2,
  "drive25Slot": 2,
  "description": "Compact mATX case with magnetic dust filters"
}
```

#### Example 3: ITX Compact
```json
{
  "name": "NZXT H1 V2",
  "size": "ITX",
  "maxVgaLengthMm": 324,
  "maxCoolerHeightMm": 145,
  "maxRadiatorSize": 140,
  "drive35Slot": 0,
  "drive25Slot": 2,
  "description": "All-in-one ITX case with built-in PSU and AIO"
}
```

### ✅ Response
```json
{
  "code": 1000,
  "result": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "name": "NZXT H710i",
    "size": "ATX",
    "maxVgaLengthMm": 413,
    "maxCoolerHeightMm": 185,
    "maxRadiatorSize": 360,
    "drive35Slot": 6,
    "drive25Slot": 4,
    "description": "Full tower case with excellent airflow and RGB lighting"
  }
}
```

---

## 2. SSD

### 📌 Endpoint
**POST** `http://localhost:8080/ssds`

### 📝 Request Body

#### Example 1: NVMe PCIe 4.0 SSD
```json
{
  "name": "Samsung 980 PRO 1TB",
  "ssdTypeId": "NVME",
  "formFactor": "M.2 2280",
  "interfaceTypeId": "PCIE_4",
  "capacity": 1000,
  "tdp": 6,
  "description": "Read: 7000 MB/s, Write: 5000 MB/s, Samsung V-NAND 3-bit MLC"
}
```

#### Example 2: NVMe PCIe 5.0 SSD
```json
{
  "name": "Crucial T700 2TB",
  "ssdTypeId": "NVME",
  "formFactor": "M.2 2280",
  "interfaceTypeId": "PCIE_5",
  "capacity": 2000,
  "tdp": 11,
  "description": "Read: 12400 MB/s, Write: 11800 MB/s, Gen5 NVMe"
}
```

#### Example 3: SATA SSD
```json
{
  "name": "Samsung 870 EVO 500GB",
  "ssdTypeId": "SATA",
  "formFactor": "2.5\"",
  "interfaceTypeId": "SATA_3",
  "capacity": 500,
  "tdp": 3,
  "description": "Read: 560 MB/s, Write: 530 MB/s, SATA III 6Gb/s"
}
```

### ✅ Response
```json
{
  "code": 1000,
  "result": {
    "id": "660e8400-e29b-41d4-a716-446655440001",
    "name": "Samsung 980 PRO 1TB",
    "ssdType": {
      "id": "NVME",
      "name": "NVMe"
    },
    "formFactor": "M.2 2280",
    "interfaceType": {
      "id": "PCIE_4",
      "name": "PCIe 4.0 x4"
    },
    "capacity": 1000,
    "tdp": 6,
    "description": "Read: 7000 MB/s, Write: 5000 MB/s, Samsung V-NAND 3-bit MLC"
  }
}
```

---

## 3. HDD

### 📌 Endpoint
**POST** `http://localhost:8080/hdds`

### 📝 Request Body

#### Example 1: 3.5" Desktop HDD
```json
{
  "name": "Seagate Barracuda 2TB",
  "formFactor": "3.5\"",
  "interfaceTypeId": "SATA_3",
  "capacity": 2000,
  "tdp": 6,
  "description": "7200 RPM, 256MB Cache, CMR Technology"
}
```

#### Example 2: 3.5" High Capacity
```json
{
  "name": "WD Red Plus 8TB",
  "formFactor": "3.5\"",
  "interfaceTypeId": "SATA_3",
  "capacity": 8000,
  "tdp": 8,
  "description": "5400 RPM, 128MB Cache, NAS optimized, CMR"
}
```

#### Example 3: 2.5" Laptop HDD
```json
{
  "name": "Seagate BarraCuda 1TB 2.5\"",
  "formFactor": "2.5\"",
  "interfaceTypeId": "SATA_3",
  "capacity": 1000,
  "tdp": 3,
  "description": "5400 RPM, 128MB Cache, 7mm thickness"
}
```

### ✅ Response
```json
{
  "code": 1000,
  "result": {
    "id": "770e8400-e29b-41d4-a716-446655440002",
    "name": "Seagate Barracuda 2TB",
    "formFactor": "3.5\"",
    "interfaceType": {
      "id": "SATA_3",
      "name": "SATA III"
    },
    "capacity": 2000,
    "tdp": 6,
    "description": "7200 RPM, 256MB Cache, CMR Technology"
  }
}
```

---

## 4. PSU

### 📌 Endpoint
**POST** `http://localhost:8080/psus`

### 📝 Request Body

#### Example 1: High-End PSU with 12VHPWR
```json
{
  "name": "Corsair RM1000x",
  "wattage": 1000,
  "efficiency": "80+ Gold",
  "pcieConnectorId": "12VHPWR",
  "sataConnector": 8,
  "description": "Fully modular, 135mm fan, 10 years warranty"
}
```

#### Example 2: Mid-Range PSU
```json
{
  "name": "EVGA SuperNOVA 850 G6",
  "wattage": 850,
  "efficiency": "80+ Gold",
  "pcieConnectorId": "3X8PIN",
  "sataConnector": 6,
  "description": "Fully modular, 10 years warranty, Eco mode"
}
```

#### Example 3: Budget PSU
```json
{
  "name": "Cooler Master MWE 650 Bronze V2",
  "wattage": 650,
  "efficiency": "80+ Bronze",
  "pcieConnectorId": "2X8PIN",
  "sataConnector": 4,
  "description": "Semi-modular, 5 years warranty, HDB fan"
}
```

#### Example 4: PSU without PCIe Connector (for APU builds)
```json
{
  "name": "Corsair CX450",
  "wattage": 450,
  "efficiency": "80+ Bronze",
  "pcieConnectorId": null,
  "sataConnector": 4,
  "description": "Non-modular, 5 years warranty, for basic builds"
}
```

### ✅ Response
```json
{
  "code": 1000,
  "result": {
    "id": "880e8400-e29b-41d4-a716-446655440003",
    "name": "Corsair RM1000x",
    "wattage": 1000,
    "efficiency": "80+ Gold",
    "pcieConnector": {
      "id": "12VHPWR",
      "name": "12VHPWR (16-pin)"
    },
    "sataConnector": 8,
    "description": "Fully modular, 135mm fan, 10 years warranty"
  }
}
```

---

## 5. Cooler

### 📌 Endpoint
**POST** `http://localhost:8080/coolers`

### 📝 Request Body

#### Example 1: AIO Liquid Cooler 360mm
```json
{
  "name": "NZXT Kraken Z73",
  "coolerTypeId": "AIO",
  "radiatorSize": 360,
  "heightMm": null,
  "tdpSupport": 250,
  "description": "360mm AIO with LCD screen, RGB fans"
}
```

#### Example 2: AIO Liquid Cooler 240mm
```json
{
  "name": "Corsair iCUE H100i RGB PRO XT",
  "coolerTypeId": "AIO",
  "radiatorSize": 240,
  "heightMm": null,
  "tdpSupport": 200,
  "description": "240mm AIO, RGB pump head, ML120 PRO RGB fans"
}
```

#### Example 3: Air Cooler - Tower
```json
{
  "name": "Noctua NH-D15",
  "coolerTypeId": "AIR",
  "radiatorSize": null,
  "heightMm": 165,
  "tdpSupport": 220,
  "description": "Dual tower heatsink, 2x NF-A15 fans, 6 heatpipes"
}
```

#### Example 4: Air Cooler - Compact
```json
{
  "name": "Noctua NH-L9i",
  "coolerTypeId": "AIR",
  "radiatorSize": null,
  "heightMm": 37,
  "tdpSupport": 95,
  "description": "Low-profile cooler for ITX builds, 92mm fan"
}
```

### ✅ Response
```json
{
  "code": 1000,
  "result": {
    "id": "990e8400-e29b-41d4-a716-446655440004",
    "name": "NZXT Kraken Z73",
    "coolerType": {
      "id": "AIO",
      "name": "AIO (All-in-One Liquid Cooler)"
    },
    "radiatorSize": 360,
    "heightMm": null,
    "tdpSupport": 250,
    "description": "360mm AIO with LCD screen, RGB fans"
  }
}
```

---

## 6. Cooler Type

### 📌 Endpoint
**POST** `http://localhost:8080/cooler-types`

### 📝 Request Body

#### Example 1: Air Cooler
```json
{
  "id": "AIR",
  "name": "Air Cooler"
}
```

#### Example 2: AIO Liquid Cooler
```json
{
  "id": "AIO",
  "name": "AIO (All-in-One Liquid Cooler)"
}
```

#### Example 3: Custom Loop (Optional)
```json
{
  "id": "CUSTOM",
  "name": "Custom Water Cooling Loop"
}
```

### ✅ Response
```json
{
  "code": 1000,
  "result": {
    "id": "AIR",
    "name": "Air Cooler"
  }
}
```

---

## 7. SSD Type

### 📌 Endpoint
**POST** `http://localhost:8080/ssd-types`

### 📝 Request Body

#### Example 1: SATA SSD
```json
{
  "id": "SATA",
  "name": "SATA"
}
```

#### Example 2: NVMe SSD
```json
{
  "id": "NVME",
  "name": "NVMe"
}
```

### ✅ Response
```json
{
  "code": 1000,
  "result": {
    "id": "SATA",
    "name": "SATA"
  }
}
```

---

## 8. Interface Type

### 📌 Endpoint
**POST** `http://localhost:8080/interface-types`

### 📝 Request Body

#### Example 1: SATA III (for HDD & SATA SSD)
```json
{
  "id": "SATA_3",
  "name": "SATA III"
}
```

#### Example 2: SAS (for Enterprise HDD)
```json
{
  "id": "SAS",
  "name": "SAS"
}
```

#### Example 3: PCIe 3.0 (for NVMe SSD)
```json
{
  "id": "PCIE_3",
  "name": "PCIe 3.0 x4"
}
```

#### Example 4: PCIe 4.0 (for NVMe SSD)
```json
{
  "id": "PCIE_4",
  "name": "PCIe 4.0 x4"
}
```

#### Example 5: PCIe 5.0 (for Latest NVMe SSD)
```json
{
  "id": "PCIE_5",
  "name": "PCIe 5.0 x4"
}
```

### ✅ Response
```json
{
  "code": 1000,
  "result": {
    "id": "SATA_3",
    "name": "SATA III"
  }
}
```

---

## 9. PCIe Connector

### 📌 Endpoint
**POST** `http://localhost:8080/pcie-connectors`

### 📝 Request Body

#### Example 1: 2x 8-pin
```json
{
  "id": "2X8PIN",
  "name": "2x 8-pin (6+2)"
}
```

#### Example 2: 3x 8-pin
```json
{
  "id": "3X8PIN",
  "name": "3x 8-pin (6+2)"
}
```

#### Example 3: 12VHPWR (for RTX 40 series)
```json
{
  "id": "12VHPWR",
  "name": "12VHPWR (16-pin)"
}
```

#### Example 4: 16-pin (Generic)
```json
{
  "id": "16PIN",
  "name": "16-pin PCIe 5.0"
}
```

#### Example 5: 1x 8-pin (Budget GPU)
```json
{
  "id": "1X8PIN",
  "name": "1x 8-pin (6+2)"
}
```

### ✅ Response
```json
{
  "code": 1000,
  "result": {
    "id": "2X8PIN",
    "name": "2x 8-pin (6+2)"
  }
}
```

---

## 📚 Thứ tự tạo dữ liệu (Dependencies)

### Bước 1: Tạo các Type/Reference entities trước
1. **Cooler Type** (AIR, AIO)
2. **SSD Type** (SATA, NVME)
3. **Interface Type** (SATA_3, PCIE_3, PCIE_4, PCIE_5)
4. **PCIe Connector** (2X8PIN, 3X8PIN, 12VHPWR)

### Bước 2: Tạo các Main entities
5. **Case** (PcCase)
6. **SSD** (cần SsdType, InterfaceType)
7. **HDD** (cần InterfaceType)
8. **PSU** (cần PcieConnector - optional)
9. **Cooler** (cần CoolerType)

---

## 🔍 Common Validation Rules

### Case
- ✅ All fields required except `description`
- ✅ `size`: ATX / mATX / ITX
- ✅ Dimensions in millimeters (mm)

### SSD
- ✅ `ssdTypeId`: Must exist (SATA / NVME)
- ✅ `interfaceTypeId`: Must exist
- ✅ `formFactor`: "2.5\"" / "M.2 2280" / "M.2 2242"
- ✅ `capacity`: GB
- ✅ `tdp`: Watts

### HDD
- ✅ `interfaceTypeId`: Must exist (usually SATA_3 or SAS)
- ✅ `formFactor`: "3.5\"" / "2.5\""
- ✅ `capacity`: GB
- ✅ `tdp`: Watts

### PSU
- ✅ `wattage`: Watts (450, 550, 650, 750, 850, 1000+)
- ✅ `efficiency`: "80+ Bronze" / "80+ Gold" / "80+ Platinum" / "80+ Titanium"
- ✅ `pcieConnectorId`: Optional (nullable)
- ✅ `sataConnector`: Number of SATA power connectors

### Cooler
- ✅ `coolerTypeId`: AIR or AIO
- ✅ AIR: `heightMm` required, `radiatorSize` null
- ✅ AIO: `radiatorSize` required, `heightMm` null
- ✅ `tdpSupport`: Maximum CPU TDP supported (Watts)

---

## 🎯 Tips for Testing

1. **Create reference data first** (Types, Connectors)
2. **Use Postman Collections** to organize requests
3. **Set environment variables** for IDs
4. **Test validation** by omitting required fields
5. **Check foreign key relationships**

---

## 📝 Postman Collection Structure

```
BuildPC Checker API
├── 01. Reference Data
│   ├── Cooler Types
│   ├── SSD Types
│   ├── Interface Types
│   └── PCIe Connectors
├── 02. Storage
│   ├── SSDs
│   └── HDDs
├── 03. Power & Cooling
│   ├── PSUs
│   └── Coolers
└── 04. Case
    └── Cases
```

---

**Generated:** February 14, 2026  
**API Base URL:** `http://localhost:8080`  
**Documentation:** Complete request examples for all entities

