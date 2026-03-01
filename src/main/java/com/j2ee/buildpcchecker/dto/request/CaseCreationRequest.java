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
public class CaseCreationRequest {

    @NotBlank(message = "CASE_NAME_REQUIRED")
    String name;

    @NotBlank(message = "CASE_SIZE_REQUIRED")
    String sizeId;

    @NotNull(message = "CASE_MAX_VGA_LENGTH_REQUIRED")
    Integer maxVgaLengthMm;

    @NotNull(message = "CASE_MAX_COOLER_HEIGHT_REQUIRED")
    Integer maxCoolerHeightMm; // mm

    @NotNull(message = "CASE_MAX_RADIATOR_SIZE_REQUIRED")
    Integer maxRadiatorSize; // 120 / 240 / 360

    @NotNull(message = "CASE_DRIVE_35_SLOT_REQUIRED")
    Integer drive35Slot; // số ổ 3.5"

    @NotNull(message = "CASE_DRIVE_25_SLOT_REQUIRED")
    Integer drive25Slot; // số ổ 2.5"

    String description;
}
