# PC PARTS ENTITIES - Tổng Hợp Thuộc Tính

> **Mục đích:** File này tổng hợp tất cả các entity PC components để phát triển chức năng kiểm tra tương thích (compatibility checker)

**Ngày tạo:** 21/02/2026

---

## 📋 Danh Sách PC Parts

### 1. Core Components (Linh kiện chính)
- [CPU](#1-cpu-central-processing-unit)
- [Mainboard](#2-mainboard-bo-mạch-chủ)
- [RAM](#3-ram-random-access-memory)
- [VGA](#4-vga-card-đồ-họa)
- [Storage (SSD, HDD)](#5-ssd-solid-state-drive)

### 2. Power & Cooling
- [PSU](#7-psu-power-supply-unit)
- [Cooler](#9-cooler-tản-nhiệt)

### 3. Case
- [PC Case](#8-pc-case-vỏ-case)

### 4. Supporting Entities (Entities hỗ trợ)
- [Socket](#10-socket)
- [RamType](#11-ramtype)
- [PcieVersion](#12-pcieversion)
- [PcieConnector](#13-pcieconnector)
- [CoolerType](#14-coolertype)
- [SsdType](#15-ssdtype)
- [InterfaceType](#16-interfacetype)
- [FormFactor](#17-formfactor)
- [CaseSize](#18-casesize)

---

## 1. CPU (Central Processing Unit)

**Entity:** `Cpu.java`  
**Table:** `cpu`

### Thuộc Tính:

| Field | Type | Mô Tả | Quan Hệ | Ghi Chú |
|-------|------|-------|---------|---------|
| `id` | String (UUID) | Primary Key | - | Auto-generated |
| `name` | String | Tên CPU | - | Required |
| `socket` | Socket | Loại socket | **ManyToOne → Socket** | Required, FK: socket_id |
| `vrmMin` | Integer | VRM tối thiểu yêu cầu | - | Optional, để check mainboard |
| `igpu` | Boolean | Có GPU tích hợp không | - | Required |
| `tdp` | Integer | Công suất tiêu thụ (W) | - | Required, để check PSU & Cooler |
| `pcieVersion` | PcieVersion | Phiên bản PCIe hỗ trợ | **ManyToOne → PcieVersion** | Required, FK: pcie_version_id |
| `score` | Integer | Điểm benchmark | - | Required |
| `description` | String (TEXT) | Mô tả | - | Optional |

### Compatibility Checks:
- ✅ **Socket** phải khớp với Mainboard.socket
- ✅ **vrmMin** ≤ Mainboard.vrmPhase
- ✅ **tdp** ≤ Mainboard.cpuTdpSupport
- ✅ **tdp** ≤ Cooler.tdpSupport
- ✅ **pcieVersion** tương thích với Mainboard.pcieVgaVersion (backward compatible)

---

## 2. Mainboard (Bo mạch chủ)

**Entity:** `Mainboard.java`  
**Table:** `mainboard`

### Thuộc Tính:

| Field | Type | Mô Tả | Quan Hệ | Ghi Chú |
|-------|------|-------|---------|---------|
| `id` | String (UUID) | Primary Key | - | Auto-generated |
| `name` | String | Tên mainboard | - | Required |
| `socket` | Socket | Loại socket CPU | **ManyToOne → Socket** | Required, FK: socket_id |
| `vrmPhase` | Integer | Số pha VRM | - | Required, check với CPU.vrmMin |
| `cpuTdpSupport` | Integer | TDP CPU tối đa hỗ trợ (W) | - | Required |
| `ramType` | RamType | Loại RAM hỗ trợ | **ManyToOne → RamType** | Required, FK: ram_type_id |
| `ramBusMax` | Integer | RAM Bus tối đa (MHz) | - | Required |
| `ramSlot` | Integer | Số khe RAM | - | Required |
| `ramMaxCapacity` | Integer | Dung lượng RAM tối đa (GB) | - | Required |
| `size` | String | Kích thước mainboard | - | Required (ATX, mATX, ITX) |
| `pcieVgaVersion` | PcieVersion | Phiên bản PCIe cho VGA | **ManyToOne → PcieVersion** | Required, FK: pcie_vga_version_id |
| `m2Slot` | Integer | Số khe M.2 | - | Optional, check SSD M.2 |
| `sataSlot` | Integer | Số khe SATA | - | Optional, check SSD/HDD SATA |
| `description` | String (TEXT) | Mô tả | - | Optional |

### Compatibility Checks:
- ✅ **socket** phải khớp với CPU.socket
- ✅ **vrmPhase** ≥ CPU.vrmMin
- ✅ **cpuTdpSupport** ≥ CPU.tdp
- ✅ **ramType** phải khớp với RAM.ramType
- ✅ **ramBusMax** ≥ RAM.ramBus
- ✅ **ramSlot** ≥ số lượng RAM sticks
- ✅ **ramMaxCapacity** ≥ tổng RAM capacity
- ✅ **size** phải phù hợp với Case.size
- ✅ **pcieVgaVersion** tương thích với VGA.pcieVersion
- ✅ **m2Slot** ≥ số lượng SSD M.2
- ✅ **sataSlot** ≥ tổng số SSD/HDD SATA

---

## 3. RAM (Random Access Memory)

**Entity:** `Ram.java`  
**Table:** `ram`

### Thuộc Tính:

| Field | Type | Mô Tả | Quan Hệ | Ghi Chú |
|-------|------|-------|---------|---------|
| `id` | String (UUID) | Primary Key | - | Auto-generated |
| `name` | String | Tên RAM | - | Required |
| `ramType` | RamType | Loại RAM | **ManyToOne → RamType** | Required, FK: ram_type_id |
| `ramBus` | Integer | Bus speed (MHz) | - | Required |
| `ramCas` | Integer | CAS Latency | - | Required |
| `capacityPerStick` | Integer | Dung lượng mỗi thanh (GB) | - | Required |
| `quantity` | Integer | Số lượng thanh | - | Required (kit 1x, 2x, 4x) |
| `tdp` | Integer | Công suất (W) | - | Required, để tính tổng PSU |
| `description` | String (TEXT) | Mô tả | - | Optional |

### Compatibility Checks:
- ✅ **ramType** phải khớp với Mainboard.ramType
- ✅ **ramBus** ≤ Mainboard.ramBusMax
- ✅ **quantity** ≤ Mainboard.ramSlot
- ✅ **capacityPerStick × quantity** ≤ Mainboard.ramMaxCapacity

---

## 4. VGA (Card Đồ Họa)

**Entity:** `Vga.java`  
**Table:** `vga`

### Thuộc Tính:

| Field | Type | Mô Tả | Quan Hệ | Ghi Chú |
|-------|------|-------|---------|---------|
| `id` | String (UUID) | Primary Key | - | Auto-generated |
| `name` | String | Tên VGA | - | Required |
| `lengthMm` | Integer | Độ dài card (mm) | - | Required, check với Case |
| `tdp` | Integer | Công suất (W) | - | Required, để tính PSU |
| `pcieVersion` | PcieVersion | Phiên bản PCIe | **ManyToOne → PcieVersion** | Required, FK: pcie_version_id |
| `powerConnector` | String | Loại nguồn phụ | - | Optional (6pin, 8pin, 12VHPWR) |
| `score` | Integer | Điểm benchmark | - | Required |
| `description` | String (TEXT) | Mô tả | - | Optional |

### Compatibility Checks:
- ✅ **lengthMm** ≤ Case.maxVgaLengthMm
- ✅ **pcieVersion** tương thích với Mainboard.pcieVgaVersion (backward compatible)
- ✅ **powerConnector** phải được PSU hỗ trợ (nếu có)
- ✅ **tdp** được tính vào tổng công suất PSU

---

## 5. SSD (Solid State Drive)

**Entity:** `Ssd.java`  
**Table:** `ssd`

### Thuộc Tính:

| Field | Type | Mô Tả | Quan Hệ | Ghi Chú |
|-------|------|-------|---------|---------|
| `id` | String (UUID) | Primary Key | - | Auto-generated |
| `name` | String | Tên SSD | - | Required |
| `ssdType` | SsdType | Loại SSD | **ManyToOne → SsdType** | Required, FK: ssd_type_id |
| `formFactor` | FormFactor | Form factor | **ManyToOne → FormFactor** | Required, FK: form_factor_id |
| `interfaceType` | InterfaceType | Chuẩn kết nối | **ManyToOne → InterfaceType** | Required, FK: interface_type_id |
| `capacity` | Integer | Dung lượng (GB) | - | Required |
| `tdp` | Integer | Công suất (W) | - | Required |
| `description` | String (TEXT) | Mô tả | - | Optional |

### Compatibility Checks:
- ✅ **formFactor = M.2** → cần Mainboard.m2Slot > 0
- ✅ **formFactor = 2.5"** và **ssdType = SATA** → cần Mainboard.sataSlot > 0
- ✅ **formFactor = 2.5"** → cần Case.drive25Slot > 0
- ✅ Tổng SSD SATA ≤ Mainboard.sataSlot
- ✅ Tổng SSD M.2 ≤ Mainboard.m2Slot

---

## 6. HDD (Hard Disk Drive)

**Entity:** `Hdd.java`  
**Table:** `hdd`

### Thuộc Tính:

| Field | Type | Mô Tả | Quan Hệ | Ghi Chú |
|-------|------|-------|---------|---------|
| `id` | String (UUID) | Primary Key | - | Auto-generated |
| `name` | String | Tên HDD | - | Required |
| `formFactor` | FormFactor | Form factor | **ManyToOne → FormFactor** | Required, FK: form_factor_id |
| `interfaceType` | InterfaceType | Chuẩn kết nối | **ManyToOne → InterfaceType** | Required, FK: interface_type_id |
| `capacity` | Integer | Dung lượng (GB) | - | Required |
| `tdp` | Integer | Công suất (W) | - | Required |
| `description` | String (TEXT) | Mô tả | - | Optional |

### Compatibility Checks:
- ✅ **formFactor = 3.5"** → cần Case.drive35Slot > 0
- ✅ **formFactor = 2.5"** → cần Case.drive25Slot > 0
- ✅ **interfaceType = SATA** → cần Mainboard.sataSlot > 0
- ✅ Tổng HDD SATA ≤ Mainboard.sataSlot
- ✅ PSU.sataConnector ≥ số lượng HDD/SSD SATA

---

## 7. PSU (Power Supply Unit)

**Entity:** `Psu.java`  
**Table:** `psu`

### Thuộc Tính:

| Field | Type | Mô Tả | Quan Hệ | Ghi Chú |
|-------|------|-------|---------|---------|
| `id` | String (UUID) | Primary Key | - | Auto-generated |
| `name` | String | Tên PSU | - | Required |
| `wattage` | Integer | Công suất (W) | - | Required |
| `efficiency` | String | Hiệu suất | - | Required (80+ Bronze, Gold, Platinum) |
| `pcieConnector` | PcieConnector | Đầu PCIe cho VGA | **ManyToOne → PcieConnector** | Optional, FK: pcie_connector_id |
| `sataConnector` | Integer | Số đầu SATA | - | Required |
| `description` | String (TEXT) | Mô tả | - | Optional |

### Compatibility Checks:
- ✅ **wattage** ≥ Tổng TDP (CPU + VGA + RAM + SSD + HDD + 20% запас)
- ✅ **pcieConnector** phải hỗ trợ VGA.powerConnector
- ✅ **sataConnector** ≥ số lượng SSD/HDD SATA

### Formula Tính Tổng TDP:
```
Total TDP = CPU.tdp + VGA.tdp + (RAM.tdp × RAM.quantity) + 
            ΣAllSSDs.tdp + ΣAllHDDs.tdp + 50W (mainboard, fans, etc.)

Recommended PSU = Total TDP × 1.2 (20% buffer)
```

---

## 8. PC Case (Vỏ Case)

**Entity:** `PcCase.java`  
**Table:** `pc_case`

### Thuộc Tính:

| Field | Type | Mô Tả | Quan Hệ | Ghi Chú |
|-------|------|-------|---------|---------|
| `id` | String (UUID) | Primary Key | - | Auto-generated |
| `name` | String | Tên case | - | Required |
| `size` | CaseSize | Kích thước case | **ManyToOne → CaseSize** | Required, FK: size_id |
| `maxVgaLengthMm` | Integer | Độ dài VGA tối đa (mm) | - | Required |
| `maxCoolerHeightMm` | Integer | Chiều cao cooler tối đa (mm) | - | Required, cho tản khí |
| `maxRadiatorSize` | Integer | Kích thước radiator tối đa | - | Required (120, 240, 360) |
| `drive35Slot` | Integer | Số khay ổ 3.5" | - | Required |
| `drive25Slot` | Integer | Số khay ổ 2.5" | - | Required |
| `description` | String (TEXT) | Mô tả | - | Optional |

### Compatibility Checks:
- ✅ **size** tương thích với Mainboard.size (ATX case chứa mATX/ITX mainboard)
- ✅ **maxVgaLengthMm** ≥ VGA.lengthMm
- ✅ **maxCoolerHeightMm** ≥ Cooler.heightMm (nếu Air Cooler)
- ✅ **maxRadiatorSize** ≥ Cooler.radiatorSize (nếu AIO)
- ✅ **drive35Slot** ≥ số lượng HDD 3.5"
- ✅ **drive25Slot** ≥ số lượng HDD 2.5" + SSD 2.5"

---

## 9. Cooler (Tản Nhiệt)

**Entity:** `Cooler.java`  
**Table:** `cooler`

### Thuộc Tính:

| Field | Type | Mô Tả | Quan Hệ | Ghi Chú |
|-------|------|-------|---------|---------|
| `id` | String (UUID) | Primary Key | - | Auto-generated |
| `name` | String | Tên cooler | - | Required |
| `coolerType` | CoolerType | Loại tản nhiệt | **ManyToOne → CoolerType** | Required, FK: cooler_type_id |
| `radiatorSize` | Integer | Kích thước radiator | - | Nullable (chỉ cho AIO: 120, 240, 360) |
| `heightMm` | Integer | Chiều cao (mm) | - | Nullable (chỉ cho Air Cooler) |
| `tdpSupport` | Integer | TDP hỗ trợ (W) | - | Required |
| `description` | String (TEXT) | Mô tả | - | Optional |

### Compatibility Checks:
- ✅ **tdpSupport** ≥ CPU.tdp
- ✅ Nếu **coolerType = AIR** → heightMm ≤ Case.maxCoolerHeightMm
- ✅ Nếu **coolerType = AIO** → radiatorSize ≤ Case.maxRadiatorSize

---

## 10. Socket

**Entity:** `Socket.java`  
**Table:** `socket`  
**Type:** Lookup/Reference Entity

### Thuộc Tính:

| Field | Type | Mô Tả | Ghi Chú |
|-------|------|-------|---------|
| `id` | String | Primary Key | AM4, AM5, LGA1700, LGA1200 |
| `name` | String | Tên socket | Required |

### Dùng Cho:
- CPU.socket
- Mainboard.socket

### Compatibility:
- CPU.socket **PHẢI BẰNG** Mainboard.socket (exact match)

---

## 11. RamType

**Entity:** `RamType.java`  
**Table:** `ram_type`  
**Type:** Lookup/Reference Entity

### Thuộc Tính:

| Field | Type | Mô Tả | Ghi Chú |
|-------|------|-------|---------|
| `id` | String | Primary Key | DDR3, DDR4, DDR5 |
| `name` | String | Tên loại RAM | Required |

### Dùng Cho:
- RAM.ramType
- Mainboard.ramType

### Compatibility:
- RAM.ramType **PHẢI BẰNG** Mainboard.ramType (exact match)

---

## 12. PcieVersion

**Entity:** `PcieVersion.java`  
**Table:** `pcie_version`  
**Type:** Lookup/Reference Entity

### Thuộc Tính:

| Field | Type | Mô Tả | Ghi Chú |
|-------|------|-------|---------|
| `id` | String | Primary Key | PCIE_3, PCIE_4, PCIE_5 |
| `name` | String | Tên phiên bản | PCIe 3.0, PCIe 4.0, PCIe 5.0 |

### Dùng Cho:
- CPU.pcieVersion
- VGA.pcieVersion
- Mainboard.pcieVgaVersion

### Compatibility:
- **Backward Compatible**: PCIe 5.0 > PCIe 4.0 > PCIe 3.0
- VGA PCIe 3.0 có thể chạy trên Mainboard PCIe 4.0/5.0
- VGA PCIe 4.0 có thể chạy trên Mainboard PCIe 5.0 (nhưng giảm tốc độ trên PCIe 3.0)

---

## 13. PcieConnector

**Entity:** `PcieConnector.java`  
**Table:** `pcie_connector`  
**Type:** Lookup/Reference Entity

### Thuộc Tính:

| Field | Type | Mô Tả | Ghi Chú |
|-------|------|-------|---------|
| `id` | String | Primary Key | 2X8PIN, 3X8PIN, 12VHPWR, 16PIN |
| `name` | String | Tên connector | Required |

### Dùng Cho:
- PSU.pcieConnector

### Compatibility:
- PSU có 3X8PIN có thể cấp nguồn cho VGA cần 2X8PIN
- PSU có 12VHPWR có thể cấp nguồn cho VGA RTX 4000 series

---

## 14. CoolerType

**Entity:** `CoolerType.java`  
**Table:** `cooler_type`  
**Type:** Lookup/Reference Entity

### Thuộc Tính:

| Field | Type | Mô Tả | Ghi Chú |
|-------|------|-------|---------|
| `id` | String | Primary Key | AIR, AIO |
| `name` | String | Tên loại | Air Cooling, All-In-One Liquid |

### Dùng Cho:
- Cooler.coolerType

### Logic:
- **AIR**: Check heightMm với Case.maxCoolerHeightMm
- **AIO**: Check radiatorSize với Case.maxRadiatorSize

---

## 15. SsdType

**Entity:** `SsdType.java`  
**Table:** `ssd_type`  
**Type:** Lookup/Reference Entity

### Thuộc Tính:

| Field | Type | Mô Tả | Ghi Chú |
|-------|------|-------|---------|
| `id` | String | Primary Key | SATA, NVME |
| `name` | String | Tên loại | SATA, NVMe |

### Dùng Cho:
- SSD.ssdType

---

## 16. InterfaceType

**Entity:** `InterfaceType.java`  
**Table:** `interface_type`  
**Type:** Lookup/Reference Entity

### Thuộc Tính:

| Field | Type | Mô Tả | Ghi Chú |
|-------|------|-------|---------|
| `id` | String | Primary Key | SATA_3, SAS, PCIE_3, PCIE_4, PCIE_5 |
| `name` | String | Tên interface | SATA III, SAS, PCIe 3.0 x4, PCIe 4.0 x4 |

### Dùng Cho:
- SSD.interfaceType
- HDD.interfaceType

---

## 17. FormFactor

**Entity:** `FormFactor.java`  
**Table:** `form_factor`  
**Type:** Lookup/Reference Entity

### Thuộc Tính:

| Field | Type | Mô Tả | Ghi Chú |
|-------|------|-------|---------|
| `id` | String | Primary Key | FF_2_5, FF_3_5, M2_2280, M2_2260, M2_2242 |
| `name` | String | Tên form factor | 2.5", 3.5", M.2 2280, M.2 2260 |

### Dùng Cho:
- SSD.formFactor
- HDD.formFactor

---

## 18. CaseSize

**Entity:** `CaseSize.java`  
**Table:** `case_size`  
**Type:** Lookup/Reference Entity

### Thuộc Tính:

| Field | Type | Mô Tả | Ghi Chú |
|-------|------|-------|---------|
| `id` | String | Primary Key | ATX, mATX, ITX |
| `name` | String | Tên kích thước | ATX Full Tower, Micro ATX, Mini ITX |

### Dùng Cho:
- PcCase.size

### Compatibility Matrix:

| Case Size | ATX Board | mATX Board | ITX Board |
|-----------|-----------|------------|-----------|
| ATX       | ✅        | ✅         | ✅        |
| mATX      | ❌        | ✅         | ✅        |
| ITX       | ❌        | ❌         | ✅        |

---

## 🔗 Relationships Map

```
CPU
├── socket → Socket (M:1)
└── pcieVersion → PcieVersion (M:1)

Mainboard
├── socket → Socket (M:1)
├── ramType → RamType (M:1)
└── pcieVgaVersion → PcieVersion (M:1)

RAM
└── ramType → RamType (M:1)

VGA
└── pcieVersion → PcieVersion (M:1)

SSD
├── ssdType → SsdType (M:1)
├── formFactor → FormFactor (M:1)
└── interfaceType → InterfaceType (M:1)

HDD
├── formFactor → FormFactor (M:1)
└── interfaceType → InterfaceType (M:1)

PSU
└── pcieConnector → PcieConnector (M:1)

PcCase
└── size → CaseSize (M:1)

Cooler
└── coolerType → CoolerType (M:1)
```

---

## 🎯 Compatibility Rules Summary

### 1. CPU ↔ Mainboard
```
✅ CPU.socket == Mainboard.socket
✅ CPU.vrmMin <= Mainboard.vrmPhase (if vrmMin not null)
✅ CPU.tdp <= Mainboard.cpuTdpSupport
✅ CPU.pcieVersion compatible with Mainboard.pcieVgaVersion
```

### 2. CPU ↔ Cooler
```
✅ CPU.tdp <= Cooler.tdpSupport
```

### 3. Mainboard ↔ RAM
```
✅ Mainboard.ramType == RAM.ramType
✅ Mainboard.ramBusMax >= RAM.ramBus
✅ Mainboard.ramSlot >= RAM.quantity
✅ Mainboard.ramMaxCapacity >= (RAM.capacityPerStick × RAM.quantity)
```

### 4. Mainboard ↔ VGA
```
✅ Mainboard.pcieVgaVersion compatible with VGA.pcieVersion (backward compatible)
```

### 5. Mainboard ↔ Storage
```
✅ Mainboard.m2Slot >= Count(SSD where formFactor = M.2)
✅ Mainboard.sataSlot >= Count(SSD+HDD where interfaceType = SATA)
```

### 6. Case ↔ Mainboard
```
✅ Case.size compatible with Mainboard.size
   - ATX case: fits ATX, mATX, ITX
   - mATX case: fits mATX, ITX
   - ITX case: fits ITX only
```

### 7. Case ↔ VGA
```
✅ Case.maxVgaLengthMm >= VGA.lengthMm
```

### 8. Case ↔ Cooler
```
✅ If Cooler.coolerType == AIR:
   Case.maxCoolerHeightMm >= Cooler.heightMm
   
✅ If Cooler.coolerType == AIO:
   Case.maxRadiatorSize >= Cooler.radiatorSize
```

### 9. Case ↔ Storage
```
✅ Case.drive35Slot >= Count(HDD where formFactor = 3.5")
✅ Case.drive25Slot >= Count(HDD where formFactor = 2.5") + Count(SSD where formFactor = 2.5")
```

### 10. PSU ↔ All Components
```
✅ PSU.wattage >= TotalTDP × 1.2
   where TotalTDP = CPU.tdp + VGA.tdp + (RAM.tdp × quantity) + 
                    Σ(SSD.tdp) + Σ(HDD.tdp) + 50W (overhead)

✅ If VGA.powerConnector exists:
   PSU.pcieConnector compatible with VGA.powerConnector

✅ PSU.sataConnector >= Count(SSD+HDD where interfaceType = SATA)
```

---

## 📊 Recommended Build Flow

### Bước 1: Chọn CPU
- Xác định socket
- Xác định TDP
- Xác định có cần iGPU không

### Bước 2: Chọn Mainboard
- Phải khớp socket với CPU
- Phải hỗ trợ TDP của CPU
- Chọn RAM type (DDR4/DDR5)
- Chọn size (ATX/mATX/ITX)

### Bước 3: Chọn RAM
- Phải khớp RAM type với Mainboard
- RAM bus ≤ Mainboard max bus
- Tổng capacity và số lượng phù hợp

### Bước 4: Chọn VGA (Optional nếu CPU có iGPU)
- Check PCIe compatibility
- Note TDP và length

### Bước 5: Chọn Storage
- Check M.2 slots
- Check SATA slots
- Note TDP

### Bước 6: Chọn PSU
- Tính tổng TDP + 20% buffer
- Check PCIe connectors cho VGA
- Check SATA connectors

### Bước 7: Chọn Case
- Phải fit Mainboard size
- Phải fit VGA length
- Check drive bays

### Bước 8: Chọn Cooler
- Phải hỗ trợ CPU TDP
- Phải fit trong Case (height/radiator)

---

## 🚨 Common Incompatibility Issues

### ❌ Socket Mismatch
```
CPU: AMD Ryzen (AM5) ≠ Mainboard: Intel (LGA1700)
→ KHÔNG TƯƠNG THÍCH
```

### ❌ RAM Type Mismatch
```
RAM: DDR5 ≠ Mainboard: DDR4
→ KHÔNG TƯƠNG THÍCH
```

### ❌ Insufficient PSU Wattage
```
Total TDP: 500W but PSU: 450W
→ KHÔNG ĐỦ CÔNG SUẤT
```

### ❌ VGA Too Long
```
VGA: 350mm but Case max: 320mm
→ KHÔNG VỪA
```

### ❌ Not Enough Slots
```
4 × SSD SATA but Mainboard: 2 SATA slots
→ KHÔNG ĐỦ CỔNG
```

### ❌ Cooler Too Tall
```
Air Cooler: 170mm but Case max height: 155mm
→ KHÔNG VỪA
```

---

## 📝 Notes

- **TDP Calculations:** Luôn để buffer 20% cho PSU
- **PCIe Backward Compatibility:** PCIe cao hơn luôn tương thích với thấp hơn
- **Case Size Compatibility:** Case lớn hơn chứa được board nhỏ hơn
- **Optional Fields:** Một số field có thể null, cần check null trước khi so sánh
- **Multi-Component:** Cần tính tổng số lượng khi check slots (multiple SSDs, HDDs, RAMs)

---

**File này sẽ được sử dụng làm reference khi phát triển Compatibility Checker Service**

_Cập nhật lần cuối: 21/02/2026_

