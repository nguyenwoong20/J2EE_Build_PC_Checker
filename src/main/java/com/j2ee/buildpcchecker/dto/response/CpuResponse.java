package com.j2ee.buildpcchecker.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CpuResponse {

    String id;
    String name;
    SocketResponse socket;
    Integer vrmMin;
    Boolean igpu;
    Integer tdp;
    PcieVersionResponse pcieVersion;
    Integer score;
    String imageUrl;
    String description;
}

