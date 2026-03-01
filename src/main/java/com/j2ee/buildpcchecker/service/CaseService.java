package com.j2ee.buildpcchecker.service;

import com.j2ee.buildpcchecker.dto.request.CaseCreationRequest;
import com.j2ee.buildpcchecker.dto.request.CaseUpdateRequest;
import com.j2ee.buildpcchecker.dto.response.CaseResponse;
import com.j2ee.buildpcchecker.entity.CaseSize;
import com.j2ee.buildpcchecker.entity.PcCase;
import com.j2ee.buildpcchecker.exception.AppException;
import com.j2ee.buildpcchecker.exception.ErrorCode;
import com.j2ee.buildpcchecker.mapper.CaseMapper;
import com.j2ee.buildpcchecker.repository.CaseRepository;
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
public class CaseService {

    CaseRepository caseRepository;
    CaseMapper caseMapper;
    CaseSizeRepository caseSizeRepository;

    /**
     * Create a new Case
     * @param request Case creation request
     * @return CaseResponse
     */
    public CaseResponse createCase(CaseCreationRequest request) {
        log.info("Creating new Case: {}", request.getName());

        // Check if Case name already exists
        if (caseRepository.existsByName(request.getName())) {
            log.error("Case name already exists: {}", request.getName());
            throw new AppException(ErrorCode.CASE_NAME_ALREADY_EXISTS);
        }

        // Get CaseSize
        CaseSize caseSize = caseSizeRepository.findById(request.getSizeId())
                .orElseThrow(() -> {
                    log.error("Case Size not found with ID: {}", request.getSizeId());
                    return new AppException(ErrorCode.CASE_SIZE_NOT_FOUND);
                });

        PcCase pcCase = caseMapper.toPcCase(request);
        pcCase.setSize(caseSize);

        PcCase savedCase = caseRepository.save(pcCase);

        log.info("Case created successfully with ID: {}", savedCase.getId());
        return caseMapper.toCaseResponse(savedCase);
    }

    /**
     * Get all Cases
     * @return List of CaseResponse
     */
    public List<CaseResponse> getAllCases() {
        log.info("Getting all Cases");
        List<PcCase> cases = caseRepository.findAll();
        return caseMapper.toListCaseResponse(cases);
    }

    /**
     * Get Case by ID
     * @param caseId Case ID
     * @return CaseResponse
     */
    public CaseResponse getCaseById(String caseId) {
        log.info("Getting Case with ID: {}", caseId);

        PcCase pcCase = caseRepository.findById(caseId)
                .orElseThrow(() -> {
                    log.error("Case not found with ID: {}", caseId);
                    return new AppException(ErrorCode.CASE_NOT_FOUND);
                });

        return caseMapper.toCaseResponse(pcCase);
    }

    /**
     * Update Case by ID
     * @param request Case update request
     * @param caseId Case ID
     * @return CaseResponse
     */
    public CaseResponse updateCase(CaseUpdateRequest request, String caseId) {
        log.info("Updating Case with ID: {}", caseId);

        PcCase pcCase = caseRepository.findById(caseId)
                .orElseThrow(() -> {
                    log.error("Case not found with ID: {}", caseId);
                    return new AppException(ErrorCode.CASE_NOT_FOUND);
                });

        // Update CaseSize if sizeId is provided
        if (request.getSizeId() != null) {
            CaseSize caseSize = caseSizeRepository.findById(request.getSizeId())
                    .orElseThrow(() -> {
                        log.error("Case Size not found with ID: {}", request.getSizeId());
                        return new AppException(ErrorCode.CASE_SIZE_NOT_FOUND);
                    });
            pcCase.setSize(caseSize);
        }

        caseMapper.updatePcCase(pcCase, request);
        PcCase updatedCase = caseRepository.save(pcCase);

        log.info("Case updated successfully with ID: {}", updatedCase.getId());
        return caseMapper.toCaseResponse(updatedCase);
    }

    /**
     * Delete Case by ID
     * @param caseId Case ID
     */
    public void deleteCase(String caseId) {
        log.info("Deleting Case with ID: {}", caseId);

        PcCase pcCase = caseRepository.findById(caseId)
                .orElseThrow(() -> {
                    log.error("Case not found with ID: {}", caseId);
                    return new AppException(ErrorCode.CASE_NOT_FOUND);
                });

        caseRepository.delete(pcCase);
        log.info("Case deleted successfully with ID: {}", caseId);
    }
}
