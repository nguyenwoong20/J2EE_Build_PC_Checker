Các Entity: SSD, HDD, Case, PSU, Cooler

SSD (
    id              VARCHAR PRIMARY KEY,
    name            VARCHAR,

    type            VARCHAR,        -- SATA / NVME
    form_factor     VARCHAR,        -- 2.5", M.2 2280
    interface       VARCHAR,        -- SATA III / PCIe 3.0 / PCIe 4.0
    capacity        INT,            -- GB

    read_speed      INT,            -- MB/s (mô phỏng)
    write_speed     INT,            -- MB/s (mô phỏng)

    tdp             INT,            -- W (rất thấp nhưng có ích cho PSU)
    description     TEXT
)

HDD (
    id              VARCHAR PRIMARY KEY,
    name            VARCHAR,

    form_factor     VARCHAR,        -- 3.5" / 2.5"
    interface       VARCHAR,        -- SATA III
    capacity        INT,            -- GB

    rpm             INT,            -- 5400 / 7200
    cache_mb        INT,            -- MB

    tdp             INT,            -- W
    description     TEXT
)

PSU (
    id              VARCHAR PRIMARY KEY,
    name            VARCHAR,

    wattage         INT,            -- W
    efficiency      VARCHAR,        -- 80+ Bronze / Gold / Platinum
    modular_type    VARCHAR,        -- Non / Semi / Full Modular

    pcie_connector  VARCHAR,        -- 2x8pin, 3x8pin, 12VHPWR
    sata_connector  INT,            -- số đầu SATA

    description     TEXT
)

CASE (
    id                  VARCHAR PRIMARY KEY,
    name                VARCHAR,

    size                VARCHAR,    -- ATX / mATX / ITX
    max_vga_length_mm   INT,
    max_cooler_height   INT,        -- mm (cho tản khí)
    max_radiator_size   INT,        -- 120 / 240 / 360

    drive_35_slot       INT,        -- số ổ 3.5"
    drive_25_slot       INT,        -- số ổ 2.5"

    description         TEXT
)

COOLER (
    id              VARCHAR PRIMARY KEY,
    name            VARCHAR,

    type            VARCHAR,        -- AIR / AIO
    radiator_size   INT,            -- 120 / 240 / 360 (nullable)
    height_mm       INT,            -- tản khí

    tdp_support     INT,            -- W
    description     TEXT
)
