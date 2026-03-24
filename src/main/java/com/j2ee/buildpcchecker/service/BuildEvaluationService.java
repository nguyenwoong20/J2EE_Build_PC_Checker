package com.j2ee.buildpcchecker.service;

import com.j2ee.buildpcchecker.dto.request.BuildCheckRequest;
import com.j2ee.buildpcchecker.dto.response.BuildPerformanceDto;
import com.j2ee.buildpcchecker.entity.*;
import com.j2ee.buildpcchecker.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BuildEvaluationService {

    private final CpuRepository cpuRepository;
    private final VgaRepository vgaRepository;
    private final RamRepository ramRepository;
    private final SsdRepository ssdRepository;
    private final HddRepository hddRepository;
    private final GameRepository gameRepository;

    public BuildPerformanceDto evaluateBuild(BuildCheckRequest request) {
        log.info("Evaluating PC build performance");

        Cpu cpu = request.getCpuId() != null ? cpuRepository.findById(request.getCpuId()).orElse(null) : null;
        Vga vga = request.getVgaId() != null ? vgaRepository.findById(request.getVgaId()).orElse(null) : null;
        Ram ram = request.getRamId() != null ? ramRepository.findById(request.getRamId()).orElse(null) : null;
        List<Ssd> ssds = request.getSsdIds() != null ? ssdRepository.findAllById(request.getSsdIds()) : Collections.emptyList();
        List<Hdd> hdds = request.getHddIds() != null ? hddRepository.findAllById(request.getHddIds()) : Collections.emptyList();

        double cpuScore = cpu != null ? cpu.getScore() : 0;
        double gpuScore = vga != null ? vga.getScore() : 0;
        
        // RAM Bonus
        double ramBonus = 0;
        int totalRamGb = ram != null ? ram.getCapacityPerStick() * ram.getQuantity() : 0;
        if (totalRamGb >= 16) ramBonus += 1000;
        else if (totalRamGb == 8) ramBonus -= 1000;
        
        if (totalRamGb >= 32) ramBonus += 500; // total +1500 for >=32GB

        if (ram != null && ram.getRamBus() > 3200) {
            cpuScore *= 1.05;
        }

        // Storage Bonus
        double storageBonus = 0;
        boolean hasNvme = ssds.stream().anyMatch(s -> s.getSsdType().getName().equalsIgnoreCase("NVME") || s.getName().toUpperCase().contains("NVME"));
        boolean hasSataSsd = ssds.stream().anyMatch(s -> s.getSsdType().getName().equalsIgnoreCase("SATA") || s.getName().toUpperCase().contains("SATA"));
        
        if (hasNvme) storageBonus += 1000;
        else if (hasSataSsd) storageBonus += 500;
        else if (!hdds.isEmpty()) storageBonus -= 1500;

        double totalScore = (cpuScore * 0.5) + (gpuScore * 0.5) + ramBonus + storageBonus;

        String tier = calculateTier(totalScore);

        Map<String, Double> breakdown = new HashMap<>();
        breakdown.put("cpuScore", cpuScore);
        breakdown.put("gpuScore", gpuScore);
        breakdown.put("ramBonus", ramBonus);
        breakdown.put("storageBonus", storageBonus);

        Map<String, Double> radarData = new HashMap<>();
        radarData.put("gaming", (gpuScore * 0.7 + cpuScore * 0.3) / 100);
        radarData.put("multitasking", (cpuScore * 0.6 + (double)totalRamGb * 100) / 100);
        radarData.put("storage", (double)(hasNvme ? 100 : (hasSataSsd ? 70 : 30)));
        radarData.put("efficiency", 80.0); // Dummy value or based on PSU efficiency if available

        List<BuildPerformanceDto.GameCompatibilityResult> gameResults = checkGameCompatibility((int)cpuScore, (int)gpuScore);

        return BuildPerformanceDto.builder()
                .totalScore(totalScore)
                .tier(tier)
                .breakdown(breakdown)
                .radarChartData(radarData)
                .gameCompatibility(gameResults)
                .build();
    }

    private String calculateTier(double score) {
        if (score > 25000) return "S-Tier (4K Gaming)";
        if (score >= 15000) return "A-Tier (High-end)";
        if (score >= 8000) return "B-Tier (Mid-range)";
        return "C-Tier (Entry-level)";
    }

    private List<BuildPerformanceDto.GameCompatibilityResult> checkGameCompatibility(int cpuScore, int gpuScore) {
        List<Game> games = gameRepository.findAll();
        List<BuildPerformanceDto.GameCompatibilityResult> results = new ArrayList<>();

        for (Game game : games) {
            boolean playable = cpuScore >= game.getMinCpuScore() && gpuScore >= game.getMinGpuScore();
            results.add(BuildPerformanceDto.GameCompatibilityResult.builder()
                    .gameName(game.getName())
                    .isPlayable(playable)
                    .status(playable ? "Playable" : "Upgrade Required")
                    .build());
        }
        return results;
    }
}
