package com.j2ee.buildpcchecker.controller;

import com.j2ee.buildpcchecker.dto.request.AnalyzeBuildRequest;
import com.j2ee.buildpcchecker.dto.request.ApiResponse;
import com.j2ee.buildpcchecker.dto.request.BuildCheckRequest;
import com.j2ee.buildpcchecker.dto.request.SaveBuildRequest;
import com.j2ee.buildpcchecker.dto.response.BuildAnalysisResponse;
import com.j2ee.buildpcchecker.dto.response.CompatibilityResult;
import com.j2ee.buildpcchecker.dto.response.PcBuildResponse;
import com.j2ee.buildpcchecker.service.BuildAnalyzerService;
import com.j2ee.buildpcchecker.service.BuildService;
import com.j2ee.buildpcchecker.service.CompatibilityService;
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

/**
 * REST Controller for PC Build Compatibility Checking
 */
@Slf4j
@RestController
@RequestMapping("/builds")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class BuildController {

    CompatibilityService compatibilityService;
    BuildService buildService;
    BuildAnalyzerService buildAnalyzerService;

    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Compatibility check completed successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Compatible Build",
                                            value = """
                                                    {
                                                      "code": 1000,
                                                      "message": "Success",
                                                      "result": {
                                                        "compatible": true,
                                                        "errors": [],
                                                        "warnings": [
                                                          "Chỉ có 1 thanh RAM - Khuyến nghị dùng 2 thanh để kích hoạt Dual Channel"
                                                        ],
                                                        "recommendedPsuWattage": 650
                                                      }
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Incompatible Build",
                                            value = """
                                                    {
                                                      "code": 1000,
                                                      "message": "Success",
                                                      "result": {
                                                        "compatible": false,
                                                        "errors": [
                                                          "CPU socket 'LGA1700' không khớp với Mainboard socket 'AM5'",
                                                          "RAM type 'DDR5' không khớp với Mainboard RAM type 'DDR4'",
                                                          "PSU chỉ có 450W, không đủ cho hệ thống (khuyến nghị tối thiểu: 650W)"
                                                        ],
                                                        "warnings": [],
                                                        "recommendedPsuWattage": 650
                                                      }
                                                    }
                                                    """
                                    )
                            }
                    )
            )
    })
    @PostMapping("/check-compatibility")
    public ApiResponse<CompatibilityResult> checkCompatibility(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Build configuration with component IDs (all fields optional)",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BuildCheckRequest.class),
                            examples = @ExampleObject(
                                    name = "Sample Request",
                                    value = """
                                            {
                                              "cpuId": "uuid-cpu-id",
                                              "mainboardId": "uuid-mainboard-id",
                                              "ramId": "uuid-ram-id",
                                              "vgaId": "uuid-vga-id",
                                              "ssdIds": ["uuid-ssd-1", "uuid-ssd-2"],
                                              "hddIds": ["uuid-hdd-1"],
                                              "psuId": "uuid-psu-id",
                                              "caseId": "uuid-case-id",
                                              "coolerId": "uuid-cooler-id"
                                            }
                                            """
                            )
                    )
            )
            @RequestBody BuildCheckRequest request) {
        log.info("Received compatibility check request");

        CompatibilityResult result = compatibilityService.checkCompatibility(request);

        return ApiResponse.<CompatibilityResult>builder()
                .result(result)
                .build();
    }

    /**
     * Save a new PC build configuration
     * POST /builds
     */
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Build saved successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class),
                    examples = @ExampleObject(
                            name = "Success Response",
                            value = """
                                    {
                                      "code": 1000,
                                      "message": "Build saved successfully",
                                      "result": {
                                        "id": "7c9e8b5a-1234-5678-90ab-cdef12345678",
                                        "name": "Gaming Build 2026",
                                        "description": "Intel i9 + RTX 4090 Gaming Setup",
                                        "totalTdp": null,
                                        "createdAt": "2026-03-09T20:45:00",
                                        "userId": "user-uuid",
                                        "parts": {
                                          "CPU": "550e8400-e29b-41d4-a716-446655440000",
                                          "MAINBOARD": "550e8400-e29b-41d4-a716-446655440001",
                                          "RAM": "550e8400-e29b-41d4-a716-446655440002",
                                          "GPU": "550e8400-e29b-41d4-a716-446655440003",
                                          "PSU": "550e8400-e29b-41d4-a716-446655440004"
                                        }
                                      }
                                    }
                                    """
                    )
            )
    )
    @PostMapping
    public ApiResponse<PcBuildResponse> saveBuild(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "PC Build configuration with name, description, and parts map",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SaveBuildRequest.class),
                            examples = @ExampleObject(
                                    name = "Sample Build Request",
                                    value = """
                                            {
                                              "name": "Gaming Build 2026",
                                              "description": "Intel i9 + RTX 4090 Gaming Setup",
                                              "parts": {
                                                "cpu": "uuid-of-cpu",
                                                "mainboard": "uuid-of-mainboard",
                                                "ram": "uuid-of-ram",
                                                "vga": "uuid-of-vga",
                                                "psu": "uuid-of-psu",
                                                "case": "uuid-of-case",
                                                "cooler": "uuid-of-cooler",
                                                "ssd": "uuid-of-ssd",
                                                "hdd": "uuid-of-hdd"
                                              }
                                            }
                                            """
                            )
                    )
            )
            @Valid @RequestBody SaveBuildRequest request) {
        log.info("Saving new PC build with name: {}", request.getName());

        PcBuildResponse response = buildService.saveBuild(request);

        return ApiResponse.<PcBuildResponse>builder()
                .message("Build saved successfully")
                .result(response)
                .build();
    }

    /**
     * Get all builds for the current user
     * GET /builds
     */
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Builds retrieved successfully"
    )
    @GetMapping
    public ApiResponse<List<PcBuildResponse>> getMyBuilds() {
        log.info("Getting all builds for current user");

        List<PcBuildResponse> builds = buildService.getMyBuilds();

        return ApiResponse.<List<PcBuildResponse>>builder()
                .result(builds)
                .build();
    }

    /**
     * Get a specific build by ID
     * GET /builds/{id}
     */
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Build retrieved successfully"
    )
    @GetMapping("/{id}")
    public ApiResponse<PcBuildResponse> getBuildById(@PathVariable String id) {
        log.info("Getting build with ID: {}", id);

        PcBuildResponse response = buildService.getBuildById(id);

        return ApiResponse.<PcBuildResponse>builder()
                .result(response)
                .build();
    }

    /**
     * Delete a build by ID
     * DELETE /builds/{id}
     */
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Build deleted successfully"
    )
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteBuild(@PathVariable String id) {
        log.info("Deleting build with ID: {}", id);

        buildService.deleteBuild(id);

        return ApiResponse.<Void>builder()
                .message("Build deleted successfully")
                .build();
    }

    /**
     * Analyze PC build for bottleneck and power consumption
     * POST /builds/analyze
     */
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Build analysis completed successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class),
                    examples = @ExampleObject(
                            name = "Analysis Result",
                            value = """
                                    {
                                      "code": 1000,
                                      "message": "Success",
                                      "result": {
                                        "cpu": { "name": "Intel Core i3-10100F", "score": 8716 },
                                        "gpu": { "name": "GeForce RTX 3060", "score": 16747 },
                                        "results": {
                                          "1080p": {
                                            "bottleneck": true,
                                            "type": "CPU",
                                            "severity": "MEDIUM",
                                            "ratio": 0.62,
                                            "message": "CPU Intel Core i3-10100F có thể giới hạn hiệu năng của GeForce RTX 3060 khi chơi game ở độ phân giải 1080p."
                                          },
                                          "2k": {
                                            "bottleneck": true,
                                            "type": "CPU",
                                            "severity": "MEDIUM",
                                            "ratio": 0.52,
                                            "message": "CPU Intel Core i3-10100F có thể giới hạn hiệu năng của GeForce RTX 3060 khi chơi game ở độ phân giải 2K."
                                          },
                                          "4k": {
                                            "bottleneck": true,
                                            "type": "CPU",
                                            "severity": "HIGH",
                                            "ratio": 0.41,
                                            "message": "CPU Intel Core i3-10100F sẽ trở thành điểm nghẽn đáng kể cho GeForce RTX 3060 ở độ phân giải 4k."
                                          }
                                        },
                                      }
                                    }
                                    """
                    )
            )
    )
    @PostMapping("/analyze")
    public ApiResponse<BuildAnalysisResponse> analyzeBuild(@RequestBody @Valid AnalyzeBuildRequest request) {
        log.info("Analyzing build - CPU: {}, VGA: {}", request.getCpuId(), request.getVgaId());

        BuildAnalysisResponse analysisResult = buildAnalyzerService.analyzeBuild(request);

        return ApiResponse.<BuildAnalysisResponse>builder()
                .result(analysisResult)
                .build();
    }
}





