package com.j2ee.buildpcchecker.compatibility;

import com.j2ee.buildpcchecker.dto.response.CompatibilityResult;
import com.j2ee.buildpcchecker.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * LAYER 4: STORAGE Compatibility Checker
 *
 * Validates:
 * - M.2 slot sufficiency
 * - SATA port sufficiency (SSD SATA + HDD)
 * - Case drive bay sufficiency (3.5" and 2.5")
 */
@Component
@RequiredArgsConstructor
public class StorageChecker {

    public void check(List<Ssd> ssds, List<Hdd> hdds, Mainboard mainboard, PcCase pcCase, CompatibilityResult result) {
        if (mainboard == null) {
            return;
        }

        checkM2Slots(ssds, mainboard, result);
        checkSataSlots(ssds, hdds, mainboard, result);

        if (pcCase != null) {
            checkCaseDriveBays(ssds, hdds, pcCase, result);
        }
    }

    private void checkM2Slots(List<Ssd> ssds, Mainboard mainboard, CompatibilityResult result) {
        if (ssds == null || ssds.isEmpty()) {
            return;
        }

        long m2Count = ssds.stream()
                .filter(ssd -> ssd.getFormFactor().getName().contains("M.2"))
                .count();

        if (mainboard.getM2Slot() != null && m2Count > mainboard.getM2Slot()) {
            result.addError(String.format(
                    CompatibilityMessages.STORAGE_M2_SLOT_INSUFFICIENT,
                    m2Count,
                    mainboard.getM2Slot()
            ));
        }
    }

    private void checkSataSlots(List<Ssd> ssds, List<Hdd> hdds, Mainboard mainboard, CompatibilityResult result) {
        int sataSsdCount = 0;
        if (ssds != null) {
            sataSsdCount = (int) ssds.stream()
                    .filter(ssd -> ssd.getInterfaceType().getName().contains("SATA"))
                    .count();
        }

        int sataHddCount = 0;
        if (hdds != null) {
            sataHddCount = (int) hdds.stream()
                    .filter(hdd -> hdd.getInterfaceType().getName().contains("SATA"))
                    .count();
        }

        int totalSata = sataSsdCount + sataHddCount;

        if (mainboard.getSataSlot() != null && totalSata > mainboard.getSataSlot()) {
            result.addError(String.format(
                    CompatibilityMessages.STORAGE_SATA_SLOT_INSUFFICIENT,
                    sataSsdCount,
                    sataHddCount,
                    totalSata,
                    mainboard.getSataSlot()
            ));
        }
    }

    private void checkCaseDriveBays(List<Ssd> ssds, List<Hdd> hdds, PcCase pcCase, CompatibilityResult result) {
        // Count 3.5" drives (typically HDDs)
        int drive35Count = 0;
        if (hdds != null) {
            drive35Count = (int) hdds.stream()
                    .filter(hdd -> hdd.getFormFactor().getName().contains("3.5"))
                    .count();
        }

        if (drive35Count > pcCase.getDrive35Slot()) {
            result.addError(String.format(
                    CompatibilityMessages.STORAGE_CASE_35_BAY_INSUFFICIENT,
                    drive35Count,
                    pcCase.getDrive35Slot()
            ));
        }

        // Count 2.5" drives (SSDs + 2.5" HDDs)
        int drive25Count = 0;
        if (ssds != null) {
            drive25Count += (int) ssds.stream()
                    .filter(ssd -> ssd.getFormFactor().getName().contains("2.5"))
                    .count();
        }
        if (hdds != null) {
            drive25Count += (int) hdds.stream()
                    .filter(hdd -> hdd.getFormFactor().getName().contains("2.5"))
                    .count();
        }

        if (drive25Count > pcCase.getDrive25Slot()) {
            result.addError(String.format(
                    CompatibilityMessages.STORAGE_CASE_25_BAY_INSUFFICIENT,
                    drive25Count,
                    pcCase.getDrive25Slot()
            ));
        }
    }
}


