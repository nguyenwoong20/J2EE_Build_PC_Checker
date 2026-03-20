package com.j2ee.buildpcchecker.service;

import com.j2ee.buildpcchecker.dto.request.FpsEstimateRequest;
import com.j2ee.buildpcchecker.dto.response.FpsEstimateResponse;
import com.j2ee.buildpcchecker.entity.Cpu;
import com.j2ee.buildpcchecker.entity.Game;
import com.j2ee.buildpcchecker.entity.Vga;
import com.j2ee.buildpcchecker.exception.AppException;
import com.j2ee.buildpcchecker.exception.ErrorCode;
import com.j2ee.buildpcchecker.repository.CpuRepository;
import com.j2ee.buildpcchecker.repository.GameRepository;
import com.j2ee.buildpcchecker.repository.VgaRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class FpsEstimatorService {

    private static final String RES_1080P = "1080p";
    private static final String RES_2K = "2k";
    private static final String RES_4K = "4k";

    private static final double WEIGHT_1080P = 1.2;
    private static final double WEIGHT_2K = 1.0;
    private static final double WEIGHT_4K = 0.8;

    private static final double SETTING_LOW_WEIGHT = 1.4;
    private static final double SETTING_MEDIUM_WEIGHT = 1.0;
    private static final double SETTING_HIGH_WEIGHT = 0.65;

    GameRepository gameRepository;
    CpuRepository cpuRepository;
    VgaRepository vgaRepository;

    public FpsEstimateResponse estimateFps(String gameId, FpsEstimateRequest request) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new AppException(ErrorCode.GAME_NOT_FOUND));

        Cpu cpu = cpuRepository.findById(request.getCpuId())
                .orElseThrow(() -> new AppException(ErrorCode.CPU_NOT_FOUND));

        Vga gpu = vgaRepository.findById(request.getVgaId())
                .orElseThrow(() -> new AppException(ErrorCode.VGA_NOT_FOUND));

        int cpuScore = cpu.getScore();
        int gpuScore = gpu.getScore();

        int limitingScore = Math.min(cpuScore, gpuScore);
        String limitingComponent = (cpuScore <= gpuScore) ? "CPU" : "GPU";

        int referenceScore = computeReferenceScore(game);
        double ratio = referenceScore > 0 ? (double) limitingScore / referenceScore : 0.0;

        String resolution = normalizeResolution(request.getResolution());
        double resolutionWeight = resolutionWeight(resolution);

        FpsEstimateResponse.SettingEstimate low = estimateForSetting(
                game.getBaseFpsLow(), ratio, SETTING_LOW_WEIGHT, resolutionWeight, "low");
        FpsEstimateResponse.SettingEstimate medium = estimateForSetting(
                game.getBaseFpsMedium(), ratio, SETTING_MEDIUM_WEIGHT, resolutionWeight, "medium");
        FpsEstimateResponse.SettingEstimate high = estimateForSetting(
                game.getBaseFpsHigh(), ratio, SETTING_HIGH_WEIGHT, resolutionWeight, "high");

        String upgradeAdvice = buildUpgradeAdvice(limitingComponent, cpu.getName(), gpu.getName());

        return FpsEstimateResponse.builder()
                .game(FpsEstimateResponse.GameInfo.builder()
                        .id(game.getId())
                        .name(game.getName())
                        .build())
                .pcSummary(FpsEstimateResponse.PcSummary.builder()
                        .cpuName(cpu.getName())
                        .cpuScore(cpuScore)
                        .gpuName(gpu.getName())
                        .gpuScore(gpuScore)
                        .limitingComponent(limitingComponent)
                        .build())
                .resolution(resolution)
                .fpsEstimates(FpsEstimateResponse.FpsEstimates.builder()
                        .low(low)
                        .medium(medium)
                        .high(high)
                        .build())
                .upgradeAdvice(upgradeAdvice)
                .build();
    }

    private int computeReferenceScore(Game game) {
        Integer recCpu = game.getRecCpuScore();
        Integer recGpu = game.getRecGpuScore();
        if (recCpu == null && recGpu == null) {
            return 0;
        }
        if (recCpu == null) {
            return recGpu;
        }
        if (recGpu == null) {
            return recCpu;
        }
        return Math.min(recCpu, recGpu);
    }

    private String normalizeResolution(String resolution) {
        if (resolution == null) {
            throw new AppException(ErrorCode.RESOLUTION_REQUIRED);
        }
        String r = resolution.trim().toLowerCase();
        if (r.equals(RES_1080P) || r.equals(RES_2K) || r.equals(RES_4K)) {
            return r;
        }
        throw new AppException(ErrorCode.RESOLUTION_INVALID);
    }

    private double resolutionWeight(String resolution) {
        return switch (resolution) {
            case RES_1080P -> WEIGHT_1080P;
            case RES_2K -> WEIGHT_2K;
            case RES_4K -> WEIGHT_4K;
            default -> WEIGHT_2K;
        };
    }

    private FpsEstimateResponse.SettingEstimate estimateForSetting(
            Integer baseline,
            double ratio,
            double settingWeight,
            double resolutionWeight,
            String settingLabel
    ) {
        if (baseline == null || baseline <= 0) {
            return FpsEstimateResponse.SettingEstimate.builder()
                    .estimatedFps(null)
                    .verdict("UNKNOWN")
                    .message("Chưa có dữ liệu FPS baseline cho setting " + settingLabel + ".")
                    .build();
        }

        double raw = baseline * ratio * settingWeight * resolutionWeight;
        int estimated = (int) Math.round(raw);
        int capped = Math.min(estimated, baseline * 2);

        String verdict = fpsVerdict(capped);
        String message = fpsMessage(settingLabel, capped, verdict);

        return FpsEstimateResponse.SettingEstimate.builder()
                .estimatedFps(capped)
                .verdict(verdict)
                .message(message)
                .build();
    }

    private String fpsVerdict(int fps) {
        if (fps >= 120) return "ULTRA_SMOOTH";
        if (fps >= 60) return "EXCELLENT";
        if (fps >= 45) return "PLAYABLE";
        if (fps >= 30) return "BELOW_TARGET";
        return "UNPLAYABLE";
    }

    private String fpsMessage(String setting, int fps, String verdict) {
        return switch (verdict) {
            case "ULTRA_SMOOTH" -> "Rất mượt ở " + setting + ". Phù hợp để ưu tiên FPS cao.";
            case "EXCELLENT" -> "Chơi rất tốt ở " + setting + ".";
            case "PLAYABLE" -> "Chơi ổn ở " + setting + ". Có thể cần tối ưu một số tuỳ chọn.";
            case "BELOW_TARGET" -> "FPS hơi thấp ở " + setting + ". Khuyến nghị giảm setting hoặc nâng cấp phần cứng.";
            default -> "Khó chơi ở " + setting + " (dưới 30 FPS).";
        };
    }

    private String buildUpgradeAdvice(String limitingComponent, String cpuName, String gpuName) {
        if ("GPU".equalsIgnoreCase(limitingComponent)) {
            return "GPU " + gpuName + " là điểm giới hạn chính. Nâng GPU sẽ cải thiện FPS rõ rệt.";
        }
        return "CPU " + cpuName + " là điểm giới hạn chính. Nâng CPU sẽ giúp FPS ổn định hơn (đặc biệt 1080p).";
    }
}

