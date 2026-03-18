# InterfaceType API Examples

## Base URL
```
http://localhost:8080/interface-types
```

## 1. Create Interface Type
**POST** `/interface-types`

```json
{
  "id": "SATA_3",
  "name": "SATA III"
}
```

### Response
```json
{
  "code": 1000,
  "result": {
    "id": "SATA_3",
    "name": "SATA III"
  }
}
```

## 2. Get All Interface Types
**GET** `/interface-types`

### Response
```json
{
  "code": 1000,
  "result": [
    {
      "id": "SATA_3",
      "name": "SATA III"
    },
    {
      "id": "SAS",
      "name": "SAS"
    },
    {
      "id": "PCIE_3",
      "name": "PCIe 3.0 x4"
    },
    {
      "id": "PCIE_4",
      "name": "PCIe 4.0 x4"
    },
    {
      "id": "PCIE_5",
      "name": "PCIe 5.0 x4"
    }
  ]
}
```

## 3. Get Interface Type by ID
**GET** `/interface-types/{id}`

Example: `GET /interface-types/SATA_3`

### Response
```json
{
  "code": 1000,
  "result": {
    "id": "SATA_3",
    "name": "SATA III"
  }
}
```

## 4. Update Interface Type
**PUT** `/interface-types/{id}`

Example: `PUT /interface-types/SATA_3`

```json
{
  "id": "SATA_3",
  "name": "SATA III (6 Gb/s)"
}
```

### Response
```json
{
  "code": 1000,
  "result": {
    "id": "SATA_3",
    "name": "SATA III (6 Gb/s)"
  }
}
```

## 5. Delete Interface Type
**DELETE** `/interface-types/{id}`

Example: `DELETE /interface-types/SATA_3`

### Response
```json
{
  "code": 1000,
  "result": "Interface Type deleted successfully"
}
```

---

## Common Interface Type IDs

### For HDD
- `SATA_3` - SATA III (6 Gb/s)
- `SAS` - SAS (Serial Attached SCSI)

### For SSD
- `SATA_3` - SATA III (6 Gb/s) - for 2.5" SSDs
- `PCIE_3` - PCIe 3.0 x4 - for M.2 NVMe SSDs
- `PCIE_4` - PCIe 4.0 x4 - for M.2 NVMe SSDs
- `PCIE_5` - PCIe 5.0 x4 - for M.2 NVMe SSDs

---

## Updated HDD API

### Create HDD (Updated)
**POST** `/hdds`

```json
{
  "name": "Seagate Barracuda 2TB",
  "formFactor": "3.5\"",
  "interfaceTypeId": "SATA_3",  // Changed from hddInterfaceId
  "capacity": 2000,
  "tdp": 6,
  "description": "7200 RPM, 256MB Cache"
}
```

---

## Updated SSD API

### Create SSD (Updated)
**POST** `/ssds`

```json
{
  "name": "Samsung 980 PRO 1TB",
  "ssdTypeId": "NVME",
  "formFactor": "M.2 2280",
  "interfaceTypeId": "PCIE_4",  // Changed from ssdInterfaceId
  "capacity": 1000,
  "tdp": 6,
  "description": "Read: 7000 MB/s, Write: 5000 MB/s"
}
```

---

## Error Codes

| Code | Message | HTTP Status |
|------|---------|-------------|
| 3121 | Interface Type ID is required | 400 |
| 3122 | Interface Type name is required | 400 |
| 3123 | Interface Type not found | 404 |

