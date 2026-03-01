package com.j2ee.buildpcchecker.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PsuResponse {

    String id;
    String name;
    Integer wattage;
    String efficiency;
    Set<PcieConnectorResponse> pcieConnectors;
    Integer sataConnector;
    String description;
}
