package com.j2ee.buildpcchecker.compatibility;

import com.j2ee.buildpcchecker.dto.response.CompatibilityResult;
import com.j2ee.buildpcchecker.entity.Cpu;
import com.j2ee.buildpcchecker.entity.Mainboard;
import com.j2ee.buildpcchecker.entity.PcCase;
import com.j2ee.buildpcchecker.entity.Vga;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * LAYER 3: CASE Compatibility Checker
 *
 * Validates:
 * - Case size supports mainboard size
 * - VGA length fits in case
 * - PCIe version compatibility (with warning for downgrade)
 * - iGPU check
 * - CPU-VGA bottleneck warning
 */
@Component
@RequiredArgsConstructor
public class CaseChecker {

    public void check(PcCase pcCase, Mainboard mainboard, Vga vga, Cpu cpu, CompatibilityResult result) {
        if (pcCase == null || mainboard == null) {
            return;
        }

        checkCaseSize(pcCase, mainboard, result);
        checkVgaLength(pcCase, vga, result);
        checkPcieVersion(mainboard, vga, result);
        checkIgpu(cpu, vga, result);
    }

    private void checkCaseSize(PcCase pcCase, Mainboard mainboard, CompatibilityResult result) {
        // Assuming Case size must match or be larger than mainboard size
        // ATX case can hold ATX, mATX, ITX
        // mATX case can hold mATX, ITX
        // ITX case can only hold ITX

        if (!pcCase.getSize().getId().equals(mainboard.getSize().getId())) {
            // For simplicity, if they don't match exactly, add error
            // In real scenario, you'd check size hierarchy (ATX > mATX > ITX)
            String caseSizeName = pcCase.getSize().getName();
            String mainboardSizeName = mainboard.getSize().getName();

            // Simple check: if case is smaller form factor than mainboard
            if (isIncompatibleSize(caseSizeName, mainboardSizeName)) {
                result.addError(String.format(
                        CompatibilityMessages.CASE_SIZE_INCOMPATIBLE,
                        mainboardSizeName,
                        caseSizeName
                ));
            }
        }
    }

    //hard code size
    private boolean isIncompatibleSize(String caseSize, String mainboardSize) {
        // ITX case can't hold ATX or mATX
        if (caseSize.contains("ITX") && (mainboardSize.contains("ATX") && !mainboardSize.contains("ITX"))) {
            return true;
        }
        // mATX case can't hold ATX
        if (caseSize.contains("mATX") && mainboardSize.contains("ATX") && !mainboardSize.contains("mATX")) {
            return true;
        }
        return false;
    }

    private void checkVgaLength(PcCase pcCase, Vga vga, CompatibilityResult result) {
        if (vga == null) {
            return;
        }

        if (vga.getLengthMm() > pcCase.getMaxVgaLengthMm()) {
            result.addError(String.format(
                    CompatibilityMessages.VGA_LENGTH_EXCEEDS,
                    vga.getLengthMm(),
                    pcCase.getMaxVgaLengthMm()
            ));
        }
    }

    private void checkPcieVersion(Mainboard mainboard, Vga vga, CompatibilityResult result) {
        if (vga == null) {
            return;
        }

        int mainboardPcieRank = getPcieRank(mainboard.getPcieVgaVersion().getId());
        int vgaPcieRank = getPcieRank(vga.getPcieVersion().getId());

        // Backward compatible, but warn if VGA is newer
        if (vgaPcieRank > mainboardPcieRank) {
            result.addWarning(String.format(
                    CompatibilityMessages.VGA_PCIE_DOWNGRADE,
                    vga.getPcieVersion().getName(),
                    mainboard.getPcieVgaVersion().getName()
            ));
        }
    }

    private int getPcieRank(String pcieId) {
        if (pcieId.contains("3")) return 3;
        if (pcieId.contains("4")) return 4;
        if (pcieId.contains("5")) return 5;
        return 0;
    }

    private void checkIgpu(Cpu cpu, Vga vga, CompatibilityResult result) {
        if (cpu == null) {
            return;
        }

        if (vga == null && !cpu.getIgpu()) {
            result.addWarning(CompatibilityMessages.WARNING_NO_VGA_NO_IGPU);
        }
    }
}

