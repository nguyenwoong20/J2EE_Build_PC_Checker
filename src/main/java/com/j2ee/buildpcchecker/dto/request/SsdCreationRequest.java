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
public class SsdCreationRequest {

    @NotBlank(message = "SSD_NAME_REQUIRED")
    String name;

    @NotBlank(message = "SSD_TYPE_ID_REQUIRED")
    String ssdTypeId; // SATA / NVME

    @NotBlank(message = "FORM_FACTOR_ID_REQUIRED")
    String formFactorId; // FF_2_5 / M2_2280

    @NotBlank(message = "INTERFACE_TYPE_ID_REQUIRED")
    String interfaceTypeId; // SATA_3 / PCIE_4 / PCIE_5

    @NotNull(message = "SSD_CAPACITY_REQUIRED")
    Integer capacity; // GB

    @NotNull(message = "SSD_TDP_REQUIRED")
    Integer tdp; // W

    String imageUrl;

    String description;
}
