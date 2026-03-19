package com.j2ee.buildpcchecker.service;

import com.j2ee.buildpcchecker.dto.request.SaveBuildRequest;
import com.j2ee.buildpcchecker.dto.response.PcBuildResponse;
import com.j2ee.buildpcchecker.entity.PcBuild;
import com.j2ee.buildpcchecker.entity.PcBuildPart;
import com.j2ee.buildpcchecker.entity.User;
import com.j2ee.buildpcchecker.exception.AppException;
import com.j2ee.buildpcchecker.exception.ErrorCode;
import com.j2ee.buildpcchecker.repository.*;
import com.j2ee.buildpcchecker.mapper.*;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.j2ee.buildpcchecker.dto.request.BuildCheckRequest;
import com.j2ee.buildpcchecker.dto.response.CompatibilityResult;
import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class BuildService {

    PcBuildRepository pcBuildRepository;
    PcBuildPartRepository pcBuildPartRepository;
    UserRepository userRepository;
    CompatibilityService compatibilityService;

    // Component Repositories
    CpuRepository cpuRepository;
    MainboardRepository mainboardRepository;
    RamRepository ramRepository;
    VgaRepository vgaRepository;
    PsuRepository psuRepository;
    CaseRepository caseRepository;
    CoolerRepository coolerRepository;
    SsdRepository ssdRepository;
    HddRepository hddRepository;

    // Component Mappers
    CpuMapper cpuMapper;
    MainboardMapper mainboardMapper;
    RamMapper ramMapper;
    VgaMapper vgaMapper;
    PsuMapper psuMapper;
    CaseMapper caseMapper;
    CoolerMapper coolerMapper;
    SsdMapper ssdMapper;
    HddMapper hddMapper;

    @Transactional
    public PcBuildResponse saveBuild(SaveBuildRequest request) {
        log.info("Saving PC build with name: {}", request.getName());

        // Step 1: Get authenticated user from SecurityContext
        var context = SecurityContextHolder.getContext().getAuthentication();
        String email = context.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (pcBuildRepository.existsByUserIdAndName(user.getId(), request.getName())) {
            throw new AppException(ErrorCode.BUILD_NAME_ALREADY_EXISTS);
        }

        // Validate compatibility before saving
        Map<String, String> parts = request.getParts();
        if (parts != null && !parts.isEmpty()) {
            BuildCheckRequest checkRequest = BuildCheckRequest.builder()
                    .cpuId(parts.getOrDefault("cpu", parts.get("CPU")))
                    .mainboardId(parts.getOrDefault("mainboard", parts.get("MAINBOARD")))
                    .ramId(parts.getOrDefault("ram", parts.get("RAM")))
                    .vgaId(parts.getOrDefault("vga", parts.get("VGA")))
                    .psuId(parts.getOrDefault("psu", parts.get("PSU")))
                    .caseId(parts.getOrDefault("case", parts.get("CASE")))
                    .coolerId(parts.getOrDefault("cooler", parts.get("COOLER")))
                    .build();

            String ssdId = parts.getOrDefault("ssd", parts.get("SSD"));
            if (ssdId != null && !ssdId.isBlank()) {
                checkRequest.setSsdIds(Collections.singletonList(ssdId));
            }

            String hddId = parts.getOrDefault("hdd", parts.get("HDD"));
            if (hddId != null && !hddId.isBlank()) {
                checkRequest.setHddIds(Collections.singletonList(hddId));
            }

            CompatibilityResult compatibilityResult = compatibilityService.checkCompatibility(checkRequest);
            if (!compatibilityResult.isCompatible()) {
                throw new AppException(ErrorCode.BUILD_INCOMPATIBLE);
            }
        }

        // Step 2: Create PcBuild entity
        PcBuild pcBuild = PcBuild.builder()
                .name(request.getName())
                .description(request.getDescription())
                .user(user)
                .build();

        // Step 3: Save PcBuild
        PcBuild savedBuild = pcBuildRepository.save(pcBuild);
        log.info("Saved PcBuild with ID: {}", savedBuild.getId());

        // Step 4: Loop through request.parts and create PcBuildPart entities
        if (request.getParts() != null && !request.getParts().isEmpty()) {
            for (Map.Entry<String, String> entry : request.getParts().entrySet()) {
                PcBuildPart buildPart = PcBuildPart.builder()
                        .partType(entry.getKey().toUpperCase())
                        .partId(entry.getValue())
                        .quantity(1)
                        .build(savedBuild)
                        .build();

                pcBuildPartRepository.save(buildPart);
                savedBuild.getParts().add(buildPart);
            }
            log.info("Saved {} parts for build ID: {}", savedBuild.getParts().size(), savedBuild.getId());
        }

        return convertToResponse(savedBuild);
    }

    public PcBuildResponse getBuildById(String buildId) {
        log.info("Getting PC build with ID: {}", buildId);

        PcBuild build = pcBuildRepository.findById(buildId)
                .orElseThrow(() -> new AppException(ErrorCode.BUILD_NOT_FOUND));

        // Check if user has access to this build
        var context = SecurityContextHolder.getContext().getAuthentication();
        String email = context.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (!build.getUser().getId().equals(user.getId())) {
            throw new AppException(ErrorCode.BUILD_UNAUTHORIZED_ACCESS);
        }

        return convertToResponse(build);
    }

    public List<PcBuildResponse> getMyBuilds() {
        log.info("Getting all builds for current user");

        var context = SecurityContextHolder.getContext().getAuthentication();
        String email = context.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        List<PcBuild> builds = pcBuildRepository.findByUserId(user.getId());

        return builds.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteBuild(String buildId) {
        log.info("Deleting PC build with ID: {}", buildId);

        PcBuild build = pcBuildRepository.findById(buildId)
                .orElseThrow(() -> new AppException(ErrorCode.BUILD_NOT_FOUND));

        // Check if user has access to this build
        var context = SecurityContextHolder.getContext().getAuthentication();
        String email = context.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (!build.getUser().getId().equals(user.getId())) {
            throw new AppException(ErrorCode.BUILD_UNAUTHORIZED_ACCESS);
        }

        pcBuildRepository.delete(build);
        log.info("Deleted PC build with ID: {}", buildId);
    }

    private PcBuildResponse convertToResponse(PcBuild build) {
        Map<String, Object> tempPartsMap = new HashMap<>();

        if (build.getParts() != null) {
            for (PcBuildPart part : build.getParts()) {
                String partId = part.getPartId();
                String partType = part.getPartType();
                Object partDetail = null;

                try {
                    switch (partType) {
                        case "CPU":
                            partDetail = cpuRepository.findById(partId)
                                    .map(cpuMapper::toCpuResponse).orElse(null);
                            break;
                        case "MAINBOARD":
                            partDetail = mainboardRepository.findById(partId)
                                    .map(mainboardMapper::toMainboardResponse).orElse(null);
                            break;
                        case "RAM":
                            partDetail = ramRepository.findById(partId)
                                    .map(ramMapper::toRamResponse).orElse(null);
                            break;
                        case "VGA":
                        case "GPU":
                            partDetail = vgaRepository.findById(partId)
                                    .map(vgaMapper::toVgaResponse).orElse(null);
                            break;
                        case "PSU":
                            partDetail = psuRepository.findById(partId)
                                    .map(psuMapper::toPsuResponse).orElse(null);
                            break;
                        case "CASE":
                            partDetail = caseRepository.findById(partId)
                                    .map(caseMapper::toCaseResponse).orElse(null);
                            break;
                        case "COOLER":
                            partDetail = coolerRepository.findById(partId)
                                    .map(coolerMapper::toCoolerResponse).orElse(null);
                            break;
                        case "SSD":
                            partDetail = ssdRepository.findById(partId)
                                    .map(ssdMapper::toSsdResponse).orElse(null);
                            break;
                        case "HDD":
                            partDetail = hddRepository.findById(partId)
                                    .map(hddMapper::toHddResponse).orElse(null);
                            break;
                        default:
                            partDetail = partId; // Fallback to ID only
                    }
                } catch (Exception e) {
                    log.error("Error fetching part details for type {} and ID {}", partType, partId, e);
                }

                // If detail not found (deleted) or error, fallback to ID
                if (partDetail == null) {
                    partDetail = partId;
                }

                tempPartsMap.put(partType, partDetail);
            }
        }

        // Sort parts in desired order
        Map<String, Object> orderedPartsMap = new LinkedHashMap<>();
        String[] sortedKeys = {"CPU", "MAINBOARD", "RAM", "VGA", "GPU", "SSD", "HDD", "PSU", "COOLER", "CASE"};

        for (String key : sortedKeys) {
            if (tempPartsMap.containsKey(key)) {
                orderedPartsMap.put(key, tempPartsMap.get(key));
                tempPartsMap.remove(key);
            }
        }

        // Add remaining parts (if any)
        if (!tempPartsMap.isEmpty()) {
            orderedPartsMap.putAll(tempPartsMap);
        }

        return PcBuildResponse.builder()
                .id(build.getId())
                .name(build.getName())
                .description(build.getDescription())
                .createdAt(build.getCreatedAt())
                .userId(build.getUser().getId())
                .parts(orderedPartsMap)
                .build();
    }
}
