package com.j2ee.buildpcchecker.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FpsEstimateRequest {

    @NotBlank(message = "CPU_ID_REQUIRED")
    String cpuId;

    @NotBlank(message = "GPU_ID_REQUIRED")
    String vgaId;

    @NotBlank(message = "RESOLUTION_REQUIRED")
    @Pattern(regexp = "^(1080p|2k|4k)$", flags = Pattern.Flag.CASE_INSENSITIVE, message = "RESOLUTION_INVALID")
    String resolution; // 1080p | 2k | 4k
}

