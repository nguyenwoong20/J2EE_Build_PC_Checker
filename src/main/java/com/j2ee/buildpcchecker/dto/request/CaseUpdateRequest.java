package com.j2ee.buildpcchecker.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CaseUpdateRequest {

    String name;
    String sizeId;
    Integer maxVgaLengthMm;
    Integer maxCoolerHeightMm;
    Integer maxRadiatorSize;
    Integer drive35Slot;
    Integer drive25Slot;
    String description;
}
