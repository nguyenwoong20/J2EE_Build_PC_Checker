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
public class CpuCreationRequest {

    @NotBlank(message = "CPU_NAME_REQUIRED")
    String name;

    @NotBlank(message = "CPU_SOCKET_ID_REQUIRED")
    String socketId;

    Integer vrmMin;

    @NotNull(message = "CPU_IGPU_REQUIRED")
    Boolean igpu;

    @NotNull(message = "CPU_TDP_REQUIRED")
    Integer tdp;

    @NotBlank(message = "CPU_PCIE_VERSION_ID_REQUIRED")
    String pcieVersionId;

    @NotNull(message = "CPU_SCORE_REQUIRED")
    Integer score;

    String imageUrl;

    String description;
}
