package com.j2ee.buildpcchecker.service;

import com.j2ee.buildpcchecker.compatibility.*;
import com.j2ee.buildpcchecker.dto.request.BuildCheckRequest;
import com.j2ee.buildpcchecker.dto.response.CompatibilityResult;
import com.j2ee.buildpcchecker.entity.*;
import com.j2ee.buildpcchecker.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for checking PC build compatibility
 *
 * Orchestrates all compatibility checkers in proper order:
 * 1. CPU ↔ Mainboard (CORE)
 * 2. RAM
 * 3. Case + VGA
 * 4. Storage
 * 5. Cooler
 * 6. Power
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CompatibilityService {

    // Repositories
    private final CpuRepository cpuRepository;
    private final MainboardRepository mainboardRepository;
    private final RamRepository ramRepository;
    private final VgaRepository vgaRepository;
    private final SsdRepository ssdRepository;
    private final HddRepository hddRepository;
    private final PsuRepository psuRepository;
    private final CaseRepository caseRepository;
    private final CoolerRepository coolerRepository;

    // Checkers
    private final CpuMainboardChecker cpuMainboardChecker;
    private final RamChecker ramChecker;
    private final CaseChecker caseChecker;
    private final StorageChecker storageChecker;
    private final CoolerChecker coolerChecker;
    private final PowerChecker powerChecker;

    /**
     * Check compatibility of all selected components
     *
     * @param request Build configuration request
     * @return Compatibility result with errors, warnings, and recommendations
     */
    public CompatibilityResult checkCompatibility(BuildCheckRequest request) {
        log.info("Starting compatibility check for build");

        // Initialize result
        CompatibilityResult result = CompatibilityResult.builder()
                .errors(new ArrayList<>())
                .warnings(new ArrayList<>())
                .build();

        // Fetch entities from database (null-safe)
        Cpu cpu = fetchCpu(request.getCpuId());
        Mainboard mainboard = fetchMainboard(request.getMainboardId());
        Ram ram = fetchRam(request.getRamId());
        Vga vga = fetchVga(request.getVgaId());
        List<Ssd> ssds = fetchSsds(request.getSsdIds());
        List<Hdd> hdds = fetchHdds(request.getHddIds());
        Psu psu = fetchPsu(request.getPsuId());
        PcCase pcCase = fetchCase(request.getCaseId());
        Cooler cooler = fetchCooler(request.getCoolerId());

        // Execute checkers in order
        log.debug("LAYER 1: Checking CPU ↔ Mainboard compatibility");
        cpuMainboardChecker.check(cpu, mainboard, result);

        log.debug("LAYER 2: Checking RAM compatibility");
        ramChecker.check(ram, mainboard, result);

        log.debug("LAYER 3: Checking Case + VGA compatibility");
        caseChecker.check(pcCase, mainboard, vga, cpu, result);

        log.debug("LAYER 4: Checking Storage compatibility");
        storageChecker.check(ssds, hdds, mainboard, pcCase, result);

        log.debug("LAYER 5: Checking Cooler compatibility");
        coolerChecker.check(cooler, cpu, pcCase, result);

        log.debug("LAYER 6: Checking Power requirements");
        powerChecker.check(psu, cpu, vga, ram, ssds, hdds, result);

        // Set final compatibility status
        result.setCompatible(result.getErrors().isEmpty());

        log.info("Compatibility check completed: compatible={}, errors={}, warnings={}",
                result.isCompatible(), result.getErrors().size(), result.getWarnings().size());

        return result;
    }

    // ========== Fetch Methods (Null-Safe) ==========

    private Cpu fetchCpu(String id) {
        if (id == null || id.isBlank()) {
            return null;
        }
        return cpuRepository.findById(id).orElse(null);
    }

    private Mainboard fetchMainboard(String id) {
        if (id == null || id.isBlank()) {
            return null;
        }
        return mainboardRepository.findById(id).orElse(null);
    }

    private Ram fetchRam(String id) {
        if (id == null || id.isBlank()) {
            return null;
        }
        return ramRepository.findById(id).orElse(null);
    }

    private Vga fetchVga(String id) {
        if (id == null || id.isBlank()) {
            return null;
        }
        return vgaRepository.findById(id).orElse(null);
    }

    private List<Ssd> fetchSsds(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }
        return ssdRepository.findAllById(ids);
    }

    private List<Hdd> fetchHdds(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }
        return hddRepository.findAllById(ids);
    }

    private Psu fetchPsu(String id) {
        if (id == null || id.isBlank()) {
            return null;
        }
        return psuRepository.findById(id).orElse(null);
    }

    private PcCase fetchCase(String id) {
        if (id == null || id.isBlank()) {
            return null;
        }
        return caseRepository.findById(id).orElse(null);
    }

    private Cooler fetchCooler(String id) {
        if (id == null || id.isBlank()) {
            return null;
        }
        return coolerRepository.findById(id).orElse(null);
    }
}

