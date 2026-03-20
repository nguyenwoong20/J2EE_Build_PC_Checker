package com.j2ee.buildpcchecker.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GameCompatListResponse {

    PcSummary pcSummary;
    List<GameCompatItem> results;

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
        Integer totalRamGb;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class GameCompatItem {
        String gameId;
        String gameName;
        String genre;
        String coverImageUrl;
        String status;   // RECOMMENDED | MINIMUM | NOT_SUPPORTED
        String detail;
    }
}
