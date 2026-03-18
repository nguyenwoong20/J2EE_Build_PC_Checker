package com.j2ee.buildpcchecker.compatibility;

import com.j2ee.buildpcchecker.dto.response.CompatibilityResult;
import com.j2ee.buildpcchecker.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * LAYER 6: POWER Compatibility Checker
 *
 * Validates:
 * - Total system power consumption
 * - PSU wattage sufficiency
 * - PCIe power connectors for VGA
 * - SATA power connectors for storage
 *
 * Formula: recommendedWattage = totalTdp * 1.2
 */
@Component
@RequiredArgsConstructor
public class PowerChecker {

    // private static final int SYSTEM_OVERHEAD = 75; // W
    private static final double SAFETY_MARGIN = 1.7;

    public void check(Psu psu, Cpu cpu, Vga vga, Ram ram, List<Ssd> ssds, List<Hdd> hdds,
            CompatibilityResult result) {

        int totalTdp = calculateTotalTdp(cpu, vga, ram, ssds, hdds);
        int recommended = totalTdp;

        result.setRecommendedPsuWattage(recommended);

        if (psu == null) {
            return; // Just set recommended, no validation
        }

        checkWattage(psu, recommended, result);
        checkPcieConnector(psu, vga, result);
        checkSataConnectors(psu, ssds, hdds, result);
    }

    private int calculateTotalTdp(Cpu cpu, Vga vga, Ram ram, List<Ssd> ssds, List<Hdd> hdds) {
        int total = 0;

        if (cpu != null) {
            total += cpu.getTdp();
        }

        if (vga != null) {
            total += vga.getTdp();
        }

        if (ram != null) {
            total += ram.getTdp() * ram.getQuantity();
        }

        if (ssds != null) {
            total += ssds.stream().mapToInt(Ssd::getTdp).sum();
        }

        if (hdds != null) {
            total += hdds.stream().mapToInt(Hdd::getTdp).sum();
        }

        total += 50;
        return (int) Math.ceil((total * SAFETY_MARGIN) / 50) * 50;
    }

    private void checkWattage(Psu psu, int recommended, CompatibilityResult result) {
        if (psu.getWattage() < recommended) {
            result.addError(String.format(
                    CompatibilityMessages.PSU_WATTAGE_INSUFFICIENT,
                    psu.getWattage(),
                    recommended));
        }
    }

    private void checkPcieConnector(Psu psu, Vga vga, CompatibilityResult result) {
        if (vga == null || vga.getPowerConnector() == null) {
            return;
        }

        // Check if PSU has the required power connector
        boolean hasConnector = psu.getPcieConnectors().stream()
                .anyMatch(connector -> connector.getId().equals(vga.getPowerConnector().getId()));

        if (!hasConnector) {
            result.addError(String.format(
                    CompatibilityMessages.PSU_PCIE_CONNECTOR_MISSING,
                    vga.getPowerConnector().getName()));
        }
    }

    private void checkSataConnectors(Psu psu, List<Ssd> ssds, List<Hdd> hdds, CompatibilityResult result) {
        int sataDriveCount = 0;

        if (ssds != null) {
            sataDriveCount += (int) ssds.stream()
                    .filter(ssd -> ssd.getInterfaceType().getName().contains("SATA"))
                    .count();
        }

        if (hdds != null) {
            sataDriveCount += (int) hdds.stream()
                    .filter(hdd -> hdd.getInterfaceType().getName().contains("SATA"))
                    .count();
        }

        if (sataDriveCount > psu.getSataConnector()) {
            result.addError(String.format(
                    CompatibilityMessages.PSU_SATA_CONNECTOR_INSUFFICIENT,
                    psu.getSataConnector(),
                    sataDriveCount));
        }
    }
}
