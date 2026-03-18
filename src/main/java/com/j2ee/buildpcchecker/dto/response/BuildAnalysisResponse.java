package com.j2ee.buildpcchecker.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Build analysis result containing bottleneck info per resolution")
public class BuildAnalysisResponse {

    @Schema(description = "CPU info used in analysis")
    ComponentInfo cpu;

    @Schema(description = "GPU info used in analysis")
    ComponentInfo gpu;

    @Schema(description = "Bottleneck results keyed by resolution (1080p, 2k, 4k)")
    Map<String, ResolutionResult> results;
}

