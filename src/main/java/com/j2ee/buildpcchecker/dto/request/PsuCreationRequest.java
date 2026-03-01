package com.j2ee.buildpcchecker.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PsuCreationRequest {

    @NotBlank(message = "PSU_NAME_REQUIRED")
    String name;

    @NotNull(message = "PSU_WATTAGE_REQUIRED")
    Integer wattage; // W

    @NotBlank(message = "PSU_EFFICIENCY_REQUIRED")
    String efficiency; // 80+ Bronze / Gold / Platinum

    Set<String> pcieConnectorIds; // Set of connector IDs: 2X8PIN, 3X8PIN, 12VHPWR

    @NotNull(message = "PSU_SATA_CONNECTOR_REQUIRED")
    Integer sataConnector; // số đầu SATA

    String description;
}
