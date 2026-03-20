package com.j2ee.buildpcchecker.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FpsEstimateResponse {

    GameInfo game;
    PcSummary pcSummary;
    String resolution;
    FpsEstimates fpsEstimates;
    String upgradeAdvice;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class GameInfo {
        String id;
        String name;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class PcSummary {
        String cpuName;
        Integer cpuScore;
        String gpuName;
        Integer gpuScore;
        String limitingComponent; // CPU | GPU
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class FpsEstimates {
        SettingEstimate low;
        SettingEstimate medium;
        SettingEstimate high;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class SettingEstimate {
        Integer estimatedFps;
        String verdict;
        String message;
    }
}

