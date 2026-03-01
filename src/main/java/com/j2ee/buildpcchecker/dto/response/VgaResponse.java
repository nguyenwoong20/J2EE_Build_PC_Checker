package com.j2ee.buildpcchecker.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VgaResponse {

    String id;
    String name;
    Integer lengthMm;
    Integer tdp;
    PcieVersionResponse pcieVersion;
    PcieConnectorResponse powerConnector;
    Integer score;
    String description;
}

