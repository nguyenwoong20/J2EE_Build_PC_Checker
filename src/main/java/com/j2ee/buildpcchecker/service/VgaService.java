package com.j2ee.buildpcchecker.service;

import com.j2ee.buildpcchecker.dto.request.VgaCreationRequest;
import com.j2ee.buildpcchecker.dto.request.VgaUpdateRequest;
import com.j2ee.buildpcchecker.dto.response.VgaResponse;
import com.j2ee.buildpcchecker.entity.PcieConnector;
import com.j2ee.buildpcchecker.entity.PcieVersion;
import com.j2ee.buildpcchecker.entity.Vga;
import com.j2ee.buildpcchecker.exception.AppException;
import com.j2ee.buildpcchecker.exception.ErrorCode;
import com.j2ee.buildpcchecker.mapper.VgaMapper;
import com.j2ee.buildpcchecker.repository.PcieConnectorRepository;
import com.j2ee.buildpcchecker.repository.PcieVersionRepository;
import com.j2ee.buildpcchecker.repository.VgaRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class VgaService {

    VgaRepository vgaRepository;
    VgaMapper vgaMapper;
    PcieVersionRepository pcieVersionRepository;
    PcieConnectorRepository pcieConnectorRepository;

    /**
     * Create a new VGA
     * @param request VGA creation request
     * @return VgaResponse
     */
    public VgaResponse createVga(VgaCreationRequest request) {
        log.info("Creating new VGA: {}", request.getName());

        // Check if VGA name already exists
        if (vgaRepository.existsByName(request.getName())) {
            log.error("VGA name already exists: {}", request.getName());
            throw new AppException(ErrorCode.VGA_NAME_ALREADY_EXISTS);
        }

        // Get PCIe Version
        PcieVersion pcieVersion = pcieVersionRepository.findById(request.getPcieVersionId())
                .orElseThrow(() -> {
                    log.error("PCIe Version not found with ID: {}", request.getPcieVersionId());
                    return new RuntimeException("PCIe Version not found with id: " + request.getPcieVersionId());
                });

        Vga vga = vgaMapper.toVga(request);
        vga.setPcieVersion(pcieVersion);

        // Get PowerConnector if provided (optional)
        if (request.getPowerConnectorId() != null) {
            PcieConnector powerConnector = pcieConnectorRepository.findById(request.getPowerConnectorId())
                    .orElseThrow(() -> {
                        log.error("PCIe Connector not found with ID: {}", request.getPowerConnectorId());
                        return new AppException(ErrorCode.PCIE_CONNECTOR_NOT_FOUND);
                    });
            vga.setPowerConnector(powerConnector);
        }

        Vga savedVga = vgaRepository.save(vga);

        log.info("VGA created successfully with ID: {}", savedVga.getId());
        return vgaMapper.toVgaResponse(savedVga);
    }

    /**
     * Get all VGAs
     * @return List of VgaResponse
     */
    public List<VgaResponse> getAllVgas() {
        log.info("Getting all VGAs");
        List<Vga> vgas = vgaRepository.findAll();
        return vgaMapper.toListVgaResponse(vgas);
    }

    /**
     * Get VGA by ID
     * @param vgaId VGA ID
     * @return VgaResponse
     */
    public VgaResponse getVgaById(String vgaId) {
        log.info("Getting VGA with ID: {}", vgaId);

        Vga vga = vgaRepository.findById(vgaId)
                .orElseThrow(() -> {
                    log.error("VGA not found with ID: {}", vgaId);
                    return new RuntimeException("VGA not found with id: " + vgaId);
                });

        return vgaMapper.toVgaResponse(vga);
    }

    /**
     * Update VGA by ID
     * @param request VGA update request
     * @param vgaId VGA ID
     * @return VgaResponse
     */
    public VgaResponse updateVga(VgaUpdateRequest request, String vgaId) {
        log.info("Updating VGA with ID: {}", vgaId);

        Vga vga = vgaRepository.findById(vgaId)
                .orElseThrow(() -> {
                    log.error("VGA not found with ID: {}", vgaId);
                    return new RuntimeException("VGA not found with id: " + vgaId);
                });

        vgaMapper.updateVga(vga, request);

        // Update PCIe Version if provided
        if (request.getPcieVersionId() != null) {
            PcieVersion pcieVersion = pcieVersionRepository.findById(request.getPcieVersionId())
                    .orElseThrow(() -> {
                        log.error("PCIe Version not found with ID: {}", request.getPcieVersionId());
                        return new RuntimeException("PCIe Version not found with id: " + request.getPcieVersionId());
                    });
            vga.setPcieVersion(pcieVersion);
        }

        // Update PowerConnector if provided
        if (request.getPowerConnectorId() != null) {
            PcieConnector powerConnector = pcieConnectorRepository.findById(request.getPowerConnectorId())
                    .orElseThrow(() -> {
                        log.error("PCIe Connector not found with ID: {}", request.getPowerConnectorId());
                        return new AppException(ErrorCode.PCIE_CONNECTOR_NOT_FOUND);
                    });
            vga.setPowerConnector(powerConnector);
        }

        Vga updatedVga = vgaRepository.save(vga);

        log.info("VGA updated successfully with ID: {}", updatedVga.getId());
        return vgaMapper.toVgaResponse(updatedVga);
    }

    /**
     * Delete VGA by ID
     * @param vgaId VGA ID
     */
    public void deleteVga(String vgaId) {
        log.info("Deleting VGA with ID: {}", vgaId);

        Vga vga = vgaRepository.findById(vgaId)
                .orElseThrow(() -> {
                    log.error("VGA not found with ID: {}", vgaId);
                    return new RuntimeException("VGA not found with id: " + vgaId);
                });

        vgaRepository.delete(vga);
        log.info("VGA deleted successfully with ID: {}", vgaId);
    }
}

