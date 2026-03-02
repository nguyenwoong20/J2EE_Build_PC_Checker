package com.j2ee.buildpcchecker.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CpuUpdateRequest {

    String name;
    String socketId;
    Integer vrmMin;
    Boolean igpu;
    Integer tdp;
    String pcieVersionId;
    Integer score;
    String imageUrl;
    String description;
}

