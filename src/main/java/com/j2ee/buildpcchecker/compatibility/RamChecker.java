package com.j2ee.buildpcchecker.compatibility;

import com.j2ee.buildpcchecker.dto.response.CompatibilityResult;
import com.j2ee.buildpcchecker.entity.Mainboard;
import com.j2ee.buildpcchecker.entity.Ram;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * LAYER 2: RAM Compatibility Checker
 *
 * Validates:
 * - RAM type match
 * - RAM bus compatibility
 * - RAM slot sufficiency
 * - Total capacity limits
 * - Single channel warning
 */
@Component
@RequiredArgsConstructor
public class RamChecker {

    public void check(Ram ram, Mainboard mainboard, CompatibilityResult result) {
        if (ram == null) {
            return;
        }

        // RAM-only checks: chạy khi có RAM, không cần mainboard
        checkSingleChannelWarning(ram, result);
        checkRamBusSpeedWarning(ram, result);

        // RAM + Mainboard checks: chỉ chạy khi có đủ cả 2
        if (mainboard == null) {
            return;
        }

        checkRamType(ram, mainboard, result);
        checkRamBus(ram, mainboard, result);
        checkRamSlot(ram, mainboard, result);
        checkRamCapacity(ram, mainboard, result);
    }

    private void checkRamType(Ram ram, Mainboard mainboard, CompatibilityResult result) {
        if (!ram.getRamType().getId().equals(mainboard.getRamType().getId())) {
            result.addError(String.format(
                    CompatibilityMessages.RAM_TYPE_MISMATCH,
                    ram.getRamType().getName(),
                    mainboard.getRamType().getName()
            ));
        }
    }

    private void checkRamBus(Ram ram, Mainboard mainboard, CompatibilityResult result) {
        if (ram.getRamBus() > mainboard.getRamBusMax()) {
            result.addError(String.format(
                    CompatibilityMessages.RAM_BUS_EXCEEDS,
                    ram.getRamBus(),
                    mainboard.getRamBusMax()
            ));
        }
    }

    private void checkRamSlot(Ram ram, Mainboard mainboard, CompatibilityResult result) {
        if (ram.getQuantity() > mainboard.getRamSlot()) {
            result.addError(String.format(
                    CompatibilityMessages.RAM_QUANTITY_EXCEEDS,
                    ram.getQuantity(),
                    mainboard.getRamSlot()
            ));
        }
    }

    private void checkRamCapacity(Ram ram, Mainboard mainboard, CompatibilityResult result) {
        int totalCapacity = ram.getCapacityPerStick() * ram.getQuantity();

        if (totalCapacity > mainboard.getRamMaxCapacity()) {
            result.addError(String.format(
                    CompatibilityMessages.RAM_CAPACITY_EXCEEDS,
                    totalCapacity,
                    mainboard.getRamMaxCapacity()
            ));
        }
    }

    private void checkSingleChannelWarning(Ram ram, CompatibilityResult result) {
        if (ram.getQuantity() == 1) {
            result.addWarning(CompatibilityMessages.WARNING_RAM_SINGLE_CHANNEL);
        }
    }

    private void checkRamBusSpeedWarning(Ram ram, CompatibilityResult result) {
        String ramTypeId = ram.getRamType().getId().toUpperCase();
        int ramBus = ram.getRamBus();

        if (ramTypeId.contains("DDR4") && ramBus < 3200) {
            result.addWarning(String.format(
                    CompatibilityMessages.WARNING_DDR4_BUS_TOO_LOW,
                    ramBus
            ));
        } else if (ramTypeId.contains("DDR5") && ramBus < 5600) {
            result.addWarning(String.format(
                    CompatibilityMessages.WARNING_DDR5_BUS_TOO_LOW,
                    ramBus
            ));
        }
    }
}

