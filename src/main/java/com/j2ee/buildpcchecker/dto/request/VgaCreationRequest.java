package com.j2ee.buildpcchecker.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VgaCreationRequest {

    @NotBlank(message = "VGA_NAME_REQUIRED")
    String name;

    @NotNull(message = "VGA_LENGTH_REQUIRED")
    Integer lengthMm;

    @NotNull(message = "VGA_TDP_REQUIRED")
    Integer tdp;

    @NotBlank(message = "VGA_PCIE_VERSION_ID_REQUIRED")
    String pcieVersionId;

    String powerConnectorId; // Optional: 6pin, 8pin, 12VHPWR, etc.

    @NotNull(message = "VGA_SCORE_REQUIRED")
    Integer score;

    String description;
}

