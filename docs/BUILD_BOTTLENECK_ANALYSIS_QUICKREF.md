# Build Bottleneck Analysis - Quick Reference

## API Endpoint
```
POST /api/builds/analyze
```

## Request Format
```json
{
  "cpuId": "uuid",
  "gpuId": "uuid",
  "ramId": "uuid",
  "ssdId": "uuid"
}
```

## Response Format
```json
{
  "code": 1000,
  "message": "Success",
  "result": {
    "bottleneck": 12.3,
    "balanceStatus": "Good Balance",
    "estimatedWattage": 450
  }
}
```

## Bottleneck Formula
```
ratio = cpuScore / gpuScore
bottleneck = |1 - ratio| × 100
```

## Balance Status Ranges
| Bottleneck % | Status          |
|--------------|-----------------|
| 0-10         | Perfect Balance |
| 10-20        | Good Balance    |
| 20-30        | Acceptable      |
| >30          | High Bottleneck |

## Power Calculation
```
estimatedWattage = CPU_TDP + GPU_TDP + RAM_TDP + SSD_TDP + 50W
```

## Files Created
1. `dto/request/AnalyzeBuildRequest.java`
2. `dto/response/BuildAnalysisResponse.java`
3. `service/BuildAnalyzerService.java`
4. `controller/BuildController.java` (updated)
5. `exception/ErrorCode.java` (updated)

## cURL Example
```bash
curl -X POST http://localhost:8080/api/builds/analyze \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "cpuId": "cpu-uuid",
    "gpuId": "gpu-uuid",
    "ramId": "ram-uuid",
    "ssdId": "ssd-uuid"
  }'
```

## Build Status
✅ Compilation: SUCCESS  
✅ Dependencies: All present  
✅ Clean Architecture: Followed  
✅ Documentation: Complete

