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
public class RamCreationRequest {

    @NotBlank(message = "RAM_NAME_REQUIRED")
    String name;

    @NotBlank(message = "RAM_TYPE_ID_REQUIRED")
    String ramTypeId;

    @NotNull(message = "RAM_BUS_REQUIRED")
    Integer ramBus;

    @NotNull(message = "RAM_CAS_REQUIRED")
    Integer ramCas;

    @NotNull(message = "RAM_CAPACITY_PER_STICK_REQUIRED")
    Integer capacityPerStick;

    @NotNull(message = "RAM_QUANTITY_REQUIRED")
    Integer quantity;

    @NotNull(message = "RAM_TDP_REQUIRED")
    Integer tdp;

    String imageUrl;

    String description;
}

