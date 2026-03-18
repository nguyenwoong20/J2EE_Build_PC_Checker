package com.j2ee.buildpcchecker.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Bottleneck analysis result for a specific resolution")
public class ResolutionResult {

    @Schema(description = "Whether there is a bottleneck", example = "true")
    boolean bottleneck;

    @Schema(description = "Bottleneck type: CPU, GPU, or NONE", example = "CPU")
    String type;

    @Schema(description = "Bottleneck severity: NONE, LOW, MEDIUM, or HIGH", example = "MEDIUM")
    String severity;

    @Schema(description = "Adjusted ratio of CPU/GPU performance at this resolution", example = "0.62")
    double ratio;

    @Schema(description = "User-friendly message describing the bottleneck", example = "CPU i3-10100F có thể giới hạn hiệu năng RTX 3060 ở 1080p.")
    String message;
}
