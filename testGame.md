## Tính năng 3 — Ước tính FPS theo từng game (Low / Medium / High)

### Vấn đề cần giải quyết
Check "chạy được hay không" chưa đủ thực tế. Người dùng muốn biết cụ thể sẽ chạy được bao nhiêu FPS để quyết định cấu hình.

### Quan điểm thiết kế

**Không nên dùng AI để tính FPS** (phức tạp, không ổn định). Thay vào đó, dùng công thức **scale tuyến tính dựa trên PassMark score**, tương tự cách hệ thống Bottleneck đang làm.

> **Nguyên lý cốt lõi:** Game nhà phát triển thường công bố FPS tham chiếu đi kèm với yêu cầu cấu hình (min/rec). Ta dùng score ratio của PC người dùng so với score tham chiếu để scale FPS ước tính.

#### Thêm vào entity `Game` — FPS Baseline

```java
// FPS baseline theo mức setting (ở 1080p, với cấu hình recommended)
@Column(name = "base_fps_low")
Integer baseFpsLow;         // VD: 30 FPS với Low setting + min config

@Column(name = "base_fps_medium")
Integer baseFpsMedium;      // VD: 60 FPS với Medium setting + rec config

@Column(name = "base_fps_high")
Integer baseFpsHigh;        // VD: 60+ FPS với High setting + rec config
```

> Admin nhập FPS baseline theo thông số benchmark thực tế (Digital Foundry, TechPowerUp, GameBench...).

#### Công thức tính FPS ước tính

```
// Bottleneck: lấy giá trị nhỏ hơn để xác định giới hạn thực tế
limitingScore = min(cpuScore, gpuScore)

// Score tham chiếu từ game (mức recommended)
referenceScore = min(recCpuScore, recGpuScore)

// Ratio hiệu năng
ratio = limitingScore / referenceScore

// FPS ước tính theo từng mức setting
estimatedFpsLow    = baseFpsLow    × ratio × settingWeight[LOW]
estimatedFpsMedium = baseFpsMedium × ratio × settingWeight[MEDIUM]
estimatedFpsHigh   = baseFpsHigh   × ratio × settingWeight[HIGH]

// Setting weights (setting càng cao, GPU càng nặng hơn)
settingWeight = { LOW: 1.4, MEDIUM: 1.0, HIGH: 0.65 }

// Cap thực tế: FPS không nên vượt quá 2× baseline
estimatedFps = min(estimatedFps, baseline × 2)
```

#### API Endpoint

```
POST /games/{gameId}/estimate-fps
```

**Request Body:**
```json
{
  "cpuId": "uuid",
  "vgaId": "uuid",
  "resolution": "1080p"   // 1080p | 2k | 4k
}
```

**Response:**
```json
{
  "game": {
    "id": "uuid",
    "name": "Cyberpunk 2077"
  },
  "pcSummary": {
    "cpuName": "Ryzen 5 5600",
    "cpuScore": 22000,
    "gpuName": "RTX 3060",
    "gpuScore": 16747,
    "limitingComponent": "GPU"
  },
  "resolution": "1080p",
  "fpsEstimates": {
    "low": {
      "estimatedFps": 95,
      "verdict": "EXCELLENT",
      "message": "Rất mượt mà ở setting thấp. Phù hợp để ưu tiên FPS cao."
    },
    "medium": {
      "estimatedFps": 58,
      "verdict": "PLAYABLE",
      "message": "Chơi được tốt ở medium. Có thể cần tắt Ray Tracing."
    },
    "high": {
      "estimatedFps": 35,
      "verdict": "BELOW_TARGET",
      "message": "Dưới 60 FPS ở high setting. Khuyến nghị nâng GPU hoặc giảm setting."
    }
  },
  "upgradeAdvice": "GPU RTX 3060 là điểm giới hạn chính. Nâng lên RTX 3070 có thể cải thiện ~40% FPS."
}
```

#### Ngưỡng FPS Verdict

| FPS | Verdict |
|-----|---------|
| ≥ 120 | `ULTRA_SMOOTH` |
| ≥ 60 | `EXCELLENT` |
| ≥ 45 | `PLAYABLE` |
| ≥ 30 | `BELOW_TARGET` |
| < 30 | `UNPLAYABLE` |

#### Resolution Weight (tái sử dụng từ Bottleneck Checker)

| Resolution | Weight |
|------------|--------|
| 1080p | 1.2 |
| 2K | 1.0 |
| 4K | 0.8 |

→ FPS ước tính sẽ tự động giảm khi người dùng chọn độ phân giải cao hơn.

---

## Kiến trúc tổng thể sau khi thêm 3 tính năng

```
Game Management (CRUD)
   ↓
Game Database (name, genre, minScore, recScore, baseFps...)
   ↓
Game Compatibility Checker ←── PC Build (cpuScore + gpuScore + ramGb)
   ↓                                   ↓
 GameCompatibilityService        FPS Estimator Service
   ↓                                   ↓
 results: [RECOMMENDED,          fpsEstimates: {low, medium, high}
           MINIMUM,              verdict + upgradeAdvice
           NOT_SUPPORTED]
```

---

## Thứ tự triển khai đề xuất

| Bước | Nội dung | Lý do |
|------|----------|-------|
| **1** | Tạo entity `Game` + CRUD API | Nền tảng cho 2 tính năng còn lại |
| **2** | Nhập dữ liệu game mẫu (10–20 game phổ biến) | Cần data để test |
| **3** | Implement `GameCompatibilityService` + endpoint | Tái sử dụng score từ entities hiện có |
| **4** | Implement `FpsEstimatorService` + endpoint | Phụ thuộc dữ liệu game đã có |
| **5** | Swagger documentation | Documentation cho frontend |

---

## Điểm cần làm rõ trước khi implement

1. **Nguồn FPS Baseline:** Admin sẽ tự nhập dữ liệu FPS. Nguồn tham khảo: Digital Foundry, TechPowerUp, hoặc benchmark cộng đồng như GameGPU.ru và Techtesters.eu.

2. **RAM entity:** Cần kiểm tra xem `Ram.java` có field `capacityGb` không — nếu chưa cần thêm để tính tổng RAM cho compatibility check.

3. **VRAM:** Entity `Vga.java` hiện không có field `vramGb`. Cần thêm nếu muốn check VRAM requirement của game (một số game AAA yêu cầu 8–12GB VRAM).

4. **Game data management:** Admin nhập tay hay có tool bulk import? Nếu game library lớn (>100 game) nên xem xét import từ CSV/JSON.

5. **Phạm vi "phần mềm":** Tính năng 2 đang hướng tới game. Nếu mở rộng sang phần mềm chuyên dụng (Adobe Premiere, Blender, v.v.), cần định nghĩa lại metric (render score thay vì FPS).

---

*Tài liệu này chỉ mang tính hướng dẫn thiết kế. Mọi thay đổi về schema hoặc business logic cần được review trước khi implement.*
