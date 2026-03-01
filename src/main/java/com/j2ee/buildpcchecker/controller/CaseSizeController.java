package com.j2ee.buildpcchecker.controller;

import com.j2ee.buildpcchecker.dto.request.ApiResponse;
import com.j2ee.buildpcchecker.dto.request.CaseSizeRequest;
import com.j2ee.buildpcchecker.dto.response.CaseSizeResponse;
import com.j2ee.buildpcchecker.service.CaseSizeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/case-sizes")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CaseSizeController {

    CaseSizeService caseSizeService;

    /**
     * Create a new Case Size
     * POST /case-sizes
     */
    @PostMapping
    public ApiResponse<CaseSizeResponse> createCaseSize(@RequestBody @Valid CaseSizeRequest request) {
        log.info("Creating Case Size: {}", request.getId());

        ApiResponse<CaseSizeResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(caseSizeService.createCaseSize(request));

        return apiResponse;
    }

    /**
     * Get all Case Sizes
     * GET /case-sizes
     */
    @GetMapping
    public ApiResponse<List<CaseSizeResponse>> getAllCaseSizes() {
        log.info("Getting all Case Sizes");

        ApiResponse<List<CaseSizeResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(caseSizeService.getAllCaseSizes());

        return apiResponse;
    }

    /**
     * Get Case Size by ID
     * GET /case-sizes/{id}
     */
    @GetMapping("/{id}")
    public ApiResponse<CaseSizeResponse> getCaseSizeById(@PathVariable("id") String caseSizeId) {
        log.info("Getting Case Size with ID: {}", caseSizeId);

        return ApiResponse.<CaseSizeResponse>builder()
                .result(caseSizeService.getCaseSizeById(caseSizeId))
                .build();
    }

    /**
     * Update Case Size by ID
     * PUT /case-sizes/{id}
     */
    @PutMapping("/{id}")
    public ApiResponse<CaseSizeResponse> updateCaseSize(
            @PathVariable("id") String caseSizeId,
            @RequestBody @Valid CaseSizeRequest request) {
        log.info("Updating Case Size with ID: {}", caseSizeId);

        ApiResponse<CaseSizeResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(caseSizeService.updateCaseSize(request, caseSizeId));

        return apiResponse;
    }

    /**
     * Delete Case Size by ID
     * DELETE /case-sizes/{id}
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCaseSize(@PathVariable("id") String caseSizeId) {
        log.info("Deleting Case Size with ID: {}", caseSizeId);

        caseSizeService.deleteCaseSize(caseSizeId);

        return ApiResponse.<Void>builder()
                .message("Case Size deleted successfully")
                .build();
    }
}

