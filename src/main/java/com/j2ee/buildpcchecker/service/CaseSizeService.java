package com.j2ee.buildpcchecker.service;

import com.j2ee.buildpcchecker.dto.request.CaseSizeRequest;
import com.j2ee.buildpcchecker.dto.response.CaseSizeResponse;
import com.j2ee.buildpcchecker.entity.CaseSize;
import com.j2ee.buildpcchecker.exception.AppException;
import com.j2ee.buildpcchecker.exception.ErrorCode;
import com.j2ee.buildpcchecker.mapper.CaseSizeMapper;
import com.j2ee.buildpcchecker.repository.CaseSizeRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CaseSizeService {

    CaseSizeRepository caseSizeRepository;
    CaseSizeMapper caseSizeMapper;

    /**
     * Create a new Case Size
     * @param request Case Size creation request
     * @return CaseSizeResponse
     */
    public CaseSizeResponse createCaseSize(CaseSizeRequest request) {
        log.info("Creating new Case Size: {}", request.getId());

        // Check if case size already exists
        if (caseSizeRepository.existsById(request.getId())) {
            log.error("Case Size already exists with ID: {}", request.getId());
            throw new AppException(ErrorCode.CASE_SIZE_ALREADY_EXISTS);
        }

        CaseSize caseSize = caseSizeMapper.toCaseSize(request);
        CaseSize savedCaseSize = caseSizeRepository.save(caseSize);

        log.info("Case Size created successfully with ID: {}", savedCaseSize.getId());
        return caseSizeMapper.toCaseSizeResponse(savedCaseSize);
    }

    /**
     * Get all Case Sizes
     * @return List of CaseSizeResponse
     */
    public List<CaseSizeResponse> getAllCaseSizes() {
        log.info("Getting all Case Sizes");
        List<CaseSize> caseSizes = caseSizeRepository.findAll();
        return caseSizeMapper.toListCaseSizeResponse(caseSizes);
    }

    /**
     * Get Case Size by ID
     * @param caseSizeId Case Size ID
     * @return CaseSizeResponse
     */
    public CaseSizeResponse getCaseSizeById(String caseSizeId) {
        log.info("Getting Case Size with ID: {}", caseSizeId);

        CaseSize caseSize = caseSizeRepository.findById(caseSizeId)
                .orElseThrow(() -> {
                    log.error("Case Size not found with ID: {}", caseSizeId);
                    return new AppException(ErrorCode.CASE_SIZE_NOT_FOUND);
                });

        return caseSizeMapper.toCaseSizeResponse(caseSize);
    }

    /**
     * Update Case Size by ID
     * @param request Case Size update request
     * @param caseSizeId Case Size ID
     * @return CaseSizeResponse
     */
    public CaseSizeResponse updateCaseSize(CaseSizeRequest request, String caseSizeId) {
        log.info("Updating Case Size with ID: {}", caseSizeId);

        CaseSize caseSize = caseSizeRepository.findById(caseSizeId)
                .orElseThrow(() -> {
                    log.error("Case Size not found with ID: {}", caseSizeId);
                    return new AppException(ErrorCode.CASE_SIZE_NOT_FOUND);
                });

        caseSizeMapper.updateCaseSize(caseSize, request);
        CaseSize updatedCaseSize = caseSizeRepository.save(caseSize);

        log.info("Case Size updated successfully with ID: {}", updatedCaseSize.getId());
        return caseSizeMapper.toCaseSizeResponse(updatedCaseSize);
    }

    /**
     * Delete Case Size by ID
     * @param caseSizeId Case Size ID
     */
    public void deleteCaseSize(String caseSizeId) {
        log.info("Deleting Case Size with ID: {}", caseSizeId);

        CaseSize caseSize = caseSizeRepository.findById(caseSizeId)
                .orElseThrow(() -> {
                    log.error("Case Size not found with ID: {}", caseSizeId);
                    return new AppException(ErrorCode.CASE_SIZE_NOT_FOUND);
                });

        caseSizeRepository.delete(caseSize);
        log.info("Case Size deleted successfully with ID: {}", caseSizeId);
    }
}

