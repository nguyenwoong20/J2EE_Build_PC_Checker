package com.j2ee.buildpcchecker.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GameResponse {

    String id;
    String name;
    String genre;
    String coverImageUrl;
    String description;
    Integer releaseYear;

    Integer minCpuScore;
    Integer minGpuScore;
    Integer recCpuScore;
    Integer recGpuScore;

    Integer minRamGb;
    Integer recRamGb;

    Integer minStorageGb;
    Integer minVramGb;
    Integer recStorageGb;
    Integer recVramGb;

    Integer baseFpsLow;
    Integer baseFpsMedium;
    Integer baseFpsHigh;
}

