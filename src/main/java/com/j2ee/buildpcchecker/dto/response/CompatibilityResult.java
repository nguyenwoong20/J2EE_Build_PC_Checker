package com.j2ee.buildpcchecker.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Schema(description = "Result of compatibility check with errors, warnings, and PSU recommendation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompatibilityResult {

    @Schema(description = "Overall compatibility status (true if no critical errors)", example = "true")
    boolean compatible;

    @Schema(description = "List of critical errors that prevent the build from working",
            example = "[\"CPU socket 'LGA1700' không khớp với Mainboard socket 'AM5'\"]")
    @Builder.Default
    List<String> errors = new ArrayList<>();

    @Schema(description = "List of warnings for non-critical issues and optimization suggestions",
            example = "[\"Chỉ có 1 thanh RAM - Khuyến nghị dùng 2 thanh để kích hoạt Dual Channel\"]")
    @Builder.Default
    List<String> warnings = new ArrayList<>();

    @Schema(description = "Recommended PSU wattage in Watts (calculated as Total TDP × 1.2)",
            example = "650")
    Integer recommendedPsuWattage;

    // Helper methods
    public void addError(String message) {
        this.errors.add(message);
    }

    public void addWarning(String message) {
        this.warnings.add(message);
    }
}


