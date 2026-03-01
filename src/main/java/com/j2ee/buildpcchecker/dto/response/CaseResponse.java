package com.j2ee.buildpcchecker.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CaseResponse {

    String id;
    String name;
    CaseSizeResponse size;
    Integer maxVgaLengthMm;
    Integer maxCoolerHeightMm;
    Integer maxRadiatorSize;
    Integer drive35Slot;
    Integer drive25Slot;
    String description;
}
