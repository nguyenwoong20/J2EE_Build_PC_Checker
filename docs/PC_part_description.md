# Thiết kế Database (Mô phỏng)
## Website hỗ trợ Build PC & Kiểm tra tương thích linh kiện

> Dữ liệu trong hệ thống là dữ liệu mô phỏng, được xây dựng nhằm phục vụ kiểm tra tương thích và tối ưu cấu hình PC (bottleneck), không nhằm mục đích thương mại.

---

## 1. CPU

```sql
CPU (
    id              VARCHAR PK,      -- UUID
    name            VARCHAR,
    socket          VARCHAR,         -- AM4, AM5, LGA1700...
    vrm_min         INT,             -- số phase VRM tối thiểu (tham khảo)
    igpu            BOOLEAN,         -- có iGPU hay không
    tdp             INT,             -- W
    pcie_version    VARCHAR,         -- PCIe 4.0 / 5.0
    score           INT,             -- điểm hiệu năng tổng
    description     TEXT
)
Mục đích sử dụng
CPU – Mainboard: kiểm tra Socket, VRM, TDP

CPU – VGA: kiểm tra bottleneck, khả năng xuất hình

Chuẩn bị cho kiểm tra PSU sau này

2. Mainboard
MAINBOARD (
    id                  VARCHAR PK,      -- UUID
    name                VARCHAR,
    socket              VARCHAR,
    vrm_phase           INT,             -- số phase chính
    cpu_tdp_support     INT,             -- W
    
    ram_type            VARCHAR,         -- DDR4 / DDR5
    ram_bus_max         INT,             -- MHz
    ram_slot            INT,
    ram_max_capacity    INT,             -- GB
    
    size                VARCHAR,         -- ATX / mATX / ITX
    
    pcie_vga_version    VARCHAR,         -- PCIe 4.0 / 5.0
    
    m2_slot             INT,
    sata_slot           INT,
    
    description         TEXT
)
Mục đích sử dụng
CPU – Mainboard: kiểm tra Socket, VRM, TDP

Mainboard – RAM: kiểm tra loại RAM, buss, số khe, dung lượng tối đa

Mainboard – VGA: kiểm tra giới hạn PCIe

3. RAM
RAM (
    id                  VARCHAR PK,      -- UUID
    name                VARCHAR,
    ram_type            VARCHAR,         -- DDR4 / DDR5
    ram_bus             INT,             -- MHz
    ram_cas             INT,             -- CL
    capacity_per_stick  INT,             -- GB
    quantity            INT,             -- số thanh
    tdp                 INT,
    description         TEXT
)
Mục đích sử dụng
Kiểm tra số khe RAM

Tính tổng dung lượng RAM

Phục vụ cảnh báo dual / quad channel (mở rộng)

4. VGA (GPU)
VGA (
    id                  VARCHAR PK,      -- UUID
    name                VARCHAR,
    length_mm           INT,             -- mm
    tdp                 INT,             -- W
    pcie_version        VARCHAR,         -- PCIe 3.0 / 4.0
    power_connector     VARCHAR,         -- 1x8pin, 2x8pin, 12VHPWR
    score               INT,             -- điểm hiệu năng
    description         TEXT
)
Mục đích sử dụng
CPU – VGA: kiểm tra nghẽn cổ chai (bottleneck)

Mainboard – VGA: kiểm tra giới hạn PCIe

Chuẩn bị cho kiểm tra PSU và CASE

5. Logic kiểm tra tương thích & tối ưu
5.1 CPU – Mainboard
cpu.socket == main.socket
main.vrm_phase >= cpu.vrm_min
cpu.tdp <= main.cpu_tdp_support
Nếu TDP CPU vượt quá khả năng main → ⚠️ không tối ưu

5.2 Mainboard – RAM
ram.ram_type == main.ram_type
ram.quantity <= main.ram_slot
ram.ram_bus <= main.ram_bus_max
(ram.capacity_per_stick * ram.quantity) <= main.ram_max_capacity
5.3 CPU – VGA
Nếu không chọn VGA và cpu.igpu = false → ❌ không thể xuất hình
So sánh cpu.score và vga.score
Nếu chênh lệch > ~40% → ⚠️ bottleneck
5.4 Mainboard – VGA
Nếu vga.pcie_version > main.pcie_vga_version
→ ⚠️ VGA bị giới hạn băng thông