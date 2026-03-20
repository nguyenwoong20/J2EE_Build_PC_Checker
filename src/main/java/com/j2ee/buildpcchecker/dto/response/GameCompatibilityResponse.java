package com.j2ee.buildpcchecker.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GameCompatibilityResponse {

    GameInfo game;
    PcSpecSummary pcSummary;

    String compatibility; // RECOMMENDED | MINIMUM | NOT_SUPPORTED
    String message;

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
    public static class PcSpecSummary {
        String cpuName;
        Integer cpuScore;
        String gpuName;
        Integer gpuScore;
        Integer ramGb;
    }
}

