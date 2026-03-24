package com.j2ee.buildpcchecker.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BuildPerformanceDto {
    double totalScore;
    String tier;
    Map<String, Double> breakdown;
    Map<String, Double> radarChartData;
    List<GameCompatibilityResult> gameCompatibility;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GameCompatibilityResult {
        String gameName;
        boolean isPlayable;
        String status; // e.g., "Ready", "Requires GPU upgrade", etc.
    }
}
