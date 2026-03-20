package com.j2ee.buildpcchecker.service;

import com.j2ee.buildpcchecker.dto.request.GameCompatCheckRequest;
import com.j2ee.buildpcchecker.dto.request.GameCompatibilityRequest;
import com.j2ee.buildpcchecker.dto.response.GameCompatListResponse;
import com.j2ee.buildpcchecker.dto.response.GameCompatibilityResponse;
import com.j2ee.buildpcchecker.entity.Cpu;
import com.j2ee.buildpcchecker.entity.Game;
import com.j2ee.buildpcchecker.entity.Ram;
import com.j2ee.buildpcchecker.entity.Vga;
import com.j2ee.buildpcchecker.exception.AppException;
import com.j2ee.buildpcchecker.exception.ErrorCode;
import com.j2ee.buildpcchecker.repository.CpuRepository;
import com.j2ee.buildpcchecker.repository.GameRepository;
import com.j2ee.buildpcchecker.repository.RamRepository;
import com.j2ee.buildpcchecker.repository.VgaRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class GameCompatibilityService {

    GameRepository gameRepository;
    CpuRepository cpuRepository;
    VgaRepository vgaRepository;
    RamRepository ramRepository;

    public GameCompatibilityResponse checkCompatibility(String gameId, GameCompatibilityRequest request) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new AppException(ErrorCode.GAME_NOT_FOUND));

        Cpu cpu = cpuRepository.findById(request.getCpuId())
                .orElseThrow(() -> new AppException(ErrorCode.CPU_NOT_FOUND));

        Vga gpu = vgaRepository.findById(request.getVgaId())
                .orElseThrow(() -> new AppException(ErrorCode.VGA_NOT_FOUND));

        Integer ramGb = null;
        if (request.getRamId() != null && !request.getRamId().isBlank()) {
            Ram ram = ramRepository.findById(request.getRamId())
                    .orElseThrow(() -> new AppException(ErrorCode.RAM_NOT_FOUND));
            ramGb = ram.getCapacityPerStick() * ram.getQuantity();
        }

        Integer vramGb = gpu.getVramGb();

        boolean passRecCpu = game.getRecCpuScore() == null || cpu.getScore() >= game.getRecCpuScore();
        boolean passRecGpu = game.getRecGpuScore() == null || gpu.getScore() >= game.getRecGpuScore();
        boolean passRecRam = game.getRecRamGb() == null || ramGb == null || ramGb >= game.getRecRamGb();
        boolean passRecVram = game.getRecVramGb() == null || (vramGb != null && vramGb >= game.getRecVramGb());

        boolean passMinCpu = game.getMinCpuScore() == null || cpu.getScore() >= game.getMinCpuScore();
        boolean passMinGpu = game.getMinGpuScore() == null || gpu.getScore() >= game.getMinGpuScore();
        boolean passMinRam = game.getMinRamGb() == null || ramGb == null || ramGb >= game.getMinRamGb();
        boolean passMinVram = game.getMinVramGb() == null || (vramGb != null && vramGb >= game.getMinVramGb());

        String compatibility;
        String message;

        if (passRecCpu && passRecGpu && passRecRam && passRecVram) {
            compatibility = "RECOMMENDED";
            message = "Cấu hình của bạn đạt mức Recommended cho game này.";
        } else if (passMinCpu && passMinGpu && passMinRam && passMinVram) {
            compatibility = "MINIMUM";
            message = "Cấu hình của bạn đạt mức Minimum. Bạn có thể cần giảm setting để chơi mượt hơn.";
        } else {
            compatibility = "NOT_SUPPORTED";
            message = "Cấu hình của bạn chưa đáp ứng yêu cầu tối thiểu cho game này.";
        }

        return GameCompatibilityResponse.builder()
                .game(GameCompatibilityResponse.GameInfo.builder()
                        .id(game.getId())
                        .name(game.getName())
                        .build())
                .pcSummary(GameCompatibilityResponse.PcSpecSummary.builder()
                        .cpuName(cpu.getName())
                        .cpuScore(cpu.getScore())
                        .gpuName(gpu.getName())
                        .gpuScore(gpu.getScore())
                        .ramGb(ramGb)
                        .build())
                .compatibility(compatibility)
                .message(message)
                .build();
    }

    /**
     * List all games with compatibility status for the given PC build.
     */
    public GameCompatListResponse checkCompatibleGames(GameCompatCheckRequest request) {
        Cpu cpu = cpuRepository.findById(request.getCpuId())
                .orElseThrow(() -> new AppException(ErrorCode.CPU_NOT_FOUND));
        Vga gpu = vgaRepository.findById(request.getVgaId())
                .orElseThrow(() -> new AppException(ErrorCode.VGA_NOT_FOUND));
        Ram ram = ramRepository.findById(request.getRamId())
                .orElseThrow(() -> new AppException(ErrorCode.RAM_NOT_FOUND));

        int sticks = request.getRamQuantity() != null ? request.getRamQuantity() : ram.getQuantity();
        int totalRamGb = ram.getCapacityPerStick() * sticks;
        Integer vramGb = gpu.getVramGb();

        List<GameCompatListResponse.GameCompatItem> results = new ArrayList<>();
        for (Game game : gameRepository.findAll()) {
            boolean passRecCpu = game.getRecCpuScore() == null || cpu.getScore() >= game.getRecCpuScore();
            boolean passRecGpu = game.getRecGpuScore() == null || gpu.getScore() >= game.getRecGpuScore();
            boolean passRecRam = game.getRecRamGb() == null || totalRamGb >= game.getRecRamGb();
            boolean passRecVram = game.getRecVramGb() == null || (vramGb != null && vramGb >= game.getRecVramGb());

            boolean passMinCpu = game.getMinCpuScore() == null || cpu.getScore() >= game.getMinCpuScore();
            boolean passMinGpu = game.getMinGpuScore() == null || gpu.getScore() >= game.getMinGpuScore();
            boolean passMinRam = game.getMinRamGb() == null || totalRamGb >= game.getMinRamGb();
            boolean passMinVram = game.getMinVramGb() == null || (vramGb != null && vramGb >= game.getMinVramGb());

            String status;
            String detail;
            if (passRecCpu && passRecGpu && passRecRam && passRecVram) {
                status = "RECOMMENDED";
                detail = "Cấu hình của bạn đáp ứng mức khuyến nghị của " + game.getName() + ".";
            } else if (passMinCpu && passMinGpu && passMinRam && passMinVram) {
                status = "MINIMUM";
                if (!passRecGpu && passMinGpu) {
                    detail = "GPU của bạn chỉ đủ mức tối thiểu. Khuyến nghị nâng cấp GPU.";
                } else if (!passRecCpu && passMinCpu) {
                    detail = "CPU của bạn chỉ đủ mức tối thiểu. Khuyến nghị nâng cấp CPU.";
                } else {
                    detail = "Cấu hình của bạn đạt mức tối thiểu. Có thể cần giảm setting.";
                }
            } else {
                status = "NOT_SUPPORTED";
                if (!passMinCpu) {
                    detail = "CPU không đủ điều kiện để chạy game này.";
                } else if (!passMinGpu) {
                    detail = "GPU không đủ điều kiện để chạy game này.";
                } else if (!passMinRam) {
                    detail = "RAM không đủ điều kiện để chạy game này.";
                } else if (!passMinVram) {
                    detail = "VRAM không đủ điều kiện để chạy game này.";
                } else {
                    detail = "Cấu hình chưa đáp ứng yêu cầu tối thiểu.";
                }
            }

            results.add(GameCompatListResponse.GameCompatItem.builder()
                    .gameId(game.getId())
                    .gameName(game.getName())
                    .genre(game.getGenre())
                    .coverImageUrl(game.getCoverImageUrl())
                    .status(status)
                    .detail(detail)
                    .build());
        }

        return GameCompatListResponse.builder()
                .pcSummary(GameCompatListResponse.PcSummary.builder()
                        .cpuName(cpu.getName())
                        .cpuScore(cpu.getScore())
                        .gpuName(gpu.getName())
                        .gpuScore(gpu.getScore())
                        .totalRamGb(totalRamGb)
                        .build())
                .results(results)
                .build();
    }
}

