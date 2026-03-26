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
    private final MainboardRepository mainboardRepository;
    private final PsuRepository psuRepository;
    private final CoolerRepository coolerRepository;

    public BuildPerformanceDto evaluateBuild(BuildCheckRequest request) {
        log.info("Evaluating PC build performance with comprehensive analysis");

        Cpu cpu = request.getCpuId() != null ? cpuRepository.findById(request.getCpuId()).orElse(null) : null;
        Vga vga = request.getVgaId() != null ? vgaRepository.findById(request.getVgaId()).orElse(null) : null;
        Ram ram = request.getRamId() != null ? ramRepository.findById(request.getRamId()).orElse(null) : null;
        Mainboard mb = request.getMainboardId() != null ? mainboardRepository.findById(request.getMainboardId()).orElse(null) : null;
        Psu psu = request.getPsuId() != null ? psuRepository.findById(request.getPsuId()).orElse(null) : null;
        Cooler cooler = request.getCoolerId() != null ? coolerRepository.findById(request.getCoolerId()).orElse(null) : null;
        List<Ssd> ssds = request.getSsdIds() != null ? ssdRepository.findAllById(request.getSsdIds()) : Collections.emptyList();
        List<Hdd> hdds = request.getHddIds() != null ? hddRepository.findAllById(request.getHddIds()) : Collections.emptyList();

        double cpuScore = cpu != null ? cpu.getScore() : 0;
        double gpuScore = vga != null ? vga.getScore() : 0;
        int totalRamGb = ram != null ? ram.getCapacityPerStick() * ram.getQuantity() : 0;

        // --- Bonuses Calculation ---
        double ramBonus = calculateRamBonus(ram, totalRamGb);
        double storageBonus = calculateStorageBonus(ssds, hdds);
        double mbBonus = (mb != null) ? (mb.getVrmPhase() * 100) : 0;

        // --- Comprehensive Scoring ---
        double gamingScore = (gpuScore * 0.7) + (cpuScore * 0.25) + (double) totalRamGb * 10 + ramBonus;
        double workstationScore = (cpuScore * 0.6) + (double) totalRamGb * 30 + storageBonus + mbBonus;
        
        // Final combined score with weight
        double totalScore = (gamingScore * 0.6) + (workstationScore * 0.4);

        // --- Future Proofing & Bottleneck ---
        String bottleneck = detectBottleneck(cpuScore, gpuScore);
        String tierGrade = calculateTierGrade(totalScore);
        String tier = getTierDescription(tierGrade);
        String buildSummary = generateBuildSummary(cpu, vga, totalRamGb, ssds, hdds, tierGrade, tier, bottleneck);

        // --- Data Visuals ---
        Map<String, Double> breakdown = new HashMap<>();
        breakdown.put("CPU Contribution", cpuScore);
        breakdown.put("GPU Contribution", gpuScore);
        breakdown.put("RAM/Memory Index", (double)totalRamGb * 15 + ramBonus);
        breakdown.put("Storage Speed Bonus", storageBonus);
        breakdown.put("Platform Stability", mbBonus);

        Map<String, Double> radarData = new HashMap<>();
        radarData.put("Ultra Gaming", Math.min(100.0, gamingScore / 250));
        radarData.put("Productivity", Math.min(100.0, workstationScore / 200));
        radarData.put("Power Stability", calculatePowerStability(psu, cpu, vga));
        radarData.put("Future-Proofing", calculateFutureProofScore(ram, ssds, mb));
        radarData.put("Cooling Stability", calculateCoolingScore(cpu, cooler));

        List<BuildPerformanceDto.GameCompatibilityResult> gameResults = checkGameCompatibility((int)cpuScore, (int)gpuScore);

        return BuildPerformanceDto.builder()
                .totalScore(totalScore)
                .gamingScore(gamingScore)
                .workstationScore(workstationScore)
                .tier(tier)
                .tierGrade(tierGrade)
                .bottleneck(bottleneck)
                .summary(buildSummary)
                .breakdown(breakdown)
                .radarChartData(radarData)
                .gameCompatibility(gameResults)
                .build();
    }

    private double calculateRamBonus(Ram ram, int totalGb) {
        double bonus = 0;
        if (totalGb >= 32) bonus += 2000;
        else if (totalGb >= 16) bonus += 1200;
        else bonus -= 1000;

        if (ram != null && ram.getRamBus() > 3600) bonus += 500;
        if (ram != null && ram.getRamCas() != null && ram.getRamCas() <= 16) bonus += 300;
        return bonus;
    }

    private double calculateStorageBonus(List<Ssd> ssds, List<Hdd> hdds) {
        double bonus = 0;
        boolean hasNvme = ssds.stream().anyMatch(s -> s.getSsdType().getName().equalsIgnoreCase("NVME"));
        if (hasNvme) bonus += 1500;
        else if (!ssds.isEmpty()) bonus += 800;
        
        if (hdds.size() > 2) bonus += 200; // Large storage bonus
        return bonus;
    }

    private String detectBottleneck(double cpuScore, double gpuScore) {
        if (cpuScore == 0 || gpuScore == 0) return "Incomplete Build";
        
        double ratio = gpuScore / cpuScore;
        if (ratio > 2.5) return "Significant CPU Bottleneck (GPU is too powerful for this CPU)";
        if (ratio > 1.8) return "Minor CPU Bottleneck";
        if (ratio < 0.4) return "Significant GPU Bottleneck (CPU is wasted on this GPU)";
        if (ratio < 0.7) return "Minor GPU Bottleneck";
        
        return "Perfectly Balanced";
    }

    private String calculateTierGrade(double total) {
        if (total > 35000) return "S";
        if (total >= 22000) return "A";
        if (total >= 13000) return "B";
        if (total >= 7000) return "C";
        if (total >= 3500) return "D";
        return "E";
    }

    private String getTierDescription(String tierGrade) {
        return switch (tierGrade) {
            case "S" -> "Master (Elite Gaming & Workstation)";
            case "A" -> "Professional (Excellent 4K/2K & Video Editing)";
            case "B" -> "Enthusiast (Solid 2K High-Refresh Gaming)";
            case "C" -> "Baseline (1080p Mainstream)";
            case "D" -> "Office / Daily (Not for gaming)";
            case "E" -> "Entry Level / Legacy";
            default -> "Unknown";
        };
    }

    private double calculatePowerStability(Psu psu, Cpu cpu, Vga vga) {
        if (psu == null) return 40.0;
        int estimatedTdp = (cpu != null ? cpu.getTdp() : 100) + (vga != null ? vga.getTdp() : 200) + 60;
        double ratio = (double) psu.getWattage() / estimatedTdp;
        
        double score = 60.0;
        if (ratio >= 1.5) score += 20;
        else if (ratio >= 1.2) score += 10;
        else if (ratio < 1.0) score -= 30;

        if (psu.getEfficiency().contains("Gold") || psu.getEfficiency().contains("Platinum")) score += 10;
        return Math.min(100.0, score);
    }

    private double calculateFutureProofScore(Ram ram, List<Ssd> ssds, Mainboard mb) {
        double fp = 50.0;
        if (ram != null && ram.getRamType().getName().contains("DDR5")) fp += 20;
        if (mb != null && mb.getPcieVgaVersion().getName().contains("5.0")) fp += 15;
        if (ssds.size() < (mb != null ? mb.getM2Slot() : 1)) fp += 10;
        return Math.min(100.0, fp);
    }

    private double calculateCoolingScore(Cpu cpu, Cooler cooler) {
        if (cpu == null || cooler == null) return 50.0;
        double ratio = (double) cooler.getTdpSupport() / cpu.getTdp();
        if (ratio > 2.0) return 95.0; // Overkill, excellent
        if (ratio >= 1.2) return 80.0; // Safe
        if (ratio >= 1.0) return 60.0; // Sufficient
        return 30.0; // Overheating risk
    }

    private String generateBuildSummary(Cpu cpu, Vga vga, int ramGb, List<Ssd> ssds, List<Hdd> hdds, String tierGrade, String tier, String bottleneck) {
        StringBuilder sb = new StringBuilder();
        sb.append("This is a Tier ").append(tierGrade).append(" (").append(tier).append(") build. ");
        sb.append("Equipped with ").append(cpu != null ? cpu.getName() : "Unknown CPU");
        sb.append(" and ").append(vga != null ? vga.getName() : "Unknown GPU").append(". ");
        sb.append("With ").append(ramGb).append("GB of RAM, this system is ");
        
        if (ramGb >= 32) sb.append("ready for extreme multitasking and heavy professional apps. ");
        else if (ramGb >= 16) sb.append("perfect for modern gaming. ");
        else sb.append("potentially limited by RAM capacity in modern tasks. ");

        sb.append("Status: ").append(bottleneck).append(".");
        return sb.toString();
    }

    private List<BuildPerformanceDto.GameCompatibilityResult> checkGameCompatibility(int cpuScore, int gpuScore) {
        List<Game> games = gameRepository.findAll();
        List<BuildPerformanceDto.GameCompatibilityResult> results = new ArrayList<>();

        for (Game game : games) {
            boolean playable = cpuScore >= game.getMinCpuScore() && gpuScore >= game.getMinGpuScore();
            String status;
            if (playable) {
                if (gpuScore >= game.getRecGpuScore() && cpuScore >= game.getRecCpuScore()) status = "Recommended High Settings";
                else status = "Playable at Low/Medium Settings";
            } else {
                if (gpuScore < game.getMinGpuScore()) status = "VGA Upgrade Required";
                else status = "CPU Upgrade Required";
            }

            results.add(BuildPerformanceDto.GameCompatibilityResult.builder()
                    .gameName(game.getName())
                    .isPlayable(playable)
                    .status(status)
                    .build());
        }
        return results;
    }
}
