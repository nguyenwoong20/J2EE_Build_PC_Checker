package com.j2ee.buildpcchecker.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Schema(description = "Request for checking PC build compatibility with selected components")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BuildCheckRequest {

    @Schema(description = "CPU ID (UUID)", example = "550e8400-e29b-41d4-a716-446655440000", nullable = true)
    String cpuId;

    @Schema(description = "Mainboard ID (UUID)", example = "550e8400-e29b-41d4-a716-446655440001", nullable = true)
    String mainboardId;

    @Schema(description = "RAM ID (UUID)", example = "550e8400-e29b-41d4-a716-446655440002", nullable = true)
    String ramId;

    @Schema(description = "VGA/Graphics Card ID (UUID)", example = "550e8400-e29b-41d4-a716-446655440003", nullable = true)
    String vgaId;

    @Schema(description = "List of SSD IDs (UUIDs)", example = "[\"550e8400-e29b-41d4-a716-446655440004\"]", nullable = true)
    List<String> ssdIds;

    @Schema(description = "List of HDD IDs (UUIDs)", example = "[\"550e8400-e29b-41d4-a716-446655440005\"]", nullable = true)
    List<String> hddIds;

    @Schema(description = "PSU/Power Supply ID (UUID)", example = "550e8400-e29b-41d4-a716-446655440006", nullable = true)
    String psuId;

    @Schema(description = "Case ID (UUID)", example = "550e8400-e29b-41d4-a716-446655440007", nullable = true)
    String caseId;

    @Schema(description = "Cooler ID (UUID)", example = "550e8400-e29b-41d4-a716-446655440008", nullable = true)
    String coolerId;
}


