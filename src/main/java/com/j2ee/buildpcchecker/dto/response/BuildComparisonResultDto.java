package com.j2ee.buildpcchecker.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BuildComparisonResultDto {
    ComponentComparison<ComponentInfo> cpu;
    ComponentComparison<GpuComparisonInfo> gpu;
    ComponentComparison<RamComparisonInfo> ram;
    ComponentComparison<StorageComparisonInfo> storage;
    ComponentComparison<PsuComparisonInfo> psu;
    String overallWinner;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GpuComparisonInfo {
        String name;
        int score;
        int vramGb;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RamComparisonInfo {
        String name;
        int totalGb;
        int bus;
        int cas;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StorageComparisonInfo {
        String name;
        String type; // NVME, SATA, HDD
        int totalCapacity;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PsuComparisonInfo {
        String name;
        int recommendedWattage;
        int actualWattage;
        boolean isSafe;
    }
}
