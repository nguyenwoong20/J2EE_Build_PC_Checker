package com.j2ee.buildpcchecker.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "game")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(nullable = false, unique = true)
    String name;

    String genre;

    @Column(name = "cover_image_url", columnDefinition = "TEXT")
    String coverImageUrl;

    @Column(columnDefinition = "TEXT")
    String description;

    @Column(name = "release_year")
    Integer releaseYear;

    @Column(name = "min_cpu_score")
    Integer minCpuScore;

    @Column(name = "min_gpu_score")
    Integer minGpuScore;

    @Column(name = "rec_cpu_score")
    Integer recCpuScore;

    @Column(name = "rec_gpu_score")
    Integer recGpuScore;

    @Column(name = "min_ram_gb")
    Integer minRamGb;

    @Column(name = "rec_ram_gb")
    Integer recRamGb;

    @Column(name = "min_storage_gb")
    Integer minStorageGb;

    @Column(name = "min_vram_gb")
    Integer minVramGb;

    @Column(name = "rec_storage_gb")
    Integer recStorageGb;

    @Column(name = "rec_vram_gb")
    Integer recVramGb;

    @Column(name = "base_fps_low")
    Integer baseFpsLow;

    @Column(name = "base_fps_medium")
    Integer baseFpsMedium;

    @Column(name = "base_fps_high")
    Integer baseFpsHigh;
}

