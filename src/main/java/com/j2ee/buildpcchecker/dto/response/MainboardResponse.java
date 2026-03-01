package com.j2ee.buildpcchecker.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MainboardResponse {

    String id;
    String name;
    SocketResponse socket;
    Integer vrmPhase;
    Integer cpuTdpSupport;
    RamTypeResponse ramType;
    Integer ramBusMax;
    Integer ramSlot;
    Integer ramMaxCapacity;
    CaseSizeResponse size;
    PcieVersionResponse pcieVgaVersion;
    Integer m2Slot;
    Integer sataSlot;
    String description;
}

