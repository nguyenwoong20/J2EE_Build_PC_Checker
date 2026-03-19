package com.j2ee.buildpcchecker.controller;

import com.j2ee.buildpcchecker.dto.request.ApiResponse;
import com.j2ee.buildpcchecker.dto.request.CpuCreationRequest;
import com.j2ee.buildpcchecker.dto.request.CpuUpdateRequest;
import com.j2ee.buildpcchecker.dto.response.CpuResponse;
import com.j2ee.buildpcchecker.service.CpuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/cpus")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CpuController {

    CpuService cpuService;

    /**
     * Create a new CPU
     * POST /cpus
     */
    @PostMapping
    public ApiResponse<CpuResponse> createCpu(@RequestBody @Valid CpuCreationRequest request) {
        log.info("Creating CPU: {}", request.getName());

        ApiResponse<CpuResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(cpuService.createCpu(request));

        return apiResponse;
    }

    /**
     * Get all CPUs
     * GET /cpus
     */
    @GetMapping
    public ApiResponse<List<CpuResponse>> getAllCpus() {
        log.info("Getting all CPUs");

        ApiResponse<List<CpuResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(cpuService.getAllCpus());

        return apiResponse;
    }

    /**
     * Get CPU by ID
     * GET /cpus/{id}
     */
    @GetMapping("/{id}")
    public ApiResponse<CpuResponse> getCpuById(@PathVariable("id") String cpuId) {
        log.info("Getting CPU with ID: {}", cpuId);

        return ApiResponse.<CpuResponse>builder()
                .result(cpuService.getCpuById(cpuId))
                .build();
    }

    /**
     * Update CPU by ID
     * PUT /cpus/{id}
     */
    @PutMapping("/{id}")
    public ApiResponse<CpuResponse> updateCpu(
            @PathVariable("id") String cpuId,
            @RequestBody @Valid CpuUpdateRequest request) {
        log.info("Updating CPU with ID: {}", cpuId);

        ApiResponse<CpuResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(cpuService.updateCpu(request, cpuId));

        return apiResponse;
    }

    /**
     * Delete CPU by ID
     * DELETE /cpus/{id}
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCpu(@PathVariable("id") String cpuId) {
        log.info("Deleting CPU with ID: {}", cpuId);

        cpuService.deleteCpu(cpuId);

        return ApiResponse.<Void>builder()
                .message("CPU deleted successfully")
                .build();
    }
}
