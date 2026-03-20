package com.j2ee.buildpcchecker.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GameCompatibilityRequest {

    @NotBlank(message = "CPU_ID_REQUIRED")
    String cpuId;

    @NotBlank(message = "GPU_ID_REQUIRED")
    String vgaId;

    String ramId; // optional
}

