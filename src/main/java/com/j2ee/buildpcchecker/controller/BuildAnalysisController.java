package com.j2ee.buildpcchecker.controller;

import com.j2ee.buildpcchecker.dto.request.ApiResponse;
import com.j2ee.buildpcchecker.dto.request.BuildCheckRequest;
import com.j2ee.buildpcchecker.dto.response.BuildComparisonResultDto;
import com.j2ee.buildpcchecker.dto.response.BuildPerformanceDto;
import com.j2ee.buildpcchecker.service.BuildComparisonService;
import com.j2ee.buildpcchecker.service.BuildEvaluationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/builds")
@RequiredArgsConstructor
@Tag(name = "Build Analysis V2", description = "New PC Build Comparison and Performance Score APIs")
public class BuildAnalysisController {

    private final BuildComparisonService comparisonService;
    private final BuildEvaluationService evaluationService;

    @Operation(summary = "Compare two PC builds")
    @PostMapping("/compare")
    public ApiResponse<BuildComparisonResultDto> compareBuilds(@RequestBody ComparisonRequest request) {
        log.info("Received request to compare two builds");
        BuildComparisonResultDto result = comparisonService.compareBuilds(request.getBuild1(), request.getBuild2());
        return ApiResponse.<BuildComparisonResultDto>builder()
                .result(result)
                .build();
    }

    @Operation(summary = "Evaluate PC build performance and tier")
    @PostMapping("/evaluate")
    public ApiResponse<BuildPerformanceDto> evaluateBuild(@RequestBody BuildCheckRequest request) {
        log.info("Received request to evaluate build");
        BuildPerformanceDto result = evaluationService.evaluateBuild(request);
        return ApiResponse.<BuildPerformanceDto>builder()
                .result(result)
                .build();
    }

    // Helper class for comparison request
    @lombok.Data
    public static class ComparisonRequest {
        private BuildCheckRequest build1;
        private BuildCheckRequest build2;
    }
}
