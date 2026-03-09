package com.j2ee.buildpcchecker.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SaveBuildRequest {

    @NotBlank(message = "BUILD_NAME_REQUIRED")
    @Schema(description = "Build name", example = "Gaming Build 2026")
    String name;

    @Schema(description = "Build description", example = "Intel i9 + RTX 4090 Gaming Setup")
    String description;

    @Schema(
            description = "Map of part types to part IDs. Keys: cpu, mainboard, ram, gpu, psu, case, cooler, ssd, hdd",
            example = "{\"cpu\": \"uuid-of-cpu\", \"mainboard\": \"uuid-of-mainboard\", \"ram\": \"uuid-of-ram\", \"vga\": \"uuid-of-vga\", \"psu\": \"uuid-of-psu\"}"
    )
    Map<String, String> parts;  // key: partType (cpu, mainboard, ram, etc.), value: partId (UUID)
}



