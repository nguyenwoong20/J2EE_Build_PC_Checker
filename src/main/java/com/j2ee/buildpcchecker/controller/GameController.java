package com.j2ee.buildpcchecker.controller;

import com.j2ee.buildpcchecker.dto.request.ApiResponse;
import com.j2ee.buildpcchecker.dto.request.FpsEstimateRequest;
import com.j2ee.buildpcchecker.dto.request.GameCompatCheckRequest;
import com.j2ee.buildpcchecker.dto.request.GameCompatibilityRequest;
import com.j2ee.buildpcchecker.dto.request.GameCreationRequest;
import com.j2ee.buildpcchecker.dto.request.GameUpdateRequest;
import com.j2ee.buildpcchecker.dto.response.FpsEstimateResponse;
import com.j2ee.buildpcchecker.dto.response.GameCompatListResponse;
import com.j2ee.buildpcchecker.dto.response.GameCompatibilityResponse;
import com.j2ee.buildpcchecker.dto.response.GameResponse;
import com.j2ee.buildpcchecker.dto.response.GameSummaryResponse;
import com.j2ee.buildpcchecker.dto.response.PageResponse;
import com.j2ee.buildpcchecker.service.FpsEstimatorService;
import com.j2ee.buildpcchecker.service.GameCompatibilityService;
import com.j2ee.buildpcchecker.service.GameService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class GameController {

    GameService gameService;
    GameCompatibilityService gameCompatibilityService;
    FpsEstimatorService fpsEstimatorService;

    @PostMapping
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Game created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "Create Game",
                                    value = """
                                            {
                                              "code": 1000,
                                              "message": "Success",
                                              "result": {
                                                "id": "uuid",
                                                "name": "Cyberpunk 2077",
                                                "genre": "RPG/Action",
                                                "minCpuScore": 9000,
                                                "minGpuScore": 8000,
                                                "recCpuScore": 16000,
                                                "recGpuScore": 17000,
                                                "minRamGb": 12,
                                                "recRamGb": 16,
                                                "baseFpsLow": 45,
                                                "baseFpsMedium": 60,
                                                "baseFpsHigh": 60
                                              }
                                            }
                                            """
                            )
                    )
            )
    })
    public ApiResponse<GameResponse> createGame(@RequestBody @Valid GameCreationRequest request) {
        log.info("Creating Game: {}", request.getName());

        ApiResponse<GameResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(gameService.createGame(request));
        return apiResponse;
    }

    @GetMapping
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Get games with pagination and optional genre filter"
            )
    })
    public ApiResponse<PageResponse<GameSummaryResponse>> getGames(
            @RequestParam(required = false) String genre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        log.info("Getting games - genre: {}, page: {}, size: {}", genre, page, size);
        return ApiResponse.<PageResponse<GameSummaryResponse>>builder()
                .result(gameService.getGames(genre, page, size))
                .build();
    }

    @GetMapping("/{id}")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Get game by id successfully"
            )
    })
    public ApiResponse<GameResponse> getGameById(@PathVariable("id") String gameId) {
        log.info("Getting Game with ID: {}", gameId);

        return ApiResponse.<GameResponse>builder()
                .result(gameService.getGameById(gameId))
                .build();
    }

    @PutMapping("/{id}")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Game updated successfully"
            )
    })
    public ApiResponse<GameResponse> updateGame(
            @PathVariable("id") String gameId,
            @RequestBody @Valid GameUpdateRequest request) {
        log.info("Updating Game with ID: {}", gameId);

        ApiResponse<GameResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(gameService.updateGame(request, gameId));
        return apiResponse;
    }

    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Game deleted successfully"
            )
    })
    public ApiResponse<Void> deleteGame(@PathVariable("id") String gameId) {
        log.info("Deleting Game with ID: {}", gameId);

        gameService.deleteGame(gameId);

        return ApiResponse.<Void>builder()
                .message("Game deleted successfully")
                .build();
    }

    @PostMapping("/check-compatible")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "List all games with compatibility status for the given PC"
            )
    })
    public ApiResponse<GameCompatListResponse> checkCompatibleGames(
            @RequestBody @Valid GameCompatCheckRequest request
    ) {
        GameCompatListResponse result = gameCompatibilityService.checkCompatibleGames(request);
        return ApiResponse.<GameCompatListResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/{id}/check-compatibility")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Check game compatibility successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "Compatibility Result",
                                    value = """
                                            {
                                              "code": 1000,
                                              "message": "Success",
                                              "result": {
                                                "game": { "id": "uuid", "name": "Cyberpunk 2077" },
                                                "pcSummary": {
                                                  "cpuName": "Ryzen 5 5600",
                                                  "cpuScore": 22000,
                                                  "gpuName": "RTX 3060",
                                                  "gpuScore": 16747,
                                                  "ramGb": 16
                                                },
                                                "compatibility": "RECOMMENDED",
                                                "message": "Cấu hình của bạn đạt mức Recommended cho game này."
                                              }
                                            }
                                            """
                            )
                    )
            )
    })
    public ApiResponse<GameCompatibilityResponse> checkGameCompatibility(
            @PathVariable("id") String gameId,
            @RequestBody @Valid GameCompatibilityRequest request
    ) {
        GameCompatibilityResponse result = gameCompatibilityService.checkCompatibility(gameId, request);
        return ApiResponse.<GameCompatibilityResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/{id}/estimate-fps")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Estimate FPS successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "FPS Estimate Result",
                                    value = """
                                            {
                                              "code": 1000,
                                              "message": "Success",
                                              "result": {
                                                "game": { "id": "uuid", "name": "Cyberpunk 2077" },
                                                "pcSummary": {
                                                  "cpuName": "Ryzen 5 5600",
                                                  "cpuScore": 22000,
                                                  "gpuName": "RTX 3060",
                                                  "gpuScore": 16747,
                                                  "limitingComponent": "GPU"
                                                },
                                                "resolution": "1080p",
                                                "fpsEstimates": {
                                                  "low": { "estimatedFps": 95, "verdict": "EXCELLENT", "message": "Chơi rất tốt ở low." },
                                                  "medium": { "estimatedFps": 58, "verdict": "PLAYABLE", "message": "Chơi ổn ở medium. Có thể cần tối ưu một số tuỳ chọn." },
                                                  "high": { "estimatedFps": 35, "verdict": "BELOW_TARGET", "message": "FPS hơi thấp ở high. Khuyến nghị giảm setting hoặc nâng cấp phần cứng." }
                                                },
                                                "upgradeAdvice": "GPU RTX 3060 là điểm giới hạn chính. Nâng GPU sẽ cải thiện FPS rõ rệt."
                                              }
                                            }
                                            """
                            )
                    )
            )
    })
    public ApiResponse<FpsEstimateResponse> estimateFps(
            @PathVariable("id") String gameId,
            @RequestBody @Valid FpsEstimateRequest request
    ) {
        FpsEstimateResponse result = fpsEstimatorService.estimateFps(gameId, request);
        return ApiResponse.<FpsEstimateResponse>builder()
                .result(result)
                .build();
    }
}

