package com.j2ee.buildpcchecker.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MainboardCreationRequest {

    @NotBlank(message = "MAINBOARD_NAME_REQUIRED")
    String name;

    @NotBlank(message = "MAINBOARD_SOCKET_ID_REQUIRED")
    String socketId;

    @NotNull(message = "MAINBOARD_VRM_PHASE_REQUIRED")
    Integer vrmPhase;

    @NotNull(message = "MAINBOARD_CPU_TDP_SUPPORT_REQUIRED")
    Integer cpuTdpSupport;

    @NotBlank(message = "MAINBOARD_RAM_TYPE_ID_REQUIRED")
    String ramTypeId;

    @NotNull(message = "MAINBOARD_RAM_BUS_MAX_REQUIRED")
    Integer ramBusMax;

    @NotNull(message = "MAINBOARD_RAM_SLOT_REQUIRED")
    Integer ramSlot;

    @NotNull(message = "MAINBOARD_RAM_MAX_CAPACITY_REQUIRED")
    Integer ramMaxCapacity;

    @NotBlank(message = "MAINBOARD_SIZE_REQUIRED")
    String sizeId;

    @NotBlank(message = "MAINBOARD_PCIE_VGA_VERSION_ID_REQUIRED")
    String pcieVgaVersionId;

    Integer m2Slot;
    Integer sataSlot;
    String description;
}
