package com.j2ee.buildpcchecker.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PsuUpdateRequest {

    String name;
    Integer wattage;
    String efficiency;
    Set<String> pcieConnectorIds;
    Integer sataConnector;
    String description;
}
