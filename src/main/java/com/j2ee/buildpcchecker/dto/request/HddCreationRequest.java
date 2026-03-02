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
public class HddCreationRequest {

    @NotBlank(message = "HDD_NAME_REQUIRED")
    String name;

    @NotBlank(message = "FORM_FACTOR_ID_REQUIRED")
    String formFactorId; // FF_2_5 / FF_3_5

    @NotBlank(message = "INTERFACE_TYPE_ID_REQUIRED")
    String interfaceTypeId; // SATA_3 / SAS

    @NotNull(message = "HDD_CAPACITY_REQUIRED")
    Integer capacity; // GB

    @NotNull(message = "HDD_TDP_REQUIRED")
    Integer tdp; // W

    String imageUrl;

    String description;
}
