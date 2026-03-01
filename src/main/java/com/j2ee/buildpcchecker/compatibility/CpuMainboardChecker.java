package com.j2ee.buildpcchecker.compatibility;

import com.j2ee.buildpcchecker.dto.response.CompatibilityResult;
import com.j2ee.buildpcchecker.entity.Cpu;
import com.j2ee.buildpcchecker.entity.Mainboard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * LAYER 1: CPU ↔ MAINBOARD Compatibility Checker
 *
 * Validates:
 * - Socket compatibility
 * - VRM phase sufficiency
 * - TDP support
 */
@Component
@RequiredArgsConstructor
public class CpuMainboardChecker {

    public void check(Cpu cpu, Mainboard mainboard, CompatibilityResult result) {
        if (cpu == null || mainboard == null) {
            return; // Skip if not selected
        }

        checkSocket(cpu, mainboard, result);
        checkVrm(cpu, mainboard, result);
        checkTdp(cpu, mainboard, result);
    }

    private void checkSocket(Cpu cpu, Mainboard mainboard, CompatibilityResult result) {
        if (!cpu.getSocket().getId().equals(mainboard.getSocket().getId())) {
            result.addError(String.format(
                    CompatibilityMessages.CPU_MAINBOARD_SOCKET_MISMATCH,
                    cpu.getSocket().getName(),
                    mainboard.getSocket().getName()
            ));
        }
    }

    private void checkVrm(Cpu cpu, Mainboard mainboard, CompatibilityResult result) {
        if (cpu.getVrmMin() != null && mainboard.getVrmPhase() < cpu.getVrmMin()) {
            result.addError(String.format(
                    CompatibilityMessages.CPU_MAINBOARD_VRM_INSUFFICIENT,
                    mainboard.getVrmPhase(),
                    cpu.getVrmMin()
            ));
        }
    }

    private void checkTdp(Cpu cpu, Mainboard mainboard, CompatibilityResult result) {
        if (cpu.getTdp() > mainboard.getCpuTdpSupport()) {
            result.addError(String.format(
                    CompatibilityMessages.CPU_MAINBOARD_TDP_INSUFFICIENT,
                    mainboard.getCpuTdpSupport(),
                    cpu.getTdp()
            ));
        }
    }
}

