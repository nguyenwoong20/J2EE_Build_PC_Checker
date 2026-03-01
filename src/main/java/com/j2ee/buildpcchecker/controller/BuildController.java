package com.j2ee.buildpcchecker.controller;

import com.j2ee.buildpcchecker.dto.request.ApiResponse;
import com.j2ee.buildpcchecker.dto.request.BuildCheckRequest;
import com.j2ee.buildpcchecker.dto.response.CompatibilityResult;
import com.j2ee.buildpcchecker.service.CompatibilityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}


