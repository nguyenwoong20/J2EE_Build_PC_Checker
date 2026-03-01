package com.j2ee.buildpcchecker.service;

import com.j2ee.buildpcchecker.dto.request.MainboardCreationRequest;
import com.j2ee.buildpcchecker.dto.request.MainboardUpdateRequest;
import com.j2ee.buildpcchecker.dto.response.MainboardResponse;
import com.j2ee.buildpcchecker.entity.CaseSize;
import com.j2ee.buildpcchecker.entity.Mainboard;
import com.j2ee.buildpcchecker.entity.PcieVersion;
import com.j2ee.buildpcchecker.entity.RamType;
import com.j2ee.buildpcchecker.entity.Socket;
import com.j2ee.buildpcchecker.exception.AppException;
import com.j2ee.buildpcchecker.exception.ErrorCode;
import com.j2ee.buildpcchecker.mapper.MainboardMapper;
import com.j2ee.buildpcchecker.repository.CaseSizeRepository;
import com.j2ee.buildpcchecker.repository.MainboardRepository;
import com.j2ee.buildpcchecker.repository.PcieVersionRepository;
import com.j2ee.buildpcchecker.repository.RamTypeRepository;
import com.j2ee.buildpcchecker.repository.SocketRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class MainboardService {

    MainboardRepository mainboardRepository;
    MainboardMapper mainboardMapper;
    SocketRepository socketRepository;
    RamTypeRepository ramTypeRepository;
    PcieVersionRepository pcieVersionRepository;
    CaseSizeRepository caseSizeRepository;

    /**
     * Create a new Mainboard
     * @param request Mainboard creation request
     * @return MainboardResponse
     */
    public MainboardResponse createMainboard(MainboardCreationRequest request) {
        log.info("Creating new Mainboard: {}", request.getName());

        // Check if Mainboard name already exists
        if (mainboardRepository.existsByName(request.getName())) {
            log.error("Mainboard name already exists: {}", request.getName());
            throw new AppException(ErrorCode.MAINBOARD_NAME_ALREADY_EXISTS);
        }

        // Get Socket
        Socket socket = socketRepository.findById(request.getSocketId())
                .orElseThrow(() -> {
                    log.error("Socket not found with ID: {}", request.getSocketId());
                    return new RuntimeException("Socket not found with id: " + request.getSocketId());
                });

        // Get RamType
        RamType ramType = ramTypeRepository.findById(request.getRamTypeId())
                .orElseThrow(() -> {
                    log.error("RAM Type not found with ID: {}", request.getRamTypeId());
                    return new RuntimeException("RAM Type not found with id: " + request.getRamTypeId());
                });

        // Get PcieVersion for VGA
        PcieVersion pcieVgaVersion = pcieVersionRepository.findById(request.getPcieVgaVersionId())
                .orElseThrow(() -> {
                    log.error("PCIe Version not found with ID: {}", request.getPcieVgaVersionId());
                    return new RuntimeException("PCIe Version not found with id: " + request.getPcieVgaVersionId());
                });

        // Get CaseSize
        CaseSize size = caseSizeRepository.findById(request.getSizeId())
                .orElseThrow(() -> {
                    log.error("Case Size not found with ID: {}", request.getSizeId());
                    return new AppException(ErrorCode.CASE_SIZE_NOT_FOUND);
                });

        Mainboard mainboard = mainboardMapper.toMainboard(request);
        mainboard.setSocket(socket);
        mainboard.setRamType(ramType);
        mainboard.setPcieVgaVersion(pcieVgaVersion);
        mainboard.setSize(size);

        Mainboard savedMainboard = mainboardRepository.save(mainboard);

        log.info("Mainboard created successfully with ID: {}", savedMainboard.getId());
        return mainboardMapper.toMainboardResponse(savedMainboard);
    }

    /**
     * Get all Mainboards
     * @return List of MainboardResponse
     */
    public List<MainboardResponse> getAllMainboards() {
        log.info("Getting all Mainboards");
        List<Mainboard> mainboards = mainboardRepository.findAll();
        return mainboardMapper.toListMainboardResponse(mainboards);
    }

    /**
     * Get Mainboard by ID
     * @param mainboardId Mainboard ID
     * @return MainboardResponse
     */
    public MainboardResponse getMainboardById(String mainboardId) {
        log.info("Getting Mainboard with ID: {}", mainboardId);

        Mainboard mainboard = mainboardRepository.findById(mainboardId)
                .orElseThrow(() -> {
                    log.error("Mainboard not found with ID: {}", mainboardId);
                    return new RuntimeException("Mainboard not found with id: " + mainboardId);
                });

        return mainboardMapper.toMainboardResponse(mainboard);
    }

    /**
     * Update Mainboard by ID
     * @param request Mainboard update request
     * @param mainboardId Mainboard ID
     * @return MainboardResponse
     */
    public MainboardResponse updateMainboard(MainboardUpdateRequest request, String mainboardId) {
        log.info("Updating Mainboard with ID: {}", mainboardId);

        Mainboard mainboard = mainboardRepository.findById(mainboardId)
                .orElseThrow(() -> {
                    log.error("Mainboard not found with ID: {}", mainboardId);
                    return new RuntimeException("Mainboard not found with id: " + mainboardId);
                });

        mainboardMapper.updateMainboard(mainboard, request);

        // Update Socket if provided
        if (request.getSocketId() != null) {
            Socket socket = socketRepository.findById(request.getSocketId())
                    .orElseThrow(() -> {
                        log.error("Socket not found with ID: {}", request.getSocketId());
                        return new RuntimeException("Socket not found with id: " + request.getSocketId());
                    });
            mainboard.setSocket(socket);
        }

        // Update RamType if provided
        if (request.getRamTypeId() != null) {
            RamType ramType = ramTypeRepository.findById(request.getRamTypeId())
                    .orElseThrow(() -> {
                        log.error("RAM Type not found with ID: {}", request.getRamTypeId());
                        return new RuntimeException("RAM Type not found with id: " + request.getRamTypeId());
                    });
            mainboard.setRamType(ramType);
        }

        // Update PcieVgaVersion if provided
        if (request.getPcieVgaVersionId() != null) {
            PcieVersion pcieVgaVersion = pcieVersionRepository.findById(request.getPcieVgaVersionId())
                    .orElseThrow(() -> {
                        log.error("PCIe Version not found with ID: {}", request.getPcieVgaVersionId());
                        return new RuntimeException("PCIe Version not found with id: " + request.getPcieVgaVersionId());
                    });
            mainboard.setPcieVgaVersion(pcieVgaVersion);
        }

        // Update CaseSize if provided
        if (request.getSizeId() != null) {
            CaseSize size = caseSizeRepository.findById(request.getSizeId())
                    .orElseThrow(() -> {
                        log.error("Case Size not found with ID: {}", request.getSizeId());
                        return new AppException(ErrorCode.CASE_SIZE_NOT_FOUND);
                    });
            mainboard.setSize(size);
        }

        Mainboard updatedMainboard = mainboardRepository.save(mainboard);

        log.info("Mainboard updated successfully with ID: {}", updatedMainboard.getId());
        return mainboardMapper.toMainboardResponse(updatedMainboard);
    }

    /**
     * Delete Mainboard by ID
     * @param mainboardId Mainboard ID
     */
    public void deleteMainboard(String mainboardId) {
        log.info("Deleting Mainboard with ID: {}", mainboardId);

        Mainboard mainboard = mainboardRepository.findById(mainboardId)
                .orElseThrow(() -> {
                    log.error("Mainboard not found with ID: {}", mainboardId);
                    return new RuntimeException("Mainboard not found with id: " + mainboardId);
                });

        mainboardRepository.delete(mainboard);
        log.info("Mainboard deleted successfully with ID: {}", mainboardId);
    }
}

