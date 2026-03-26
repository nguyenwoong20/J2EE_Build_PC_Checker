package com.j2ee.buildpcchecker.service;

import com.j2ee.buildpcchecker.dto.request.BuildCheckRequest;
import com.j2ee.buildpcchecker.dto.response.*;
import com.j2ee.buildpcchecker.entity.*;
import com.j2ee.buildpcchecker.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BuildComparisonService {

    private final CpuRepository cpuRepository;
    private final VgaRepository vgaRepository;
    private final RamRepository ramRepository;
    private final SsdRepository ssdRepository;
    private final HddRepository hddRepository;
    private final PsuRepository psuRepository;
    private final CompatibilityService compatibilityService;

    public BuildComparisonResultDto compareBuilds(BuildCheckRequest build1, BuildCheckRequest build2) {
        log.info("Comparing two PC builds");

        var result = BuildComparisonResultDto.builder()
                .cpu(compareCpu(build1.getCpuId(), build2.getCpuId()))
                .gpu(compareGpu(build1.getVgaId(), build2.getVgaId()))
                .ram(compareRam(build1.getRamId(), build2.getRamId()))
                .storage(compareStorage(build1.getSsdIds(), build1.getHddIds(), build2.getSsdIds(), build2.getHddIds()))
                .psu(comparePsu(build1, build2))
                .build();

        result.setOverallWinner(calculateOverallWinner(result, build1, build2));
        return result;
    }

    private ComponentComparison<ComponentInfo> compareCpu(String id1, String id2) {
        Cpu cpu1 = id1 != null ? cpuRepository.findById(id1).orElse(null) : null;
        Cpu cpu2 = id2 != null ? cpuRepository.findById(id2).orElse(null) : null;

        ComponentInfo info1 = cpu1 != null ? ComponentInfo.builder().name(cpu1.getName()).score(cpu1.getScore()).build() : null;
        ComponentInfo info2 = cpu2 != null ? ComponentInfo.builder().name(cpu2.getName()).score(cpu2.getScore()).build() : null;

        String winner;
        if (info1 != null && info2 != null) {
            if (info1.getScore() > info2.getScore()) winner = "build1";
            else if (info2.getScore() > info1.getScore()) winner = "build2";
            else {
                // Tie breaker: Efficiency (Lower TDP is better)
                if (cpu1.getTdp() < cpu2.getTdp()) winner = "build1";
                else if (cpu2.getTdp() < cpu1.getTdp()) winner = "build2";
                else winner = "equal"; // Identical
            }
        } else if (info1 != null) winner = "build1";
        else if (info2 != null) winner = "build2";
        else winner = "equal"; // Both null

        return ComponentComparison.<ComponentInfo>builder()
                .build1(info1).build2(info2).winner(winner).build();
    }

    private ComponentComparison<BuildComparisonResultDto.GpuComparisonInfo> compareGpu(String id1, String id2) {
        Vga vga1 = id1 != null ? vgaRepository.findById(id1).orElse(null) : null;
        Vga vga2 = id2 != null ? vgaRepository.findById(id2).orElse(null) : null;

        BuildComparisonResultDto.GpuComparisonInfo info1 = vga1 != null ? BuildComparisonResultDto.GpuComparisonInfo.builder()
                .name(vga1.getName()).score(vga1.getScore()).vramGb(vga1.getVramGb()).build() : null;
        BuildComparisonResultDto.GpuComparisonInfo info2 = vga2 != null ? BuildComparisonResultDto.GpuComparisonInfo.builder()
                .name(vga2.getName()).score(vga2.getScore()).vramGb(vga2.getVramGb()).build() : null;

        String winner;
        if (info1 != null && info2 != null) {
            if (Math.abs(info1.getScore() - info2.getScore()) < 500) {
                if (info1.getVramGb() > info2.getVramGb()) winner = "build1";
                else if (info2.getVramGb() > info1.getVramGb()) winner = "build2";
                else if (info1.getScore() > info2.getScore()) winner = "build1";
                else if (info2.getScore() > info1.getScore()) winner = "build2";
                else {
                    // Tie breaker: Efficiency
                    if (vga1.getTdp() < vga2.getTdp()) winner = "build1";
                    else if (vga2.getTdp() < vga1.getTdp()) winner = "build2";
                    else winner = "equal";
                }
            } else {
                if (info1.getScore() > info2.getScore()) winner = "build1";
                else winner = "build2";
            }
        } else if (info1 != null) winner = "build1";
        else if (info2 != null) winner = "build2";
        else winner = "equal";

        return ComponentComparison.<BuildComparisonResultDto.GpuComparisonInfo>builder()
                .build1(info1).build2(info2).winner(winner).build();
    }

    private ComponentComparison<BuildComparisonResultDto.RamComparisonInfo> compareRam(String id1, String id2) {
        Ram ram1 = id1 != null ? ramRepository.findById(id1).orElse(null) : null;
        Ram ram2 = id2 != null ? ramRepository.findById(id2).orElse(null) : null;

        BuildComparisonResultDto.RamComparisonInfo info1 = ram1 != null ? BuildComparisonResultDto.RamComparisonInfo.builder()
                .name(ram1.getName()).totalGb(ram1.getCapacityPerStick() * ram1.getQuantity()).bus(ram1.getRamBus()).cas(ram1.getRamCas()).build() : null;
        BuildComparisonResultDto.RamComparisonInfo info2 = ram2 != null ? BuildComparisonResultDto.RamComparisonInfo.builder()
                .name(ram2.getName()).totalGb(ram2.getCapacityPerStick() * ram2.getQuantity()).bus(ram2.getRamBus()).cas(ram2.getRamCas()).build() : null;

        String winner;
        if (info1 != null && info2 != null) {
            if (info1.getTotalGb() > info2.getTotalGb()) winner = "build1";
            else if (info2.getTotalGb() > info1.getTotalGb()) winner = "build2";
            else if (info1.getBus() > info2.getBus()) winner = "build1";
            else if (info2.getBus() > info1.getBus()) winner = "build2";
            else if (info1.getCas() < info2.getCas()) winner = "build1";
            else if (info2.getCas() < info1.getCas()) winner = "build2";
            else {
                // Tie breaker: Efficiency
                if (ram1.getTdp() < ram2.getTdp()) winner = "build1";
                else if (ram2.getTdp() < ram1.getTdp()) winner = "build2";
                else winner = "equal";
            }
        } else if (info1 != null) winner = "build1";
        else if (info2 != null) winner = "build2";
        else winner = "equal";

        return ComponentComparison.<BuildComparisonResultDto.RamComparisonInfo>builder()
                .build1(info1).build2(info2).winner(winner).build();
    }

    private ComponentComparison<BuildComparisonResultDto.StorageComparisonInfo> compareStorage(List<String> s1, List<String> h1, List<String> s2, List<String> h2) {
        List<Ssd> ssds1 = s1 != null ? ssdRepository.findAllById(s1) : Collections.emptyList();
        List<Hdd> hdds1 = h1 != null ? hddRepository.findAllById(h1) : Collections.emptyList();
        List<Ssd> ssds2 = s2 != null ? ssdRepository.findAllById(s2) : Collections.emptyList();
        List<Hdd> hdds2 = h2 != null ? hddRepository.findAllById(h2) : Collections.emptyList();

        int cap1 = ssds1.stream().mapToInt(Ssd::getCapacity).sum() + hdds1.stream().mapToInt(Hdd::getCapacity).sum();
        int cap2 = ssds2.stream().mapToInt(Ssd::getCapacity).sum() + hdds2.stream().mapToInt(Hdd::getCapacity).sum();

        String type1 = getStorageTier(ssds1, hdds1);
        String type2 = getStorageTier(ssds2, hdds2);

        BuildComparisonResultDto.StorageComparisonInfo info1 = BuildComparisonResultDto.StorageComparisonInfo.builder()
                .name("Storage Build 1").type(type1).totalCapacity(cap1).build();
        BuildComparisonResultDto.StorageComparisonInfo info2 = BuildComparisonResultDto.StorageComparisonInfo.builder()
                .name("Storage Build 2").type(type2).totalCapacity(cap2).build();

        String winner;
        int rank1 = getStorageRank(type1);
        int rank2 = getStorageRank(type2);

        if (rank1 > rank2) winner = "build1";
        else if (rank2 > rank1) winner = "build2";
        else if (cap1 > cap2) winner = "build1";
        else if (cap2 > cap1) winner = "build2";
        else {
            // Tie breaker: Prefer build with more SSDs
            if (ssds1.size() > ssds2.size()) winner = "build1";
            else if (ssds2.size() > ssds1.size()) winner = "build2";
            else winner = "equal";
        }

        return ComponentComparison.<BuildComparisonResultDto.StorageComparisonInfo>builder()
                .build1(info1).build2(info2).winner(winner).build();
    }

    private String getStorageTier(List<Ssd> ssds, List<Hdd> hdds) {
        if (ssds.stream().anyMatch(s -> s.getSsdType().getName().equalsIgnoreCase("NVME"))) return "NVMe";
        if (!ssds.isEmpty()) return "SATA SSD";
        if (!hdds.isEmpty()) return "HDD";
        return "None";
    }

    private int getStorageRank(String tier) {
        switch (tier) {
            case "NVMe": return 3;
            case "SATA SSD": return 2;
            case "HDD": return 1;
            default: return 0;
        }
    }

    private ComponentComparison<BuildComparisonResultDto.PsuComparisonInfo> comparePsu(BuildCheckRequest b1, BuildCheckRequest b2) {
        CompatibilityResult res1 = compatibilityService.checkCompatibility(b1);
        CompatibilityResult res2 = compatibilityService.checkCompatibility(b2);

        Psu psu1 = b1.getPsuId() != null ? psuRepository.findById(b1.getPsuId()).orElse(null) : null;
        Psu psu2 = b2.getPsuId() != null ? psuRepository.findById(b2.getPsuId()).orElse(null) : null;

        BuildComparisonResultDto.PsuComparisonInfo info1 = BuildComparisonResultDto.PsuComparisonInfo.builder()
                .name(psu1 != null ? psu1.getName() : "None")
                .recommendedWattage(res1.getRecommendedPsuWattage())
                .actualWattage(psu1 != null ? psu1.getWattage() : 0)
                .isSafe(res1.isCompatible() && psu1 != null && psu1.getWattage() >= res1.getRecommendedPsuWattage())
                .build();

        BuildComparisonResultDto.PsuComparisonInfo info2 = BuildComparisonResultDto.PsuComparisonInfo.builder()
                .name(psu2 != null ? psu2.getName() : "None")
                .recommendedWattage(res2.getRecommendedPsuWattage())
                .actualWattage(psu2 != null ? psu2.getWattage() : 0)
                .isSafe(res2.isCompatible() && psu2 != null && psu2.getWattage() >= res2.getRecommendedPsuWattage())
                .build();

        String winner;
        if (info1.isSafe() && !info2.isSafe()) winner = "build1";
        else if (!info1.isSafe() && info2.isSafe()) winner = "build2";
        else if (info1.isSafe() && info2.isSafe()) {
            double margin1 = (double) info1.getActualWattage() / info1.getRecommendedWattage();
            double margin2 = (double) info2.getActualWattage() / info2.getRecommendedWattage();
            if (margin1 > margin2) winner = "build1";
            else if (margin2 > margin1) winner = "build2";
            else winner = "equal";
        } else {
            // Both unsafe, but which one is less bad?
            double margin1 = (double) info1.getActualWattage() / info1.getRecommendedWattage();
            double margin2 = (double) info2.getActualWattage() / info2.getRecommendedWattage();
            if (margin1 > margin2) winner = "build1";
            else if (margin2 > margin1) winner = "build2";
            else winner = "equal";
        }

        return ComponentComparison.<BuildComparisonResultDto.PsuComparisonInfo>builder()
                .build1(info1).build2(info2).winner(winner).build();
    }

    private String calculateOverallWinner(BuildComparisonResultDto result, BuildCheckRequest b1Req, BuildCheckRequest b2Req) {
        int b1 = 0, b2 = 0;
        if (result.getCpu().getWinner().equals("build1")) b1++; else if (result.getCpu().getWinner().equals("build2")) b2++;
        if (result.getGpu().getWinner().equals("build1")) b1++; else if (result.getGpu().getWinner().equals("build2")) b2++;
        if (result.getRam().getWinner().equals("build1")) b1++; else if (result.getRam().getWinner().equals("build2")) b2++;
        if (result.getStorage().getWinner().equals("build1")) b1++; else if (result.getStorage().getWinner().equals("build2")) b2++;
        if (result.getPsu().getWinner().equals("build1")) b1++; else if (result.getPsu().getWinner().equals("build2")) b2++;

        if (b1 > b2) return "build1";
        if (b2 > b1) return "build2";

        // Tie breaker 1: Aggregate CPU + GPU score (Primary performance indicator)
        int score1 = 0, score2 = 0;
        Cpu cpu1 = b1Req.getCpuId() != null ? cpuRepository.findById(b1Req.getCpuId()).orElse(null) : null;
        Cpu cpu2 = b2Req.getCpuId() != null ? cpuRepository.findById(b2Req.getCpuId()).orElse(null) : null;
        Vga vga1 = b1Req.getVgaId() != null ? vgaRepository.findById(b1Req.getVgaId()).orElse(null) : null;
        Vga vga2 = b2Req.getVgaId() != null ? vgaRepository.findById(b2Req.getVgaId()).orElse(null) : null;

        if (cpu1 != null) score1 += cpu1.getScore();
        if (vga1 != null) score1 += vga1.getScore();
        if (cpu2 != null) score2 += cpu2.getScore();
        if (vga2 != null) score2 += vga2.getScore();

        if (score1 > score2) return "build1";
        if (score2 > score1) return "build2";

        // Tie breaker 2: Power Efficiency (Lower TDP is better if performance is identical)
        int tdp1 = (cpu1 != null ? cpu1.getTdp() : 0) + (vga1 != null ? vga1.getTdp() : 0);
        int tdp2 = (cpu2 != null ? cpu2.getTdp() : 0) + (vga2 != null ? vga2.getTdp() : 0);
        if (tdp1 < tdp2) return "build1";
        if (tdp2 < tdp1) return "build2";

        // Tie breaker 3: RAM Capacity
        int ram1 = result.getRam().getBuild1() != null ? result.getRam().getBuild1().getTotalGb() : 0;
        int ram2 = result.getRam().getBuild2() != null ? result.getRam().getBuild2().getTotalGb() : 0;
        if (ram1 > ram2) return "build1";
        if (ram2 > ram1) return "build2";

        // Tie breaker 4: Storage Capacity
        int storage1 = result.getStorage().getBuild1() != null ? result.getStorage().getBuild1().getTotalCapacity() : 0;
        int storage2 = result.getStorage().getBuild2() != null ? result.getStorage().getBuild2().getTotalCapacity() : 0;
        if (storage1 > storage2) return "build1";
        if (storage2 > storage1) return "build2";

        // Final fail-safe: Compare by name alphabetically
        String name1 = (cpu1 != null ? cpu1.getName() : "") + (vga1 != null ? vga1.getName() : "");
        String name2 = (cpu2 != null ? cpu2.getName() : "") + (vga2 != null ? vga2.getName() : "");
        int nameComp = name1.compareToIgnoreCase(name2);
        if (nameComp < 0) return "build1";
        if (nameComp > 0) return "build2";

        return "equal"; // Truly identical computers
    }
}
