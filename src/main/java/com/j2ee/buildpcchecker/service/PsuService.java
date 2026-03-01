package com.j2ee.buildpcchecker.service;

import com.j2ee.buildpcchecker.dto.request.PsuCreationRequest;
import com.j2ee.buildpcchecker.dto.request.PsuUpdateRequest;
import com.j2ee.buildpcchecker.dto.response.PsuResponse;
import com.j2ee.buildpcchecker.entity.PcieConnector;
import com.j2ee.buildpcchecker.entity.Psu;
import com.j2ee.buildpcchecker.exception.AppException;
import com.j2ee.buildpcchecker.exception.ErrorCode;
import com.j2ee.buildpcchecker.mapper.PsuMapper;
import com.j2ee.buildpcchecker.repository.PcieConnectorRepository;
import com.j2ee.buildpcchecker.repository.PsuRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class PsuService {

    PsuRepository psuRepository;
    PsuMapper psuMapper;
    PcieConnectorRepository pcieConnectorRepository;

    /**
     * Create a new PSU
     * @param request PSU creation request
     * @return PsuResponse
     */
    public PsuResponse createPsu(PsuCreationRequest request) {
        log.info("Creating new PSU: {}", request.getName());

        // Check if PSU name already exists
        if (psuRepository.existsByName(request.getName())) {
            log.error("PSU name already exists: {}", request.getName());
            throw new AppException(ErrorCode.PSU_NAME_ALREADY_EXISTS);
        }

        Psu psu = psuMapper.toPsu(request);

        // Get PcieConnectors if provided
        if (request.getPcieConnectorIds() != null && !request.getPcieConnectorIds().isEmpty()) {
            Set<PcieConnector> pcieConnectors = new HashSet<>();
            for (String connectorId : request.getPcieConnectorIds()) {
                PcieConnector connector = pcieConnectorRepository.findById(connectorId)
                        .orElseThrow(() -> {
                            log.error("PCIe Connector not found with ID: {}", connectorId);
                            return new AppException(ErrorCode.PCIE_CONNECTOR_NOT_FOUND);
                        });
                pcieConnectors.add(connector);
            }
            psu.setPcieConnectors(pcieConnectors);
        }

        Psu savedPsu = psuRepository.save(psu);

        log.info("PSU created successfully with ID: {}", savedPsu.getId());
        return psuMapper.toPsuResponse(savedPsu);
    }

    /**
     * Get all PSUs
     * @return List of PsuResponse
     */
    public List<PsuResponse> getAllPsus() {
        log.info("Getting all PSUs");
        List<Psu> psus = psuRepository.findAll();
        return psuMapper.toListPsuResponse(psus);
    }

    /**
     * Get PSU by ID
     * @param psuId PSU ID
     * @return PsuResponse
     */
    public PsuResponse getPsuById(String psuId) {
        log.info("Getting PSU with ID: {}", psuId);

        Psu psu = psuRepository.findById(psuId)
                .orElseThrow(() -> {
                    log.error("PSU not found with ID: {}", psuId);
                    return new AppException(ErrorCode.PSU_NOT_FOUND);
                });

        return psuMapper.toPsuResponse(psu);
    }

    /**
     * Update PSU by ID
     * @param request PSU update request
     * @param psuId PSU ID
     * @return PsuResponse
     */
    public PsuResponse updatePsu(PsuUpdateRequest request, String psuId) {
        log.info("Updating PSU with ID: {}", psuId);

        Psu psu = psuRepository.findById(psuId)
                .orElseThrow(() -> {
                    log.error("PSU not found with ID: {}", psuId);
                    return new AppException(ErrorCode.PSU_NOT_FOUND);
                });

        psuMapper.updatePsu(psu, request);

        // Update PcieConnectors if provided
        if (request.getPcieConnectorIds() != null && !request.getPcieConnectorIds().isEmpty()) {
            Set<PcieConnector> pcieConnectors = new HashSet<>();
            for (String connectorId : request.getPcieConnectorIds()) {
                PcieConnector connector = pcieConnectorRepository.findById(connectorId)
                        .orElseThrow(() -> {
                            log.error("PCIe Connector not found with ID: {}", connectorId);
                            return new AppException(ErrorCode.PCIE_CONNECTOR_NOT_FOUND);
                        });
                pcieConnectors.add(connector);
            }
            psu.setPcieConnectors(pcieConnectors);
        }

        Psu updatedPsu = psuRepository.save(psu);

        log.info("PSU updated successfully with ID: {}", updatedPsu.getId());
        return psuMapper.toPsuResponse(updatedPsu);
    }

    /**
     * Delete PSU by ID
     * @param psuId PSU ID
     */
    public void deletePsu(String psuId) {
        log.info("Deleting PSU with ID: {}", psuId);

        Psu psu = psuRepository.findById(psuId)
                .orElseThrow(() -> {
                    log.error("PSU not found with ID: {}", psuId);
                    return new AppException(ErrorCode.PSU_NOT_FOUND);
                });

        psuRepository.delete(psu);
        log.info("PSU deleted successfully with ID: {}", psuId);
    }
}
