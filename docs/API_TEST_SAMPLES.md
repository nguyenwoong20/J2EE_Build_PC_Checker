# Sample API Request for Testing

## 1. Create Socket First
### POST /sockets
```json
{
  "id": "AM5",
  "name": "Socket AM5"
}
```

```json
{
  "id": "LGA1700",
  "name": "Socket LGA1700"
}
```

## 2. Create PCIe Version
### POST /pcie-versions
```json
{
  "id": "PCIE_4",
  "name": "PCIe 4.0"
}
```

```json
{
  "id": "PCIE_5",
  "name": "PCIe 5.0"
}
```

## 3. Create CPU
### POST /cpus
```json
{
  "name": "AMD Ryzen 9 7950X",
  "socketId": "AM5",
  "vrmMin": 12,
  "igpu": true,
  "tdp": 170,
  "pcieVersionId": "PCIE_5",
  "score": 95,
  "description": "16-core 32-thread processor"
}
```

```json
{
  "name": "Intel Core i9-14900K",
  "socketId": "LGA1700",
  "vrmMin": 14,
  "igpu": true,
  "tdp": 253,
  "pcieVersionId": "PCIE_5",
  "score": 93,
  "description": "24-core processor (8P+16E cores)"
}
```

## 4. Create RAM Type
### POST /ram-types
```json
{
  "id": "DDR5",
  "name": "DDR5"
}
```

```json
{
  "id": "DDR4",
  "name": "DDR4"
}
```

## Common Errors and Solutions

### Error: socketId or pcieVersionId is null
**Solution:** Make sure you create Socket and PcieVersion first before creating CPU

### Error: Validation failed
**Solution:** Ensure all required fields are provided:
- name (String, required)
- socketId (String, required) 
- igpu (Boolean, required)
- tdp (Integer, required)
- pcieVersionId (String, required)
- score (Integer, required)
- vrmMin (Integer, optional)
- description (String, optional)

