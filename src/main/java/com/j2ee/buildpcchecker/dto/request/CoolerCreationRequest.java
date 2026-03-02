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
public class CoolerCreationRequest {

    @NotBlank(message = "COOLER_NAME_REQUIRED")
    String name;

    @NotBlank(message = "COOLER_TYPE_ID_REQUIRED")
    String coolerTypeId; // AIR / AIO

    Integer radiatorSize; // 120 / 240 / 360 (nullable cho tản khí)

    Integer heightMm; // chiều cao tản khí

    @NotNull(message = "COOLER_TDP_SUPPORT_REQUIRED")
    Integer tdpSupport; // W

    String imageUrl;

    String description;
}
