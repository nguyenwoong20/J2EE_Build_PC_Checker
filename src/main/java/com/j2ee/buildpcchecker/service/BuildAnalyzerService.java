package com.j2ee.buildpcchecker.service;

import com.j2ee.buildpcchecker.dto.request.AnalyzeBuildRequest;
import com.j2ee.buildpcchecker.dto.response.BuildAnalysisResponse;
import com.j2ee.buildpcchecker.dto.response.ComponentInfo;
import com.j2ee.buildpcchecker.dto.response.ResolutionResult;
import com.j2ee.buildpcchecker.entity.Cpu;
import com.j2ee.buildpcchecker.entity.Vga;
import com.j2ee.buildpcchecker.exception.AppException;
import com.j2ee.buildpcchecker.exception.ErrorCode;
import com.j2ee.buildpcchecker.repository.CpuRepository;
import com.j2ee.buildpcchecker.repository.VgaRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Service for analyzing PC build bottleneck.
 *
 * Bottleneck is calculated per resolution using PassMark benchmark scores:
 * - CPU: CPU Mark  (from cpubenchmark.net)
 * - GPU: G3D Mark  (from videocardbenchmark.net)
 */
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class BuildAnalyzerService {

    // Resolution labels
    private static final String RES_1080P = "1080p";
    private static final String RES_2K    = "2k";
    private static final String RES_4K    = "4k";

    // Resolution weights: higher resolution → GPU works harder → CPU appears less of a bottleneck
    private static final double WEIGHT_1080P = 1.2;
    private static final double WEIGHT_2K    = 1.0;
    private static final double WEIGHT_4K    = 0.8;

    CpuRepository cpuRepository;
    VgaRepository vgaRepository;

    /**
     * Analyze PC build bottleneck for 3 resolutions.
     *
     * @param request Contains cpuId and gpuId
     * @return BuildAnalysisResponse with per-resolution bottleneck results
     */
    public BuildAnalysisResponse analyzeBuild(AnalyzeBuildRequest request) {
        log.info("Analyzing build - CPU: {}, VGA: {}", request.getCpuId(), request.getVgaId());

        // Fetch CPU
        Cpu cpu = cpuRepository.findById(request.getCpuId())
                .orElseThrow(() -> {
                    log.error("CPU not found with ID: {}", request.getCpuId());
                    return new AppException(ErrorCode.CPU_NOT_FOUND);
                });

        // Fetch GPU (VGA)
        Vga gpu = vgaRepository.findById(request.getVgaId())
                .orElseThrow(() -> {
                    log.error("GPU (VGA) not found with ID: {}", request.getVgaId());
                    return new AppException(ErrorCode.VGA_NOT_FOUND);
                });

        int cpuScore = cpu.getScore();
        int gpuScore = gpu.getScore();
        log.debug("CPU Score: {}, GPU Score: {}", cpuScore, gpuScore);

        // Base ratio: ratio < 1 → CPU bottleneck, ratio > 1 → GPU bottleneck
        double baseRatio = (double) cpuScore / gpuScore;
        log.debug("Base ratio: {}", baseRatio);

        // Calculate results for each resolution
        Map<String, ResolutionResult> results = new LinkedHashMap<>();
        results.put(RES_1080P, analyzeResolution(RES_1080P, baseRatio, WEIGHT_1080P, cpu.getName(), gpu.getName()));
        results.put(RES_2K,    analyzeResolution(RES_2K,    baseRatio, WEIGHT_2K,    cpu.getName(), gpu.getName()));
        results.put(RES_4K,    analyzeResolution(RES_4K,    baseRatio, WEIGHT_4K,    cpu.getName(), gpu.getName()));

        log.info("Build analysis completed");

        return BuildAnalysisResponse.builder()
                .cpu(ComponentInfo.builder().name(cpu.getName()).score(cpuScore).build())
                .gpu(ComponentInfo.builder().name(gpu.getName()).score(gpuScore).build())
                .results(results)
                .build();
    }

    /**
     * Analyze bottleneck for a specific resolution.
     *
     * @param resolution Resolution label ("1080p", "2k", "4k")
     * @param baseRatio  cpuScore / gpuScore
     * @param weight     Resolution weight
     * @param cpuName    CPU display name
     * @param gpuName    GPU display name
     */
    private ResolutionResult analyzeResolution(String resolution, double baseRatio, double weight,
                                               String cpuName, String gpuName) {
        double adjustedRatio = baseRatio * weight;
        double roundedRatio  = Math.round(adjustedRatio * 100.0) / 100.0;

        String type;
        String severity;
        boolean hasBottleneck;
        String message;

        if (adjustedRatio < 0.9) {
            // CPU is the bottleneck
            type = "CPU";
            hasBottleneck = true;

            if (adjustedRatio < 0.5) {
                severity = "HIGH";
                message = String.format(
                        "CPU %s sẽ trở thành điểm nghẽn đáng kể cho %s ở độ phân giải %s. " +
                        "Nên nâng cấp CPU để khai thác tối đa hiệu năng GPU.",
                        cpuName, gpuName, resolution);
            } else if (adjustedRatio < 0.7) {
                severity = "MEDIUM";
                message = String.format(
                        "CPU %s có thể giới hạn hiệu năng của %s khi chơi game ở độ phân giải %s.",
                        cpuName, gpuName, resolution);
            } else {
                severity = "LOW";
                message = String.format(
                        "CPU %s có bottleneck nhẹ với %s ở độ phân giải %s. " +
                        "Hiệu năng vẫn chấp nhận được.",
                        cpuName, gpuName, resolution);
            }

        } else if (adjustedRatio > 1.1) {
            // GPU is the bottleneck
            type = "GPU";
            hasBottleneck = true;

            if (adjustedRatio > 1.6) {
                severity = "HIGH";
                message = String.format(
                        "GPU %s sẽ trở thành điểm nghẽn đáng kể cho %s ở độ phân giải %s. " +
                        "Nên nâng cấp GPU để cải thiện hiệu năng.",
                        gpuName, cpuName, resolution);
            } else if (adjustedRatio > 1.3) {
                severity = "MEDIUM";
                message = String.format(
                        "GPU %s có thể giới hạn hiệu năng tổng thể với CPU %s ở độ phân giải %s.",
                        gpuName, cpuName, resolution);
            } else {
                severity = "LOW";
                message = String.format(
                        "GPU %s có bottleneck nhẹ với CPU %s ở độ phân giải %s. " +
                        "Hiệu năng vẫn chấp nhận được.",
                        gpuName, cpuName, resolution);
            }

        } else {
            // Balanced
            type = "NONE";
            severity = "NONE";
            hasBottleneck = false;
            message = String.format(
                    "CPU %s và GPU %s được cân bằng tốt ở độ phân giải %s. " +
                    "Không có bottleneck đáng kể.",
                    cpuName, gpuName, resolution);
        }

        log.debug("Resolution {} - ratio: {}, type: {}, severity: {}", resolution, roundedRatio, type, severity);

        return ResolutionResult.builder()
                .bottleneck(hasBottleneck)
                .type(type)
                .severity(severity)
                .ratio(roundedRatio)
                .message(message)
                .build();
    }
}
