package com.j2ee.buildpcchecker.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GameCreationRequest {

    @NotBlank(message = "GAME_NAME_REQUIRED")
    String name;

    String genre;

    String coverImageUrl;
    String description;
    Integer releaseYear;

    @Positive(message = "GAME_MIN_CPU_SCORE_INVALID")
    Integer minCpuScore;

    @Positive(message = "GAME_MIN_GPU_SCORE_INVALID")
    Integer minGpuScore;

    @Positive(message = "GAME_REC_CPU_SCORE_INVALID")
    Integer recCpuScore;

    @Positive(message = "GAME_REC_GPU_SCORE_INVALID")
    Integer recGpuScore;

    @Positive(message = "GAME_MIN_RAM_GB_INVALID")
    Integer minRamGb;

    @Positive(message = "GAME_REC_RAM_GB_INVALID")
    Integer recRamGb;

    @Positive(message = "GAME_MIN_STORAGE_GB_INVALID")
    Integer minStorageGb;
    @Positive(message = "GAME_MIN_VRAM_GB_INVALID")
    Integer minVramGb;
    @Positive(message = "GAME_REC_STORAGE_GB_INVALID")
    Integer recStorageGb;
    @Positive(message = "GAME_REC_VRAM_GB_INVALID")
    Integer recVramGb;

    @Positive(message = "GAME_BASE_FPS_LOW_INVALID")
    Integer baseFpsLow;

    @Positive(message = "GAME_BASE_FPS_MEDIUM_INVALID")
    Integer baseFpsMedium;

    @Positive(message = "GAME_BASE_FPS_HIGH_INVALID")
    Integer baseFpsHigh;
}

