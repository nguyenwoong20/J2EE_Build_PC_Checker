package com.j2ee.buildpcchecker.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VgaUpdateRequest {

    String name;
    Integer lengthMm;
    Integer tdp;
    String pcieVersionId;
    String powerConnectorId;
    Integer score;
    String description;
}

