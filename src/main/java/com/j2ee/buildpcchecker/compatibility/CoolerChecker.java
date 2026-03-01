package com.j2ee.buildpcchecker.compatibility;

import com.j2ee.buildpcchecker.dto.response.CompatibilityResult;
import com.j2ee.buildpcchecker.entity.Cooler;
import com.j2ee.buildpcchecker.entity.Cpu;
import com.j2ee.buildpcchecker.entity.PcCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * LAYER 5: COOLER Compatibility Checker
 */
@Component
@RequiredArgsConstructor
public class CoolerChecker {

    public void check(Cooler cooler, Cpu cpu, PcCase pcCase, CompatibilityResult result) {
        if (cooler == null || cpu == null) {
            return;
        }

        // Check TDP support
        if (cpu.getTdp() > cooler.getTdpSupport()) {
            result.addError(String.format(
                    CompatibilityMessages.COOLER_TDP_INSUFFICIENT,
                    cooler.getTdpSupport(),
                    cpu.getTdp()
            ));
        }

        if (pcCase == null) {
            return;
        }

        String coolerTypeId = cooler.getCoolerType().getId().toUpperCase();

        if (coolerTypeId.contains("AIR") && cooler.getHeightMm() != null) {
            if (cooler.getHeightMm() > pcCase.getMaxCoolerHeightMm()) {
                result.addError(String.format(
                        CompatibilityMessages.COOLER_AIR_HEIGHT_EXCEEDS,
                        cooler.getHeightMm(),
                        pcCase.getMaxCoolerHeightMm()
                ));
            }
        } else if (coolerTypeId.contains("AIO") && cooler.getRadiatorSize() != null) {
            if (cooler.getRadiatorSize() > pcCase.getMaxRadiatorSize()) {
                result.addError(String.format(
                        CompatibilityMessages.COOLER_AIO_RADIATOR_INCOMPATIBLE,
                        cooler.getRadiatorSize(),
                        pcCase.getMaxRadiatorSize()
                ));
            }
        }
    }
}

