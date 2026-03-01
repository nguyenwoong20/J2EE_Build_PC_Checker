package com.j2ee.buildpcchecker.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MainboardUpdateRequest {

    String name;
    String socketId;
    Integer vrmPhase;
    Integer cpuTdpSupport;
    String ramTypeId;
    Integer ramBusMax;
    Integer ramSlot;
    Integer ramMaxCapacity;
    String sizeId;
    String pcieVgaVersionId;
    Integer m2Slot;
    Integer sataSlot;
    String description;
}

