package com.j2ee.buildpcchecker.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode
{
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Invalid message key", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User already exists", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at {min} charactors", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004, "Password must be at least {min} charactors", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(1005, "Email is not valid", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1006, "User not exist", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1007, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1008, "You don't have permission", HttpStatus.FORBIDDEN),
    INVALID_DATE_OF_BIRTH(1009, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    INVALID_VERIFICATION_TOKEN(1010, "Invalid or expired verification token", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_VERIFIED(1011, "Email is not verified. Please verify your email first", HttpStatus.FORBIDDEN),
    EMAIL_ALREADY_VERIFIED(1012, "Email is already verified", HttpStatus.BAD_REQUEST),
    ACCOUNT_DISABLED(1013, "Account is disabled", HttpStatus.FORBIDDEN),

    // CPU validation errors (2001-2099)
    CPU_NAME_REQUIRED(2001, "CPU name is required", HttpStatus.BAD_REQUEST),
    CPU_SOCKET_ID_REQUIRED(2002, "Socket ID is required", HttpStatus.BAD_REQUEST),
    CPU_IGPU_REQUIRED(2003, "iGPU field is required", HttpStatus.BAD_REQUEST),
    CPU_TDP_REQUIRED(2004, "TDP is required", HttpStatus.BAD_REQUEST),
    CPU_PCIE_VERSION_ID_REQUIRED(2005, "PCIe Version ID is required", HttpStatus.BAD_REQUEST),
    CPU_SCORE_REQUIRED(2006, "Score is required", HttpStatus.BAD_REQUEST),
    CPU_NOT_FOUND(2007, "CPU not found", HttpStatus.NOT_FOUND),
    CPU_NAME_ALREADY_EXISTS(2008, "CPU name already exists", HttpStatus.BAD_REQUEST),

    // Mainboard validation errors (2101-2199)
    MAINBOARD_NAME_REQUIRED(2101, "Mainboard name is required", HttpStatus.BAD_REQUEST),
    MAINBOARD_SOCKET_ID_REQUIRED(2102, "Socket ID is required", HttpStatus.BAD_REQUEST),
    MAINBOARD_VRM_PHASE_REQUIRED(2103, "VRM Phase is required", HttpStatus.BAD_REQUEST),
    MAINBOARD_CPU_TDP_SUPPORT_REQUIRED(2104, "CPU TDP Support is required", HttpStatus.BAD_REQUEST),
    MAINBOARD_RAM_TYPE_ID_REQUIRED(2105, "RAM Type ID is required", HttpStatus.BAD_REQUEST),
    MAINBOARD_RAM_BUS_MAX_REQUIRED(2106, "RAM Bus Max is required", HttpStatus.BAD_REQUEST),
    MAINBOARD_RAM_SLOT_REQUIRED(2107, "RAM Slot is required", HttpStatus.BAD_REQUEST),
    MAINBOARD_RAM_MAX_CAPACITY_REQUIRED(2108, "RAM Max Capacity is required", HttpStatus.BAD_REQUEST),
    MAINBOARD_SIZE_REQUIRED(2109, "Size is required", HttpStatus.BAD_REQUEST),
    MAINBOARD_PCIE_VGA_VERSION_ID_REQUIRED(2110, "PCIe VGA Version ID is required", HttpStatus.BAD_REQUEST),
    MAINBOARD_NOT_FOUND(2111, "Mainboard not found", HttpStatus.NOT_FOUND),
    MAINBOARD_NAME_ALREADY_EXISTS(2112, "Mainboard name already exists", HttpStatus.BAD_REQUEST),

    // RAM validation errors (2201-2299)
    RAM_NAME_REQUIRED(2201, "RAM name is required", HttpStatus.BAD_REQUEST),
    RAM_TYPE_ID_REQUIRED(2202, "RAM Type ID is required", HttpStatus.BAD_REQUEST),
    RAM_BUS_REQUIRED(2203, "RAM Bus is required", HttpStatus.BAD_REQUEST),
    RAM_CAS_REQUIRED(2204, "RAM CAS is required", HttpStatus.BAD_REQUEST),
    RAM_CAPACITY_PER_STICK_REQUIRED(2205, "Capacity per stick is required", HttpStatus.BAD_REQUEST),
    RAM_QUANTITY_REQUIRED(2206, "Quantity is required", HttpStatus.BAD_REQUEST),
    RAM_TDP_REQUIRED(2207, "TDP is required", HttpStatus.BAD_REQUEST),
    RAM_NOT_FOUND(2208, "RAM not found", HttpStatus.NOT_FOUND),
    RAM_NAME_ALREADY_EXISTS(2209, "RAM name already exists", HttpStatus.BAD_REQUEST),

    // VGA validation errors (2301-2399)
    VGA_NAME_REQUIRED(2301, "VGA name is required", HttpStatus.BAD_REQUEST),
    VGA_LENGTH_REQUIRED(2302, "Length is required", HttpStatus.BAD_REQUEST),
    VGA_TDP_REQUIRED(2303, "TDP is required", HttpStatus.BAD_REQUEST),
    VGA_PCIE_VERSION_ID_REQUIRED(2304, "PCIe Version ID is required", HttpStatus.BAD_REQUEST),
    VGA_SCORE_REQUIRED(2305, "Score is required", HttpStatus.BAD_REQUEST),
    VGA_NOT_FOUND(2306, "VGA not found", HttpStatus.NOT_FOUND),
    VGA_NAME_ALREADY_EXISTS(2307, "VGA name already exists", HttpStatus.BAD_REQUEST),

    // Socket validation errors (2401-2499)
    SOCKET_ID_REQUIRED(2401, "Socket ID is required", HttpStatus.BAD_REQUEST),
    SOCKET_NAME_REQUIRED(2402, "Socket name is required", HttpStatus.BAD_REQUEST),
    SOCKET_NOT_FOUND(2403, "Socket not found", HttpStatus.NOT_FOUND),
    SOCKET_ALREADY_EXISTS(2404, "Socket already exists", HttpStatus.BAD_REQUEST),

    // RamType validation errors (2501-2599)
    RAM_TYPE_ID_VALUE_REQUIRED(2501, "RAM Type ID is required", HttpStatus.BAD_REQUEST),
    RAM_TYPE_NAME_REQUIRED(2502, "RAM Type name is required", HttpStatus.BAD_REQUEST),
    RAM_TYPE_NOT_FOUND(2503, "RAM Type not found", HttpStatus.NOT_FOUND),
    RAM_TYPE_ALREADY_EXISTS(2504, "RAM Type already exists", HttpStatus.BAD_REQUEST),

    // PcieVersion validation errors (2601-2699)
    PCIE_VERSION_ID_REQUIRED(2601, "PCIe Version ID is required", HttpStatus.BAD_REQUEST),
    PCIE_VERSION_NAME_REQUIRED(2602, "PCIe Version name is required", HttpStatus.BAD_REQUEST),
    PCIE_VERSION_NOT_FOUND(2603, "PCIe Version not found", HttpStatus.NOT_FOUND),
    PCIE_VERSION_ALREADY_EXISTS(2604, "PCIe Version already exists", HttpStatus.BAD_REQUEST),

    // SSD validation errors (2701-2799)
    SSD_NAME_REQUIRED(2701, "SSD name is required", HttpStatus.BAD_REQUEST),
    SSD_TYPE_ID_REQUIRED(2702, "SSD Type ID is required", HttpStatus.BAD_REQUEST),
    SSD_FORM_FACTOR_REQUIRED(2703, "SSD Form Factor is required", HttpStatus.BAD_REQUEST),
    SSD_INTERFACE_ID_REQUIRED(2704, "SSD Interface ID is required", HttpStatus.BAD_REQUEST),
    SSD_CAPACITY_REQUIRED(2705, "SSD Capacity is required", HttpStatus.BAD_REQUEST),
    SSD_TDP_REQUIRED(2706, "SSD TDP is required", HttpStatus.BAD_REQUEST),
    SSD_NOT_FOUND(2707, "SSD not found", HttpStatus.NOT_FOUND),
    SSD_NAME_ALREADY_EXISTS(2708, "SSD name already exists", HttpStatus.BAD_REQUEST),

    // SsdType validation errors (2711-2719)
    SSD_TYPE_ENTITY_ID_REQUIRED(2711, "SSD Type ID is required", HttpStatus.BAD_REQUEST),
    SSD_TYPE_NAME_REQUIRED(2712, "SSD Type name is required", HttpStatus.BAD_REQUEST),
    SSD_TYPE_NOT_FOUND(2713, "SSD Type not found", HttpStatus.NOT_FOUND),
    SSD_TYPE_ALREADY_EXISTS(2714, "SSD Type already exists", HttpStatus.BAD_REQUEST),

    // SsdInterface validation errors (2721-2729)
    SSD_INTERFACE_ENTITY_ID_REQUIRED(2721, "SSD Interface ID is required", HttpStatus.BAD_REQUEST),
    SSD_INTERFACE_NAME_REQUIRED(2722, "SSD Interface name is required", HttpStatus.BAD_REQUEST),
    SSD_INTERFACE_NOT_FOUND(2723, "SSD Interface not found", HttpStatus.NOT_FOUND),

    // HDD validation errors (2801-2899)
    HDD_NAME_REQUIRED(2801, "HDD name is required", HttpStatus.BAD_REQUEST),
    HDD_FORM_FACTOR_REQUIRED(2802, "HDD Form Factor is required", HttpStatus.BAD_REQUEST),
    HDD_INTERFACE_ID_REQUIRED(2803, "HDD Interface ID is required", HttpStatus.BAD_REQUEST),
    HDD_CAPACITY_REQUIRED(2804, "HDD Capacity is required", HttpStatus.BAD_REQUEST),
    HDD_TDP_REQUIRED(2805, "HDD TDP is required", HttpStatus.BAD_REQUEST),
    HDD_NOT_FOUND(2806, "HDD not found", HttpStatus.NOT_FOUND),
    HDD_NAME_ALREADY_EXISTS(2807, "HDD name already exists", HttpStatus.BAD_REQUEST),

    // HddInterface validation errors (2811-2819)
    HDD_INTERFACE_ENTITY_ID_REQUIRED(2811, "HDD Interface ID is required", HttpStatus.BAD_REQUEST),
    HDD_INTERFACE_NAME_REQUIRED(2812, "HDD Interface name is required", HttpStatus.BAD_REQUEST),
    HDD_INTERFACE_NOT_FOUND(2813, "HDD Interface not found", HttpStatus.NOT_FOUND),

    // PSU validation errors (2901-2999)
    PSU_NAME_REQUIRED(2901, "PSU name is required", HttpStatus.BAD_REQUEST),
    PSU_WATTAGE_REQUIRED(2902, "PSU Wattage is required", HttpStatus.BAD_REQUEST),
    PSU_EFFICIENCY_REQUIRED(2903, "PSU Efficiency is required", HttpStatus.BAD_REQUEST),
    PSU_SATA_CONNECTOR_REQUIRED(2904, "PSU SATA Connector count is required", HttpStatus.BAD_REQUEST),
    PSU_NOT_FOUND(2905, "PSU not found", HttpStatus.NOT_FOUND),
    PSU_NAME_ALREADY_EXISTS(2906, "PSU name already exists", HttpStatus.BAD_REQUEST),

    // PcieConnector validation errors (2911-2919)
    PCIE_CONNECTOR_ID_REQUIRED(2911, "PCIe Connector ID is required", HttpStatus.BAD_REQUEST),
    PCIE_CONNECTOR_NAME_REQUIRED(2912, "PCIe Connector name is required", HttpStatus.BAD_REQUEST),
    PCIE_CONNECTOR_NOT_FOUND(2913, "PCIe Connector not found", HttpStatus.NOT_FOUND),
    PCIE_CONNECTOR_ALREADY_EXISTS(2914, "PCIe Connector already exists", HttpStatus.BAD_REQUEST),

    // PCCase validation errors (3001-3099)
    CASE_NAME_REQUIRED(3001, "Case name is required", HttpStatus.BAD_REQUEST),
    CASE_SIZE_REQUIRED(3002, "Case Size is required", HttpStatus.BAD_REQUEST),
    CASE_MAX_VGA_LENGTH_REQUIRED(3003, "Case Max VGA Length is required", HttpStatus.BAD_REQUEST),
    CASE_MAX_COOLER_HEIGHT_REQUIRED(3004, "Case Max Cooler Height is required", HttpStatus.BAD_REQUEST),
    CASE_MAX_RADIATOR_SIZE_REQUIRED(3005, "Case Max Radiator Size is required", HttpStatus.BAD_REQUEST),
    CASE_DRIVE_35_SLOT_REQUIRED(3006, "Case 3.5\" Drive Slot count is required", HttpStatus.BAD_REQUEST),
    CASE_DRIVE_25_SLOT_REQUIRED(3007, "Case 2.5\" Drive Slot count is required", HttpStatus.BAD_REQUEST),
    CASE_NOT_FOUND(3008, "Case not found", HttpStatus.NOT_FOUND),
    CASE_NAME_ALREADY_EXISTS(3009, "Case name already exists", HttpStatus.BAD_REQUEST),

    // Cooler validation errors (3101-3199)
    COOLER_NAME_REQUIRED(3101, "Cooler name is required", HttpStatus.BAD_REQUEST),
    COOLER_TYPE_ID_REQUIRED(3102, "Cooler Type ID is required", HttpStatus.BAD_REQUEST),
    COOLER_TDP_SUPPORT_REQUIRED(3103, "Cooler TDP Support is required", HttpStatus.BAD_REQUEST),
    COOLER_NOT_FOUND(3104, "Cooler not found", HttpStatus.NOT_FOUND),
    COOLER_NAME_ALREADY_EXISTS(3105, "Cooler name already exists", HttpStatus.BAD_REQUEST),

    // CoolerType validation errors (3111-3119)
    COOLER_TYPE_ENTITY_ID_REQUIRED(3111, "Cooler Type ID is required", HttpStatus.BAD_REQUEST),
    COOLER_TYPE_NAME_REQUIRED(3112, "Cooler Type name is required", HttpStatus.BAD_REQUEST),
    COOLER_TYPE_NOT_FOUND(3113, "Cooler Type not found", HttpStatus.NOT_FOUND),
    COOLER_TYPE_ALREADY_EXISTS(3114, "Cooler Type already exists", HttpStatus.BAD_REQUEST),

    // InterfaceType validation errors (3121-3129)
    INTERFACE_TYPE_ENTITY_ID_REQUIRED(3121, "Interface Type ID is required", HttpStatus.BAD_REQUEST),
    INTERFACE_TYPE_NAME_REQUIRED(3122, "Interface Type name is required", HttpStatus.BAD_REQUEST),
    INTERFACE_TYPE_NOT_FOUND(3123, "Interface Type not found", HttpStatus.NOT_FOUND),
    INTERFACE_TYPE_ALREADY_EXISTS(3124, "Interface Type already exists", HttpStatus.BAD_REQUEST),

    // FormFactor validation errors (3131-3139)
    FORM_FACTOR_ID_REQUIRED(3131, "Form Factor ID is required", HttpStatus.BAD_REQUEST),
    FORM_FACTOR_NAME_REQUIRED(3132, "Form Factor name is required", HttpStatus.BAD_REQUEST),
    FORM_FACTOR_NOT_FOUND(3133, "Form Factor not found", HttpStatus.NOT_FOUND),
    FORM_FACTOR_ALREADY_EXISTS(3134, "Form Factor already exists", HttpStatus.BAD_REQUEST),

    // CaseSize validation errors (3141-3149)
    CASE_SIZE_ID_REQUIRED(3141, "Case Size ID is required", HttpStatus.BAD_REQUEST),
    CASE_SIZE_NAME_REQUIRED(3142, "Case Size name is required", HttpStatus.BAD_REQUEST),
    CASE_SIZE_NOT_FOUND(3143, "Case Size not found", HttpStatus.NOT_FOUND),
    CASE_SIZE_ALREADY_EXISTS(3144, "Case Size already exists", HttpStatus.BAD_REQUEST),

    // Data Integrity Violation errors (4001-4099)
    SOCKET_IN_USE_BY_CPU(4001, "Cannot delete Socket. There are CPUs using this Socket", HttpStatus.CONFLICT),
    SOCKET_IN_USE_BY_MAINBOARD(4002, "Cannot delete Socket. There are Mainboards using this Socket", HttpStatus.CONFLICT),
    RAM_TYPE_IN_USE_BY_RAM(4003, "Cannot delete RAM Type. There are RAM products using this RAM Type", HttpStatus.CONFLICT),
    RAM_TYPE_IN_USE_BY_MAINBOARD(4004, "Cannot delete RAM Type. There are Mainboards using this RAM Type", HttpStatus.CONFLICT),
    PCIE_VERSION_IN_USE_BY_CPU(4005, "Cannot delete PCIe Version. There are CPUs using this PCIe Version", HttpStatus.CONFLICT),
    PCIE_VERSION_IN_USE_BY_MAINBOARD(4006, "Cannot delete PCIe Version. There are Mainboards using this PCIe Version", HttpStatus.CONFLICT),
    PCIE_VERSION_IN_USE_BY_VGA(4007, "Cannot delete PCIe Version. There are VGAs using this PCIe Version", HttpStatus.CONFLICT),
    SSD_TYPE_IN_USE(4008, "Cannot delete SSD Type. There are SSDs using this SSD Type", HttpStatus.CONFLICT),
    INTERFACE_TYPE_IN_USE_BY_SSD(4009, "Cannot delete Interface Type. There are SSDs using this Interface Type", HttpStatus.CONFLICT),
    INTERFACE_TYPE_IN_USE_BY_HDD(4010, "Cannot delete Interface Type. There are HDDs using this Interface Type", HttpStatus.CONFLICT),
    FORM_FACTOR_IN_USE_BY_SSD(4011, "Cannot delete Form Factor. There are SSDs using this Form Factor", HttpStatus.CONFLICT),
    FORM_FACTOR_IN_USE_BY_HDD(4012, "Cannot delete Form Factor. There are HDDs using this Form Factor", HttpStatus.CONFLICT),
    COOLER_TYPE_IN_USE(4013, "Cannot delete Cooler Type. There are Coolers using this Cooler Type", HttpStatus.CONFLICT),
    CASE_SIZE_IN_USE_BY_MAINBOARD(4014, "Cannot delete Case Size. There are Mainboards using this Case Size", HttpStatus.CONFLICT),
    CASE_SIZE_IN_USE_BY_CASE(4015, "Cannot delete Case Size. There are Cases using this Case Size", HttpStatus.CONFLICT),
    PCIE_CONNECTOR_IN_USE(4016, "Cannot delete PCIe Connector. There are PSUs using this PCIe Connector", HttpStatus.CONFLICT),
    FOREIGN_KEY_VIOLATION(4099, "Cannot delete this entity. It is being referenced by other entities", HttpStatus.CONFLICT),

    // PC Build validation errors (5001-5099)
    BUILD_NAME_REQUIRED(5001, "Build name is required", HttpStatus.BAD_REQUEST),
    BUILD_NOT_FOUND(5002, "Build not found", HttpStatus.NOT_FOUND),
    BUILD_UNAUTHORIZED_ACCESS(5003, "You don't have permission to access this build", HttpStatus.FORBIDDEN),

    // Build Analysis errors (6001-6099)
    CPU_ID_REQUIRED(6001, "CPU ID is required", HttpStatus.BAD_REQUEST),
    GPU_ID_REQUIRED(6002, "GPU ID is required", HttpStatus.BAD_REQUEST),
    RAM_ID_REQUIRED(6003, "RAM ID is required", HttpStatus.BAD_REQUEST),
    SSD_ID_REQUIRED(6004, "SSD ID is required", HttpStatus.BAD_REQUEST),

    // File storage errors (6101-6199)
    FILE_EMPTY(6101, "File is empty", HttpStatus.BAD_REQUEST),
    FILE_INVALID_NAME(6102, "File name or entity is invalid", HttpStatus.BAD_REQUEST),
    FILE_STORAGE_ERROR(6103, "Failed to store file", HttpStatus.INTERNAL_SERVER_ERROR)
    ,

    // Game validation errors (3201-3299)
    GAME_NAME_REQUIRED(3201, "Game name is required", HttpStatus.BAD_REQUEST),
    GAME_NAME_ALREADY_EXISTS(3202, "Game name already exists", HttpStatus.BAD_REQUEST),
    GAME_NOT_FOUND(3203, "Game not found", HttpStatus.NOT_FOUND),
    GAME_MIN_CPU_SCORE_INVALID(3204, "Min CPU score must be positive", HttpStatus.BAD_REQUEST),
    GAME_MIN_GPU_SCORE_INVALID(3205, "Min GPU score must be positive", HttpStatus.BAD_REQUEST),
    GAME_REC_CPU_SCORE_INVALID(3206, "Recommended CPU score must be positive", HttpStatus.BAD_REQUEST),
    GAME_REC_GPU_SCORE_INVALID(3207, "Recommended GPU score must be positive", HttpStatus.BAD_REQUEST),
    GAME_MIN_RAM_GB_INVALID(3208, "Min RAM (GB) must be positive", HttpStatus.BAD_REQUEST),
    GAME_REC_RAM_GB_INVALID(3209, "Recommended RAM (GB) must be positive", HttpStatus.BAD_REQUEST),
    GAME_BASE_FPS_LOW_INVALID(3210, "Base FPS Low must be positive", HttpStatus.BAD_REQUEST),
    GAME_BASE_FPS_MEDIUM_INVALID(3211, "Base FPS Medium must be positive", HttpStatus.BAD_REQUEST),
    GAME_BASE_FPS_HIGH_INVALID(3212, "Base FPS High must be positive", HttpStatus.BAD_REQUEST),
    GAME_MIN_STORAGE_GB_INVALID(3213, "Min storage (GB) must be positive", HttpStatus.BAD_REQUEST),
    GAME_MIN_VRAM_GB_INVALID(3214, "Min VRAM (GB) must be positive", HttpStatus.BAD_REQUEST),
    GAME_REC_STORAGE_GB_INVALID(3215, "Recommended storage (GB) must be positive", HttpStatus.BAD_REQUEST),
    GAME_REC_VRAM_GB_INVALID(3216, "Recommended VRAM (GB) must be positive", HttpStatus.BAD_REQUEST),

    // FPS estimation validation errors (3301-3399)
    RESOLUTION_REQUIRED(3301, "Resolution is required", HttpStatus.BAD_REQUEST),
    RESOLUTION_INVALID(3302, "Resolution must be one of: 1080p, 2k, 4k", HttpStatus.BAD_REQUEST)
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private int code;
    private String message;
    private HttpStatusCode statusCode;


}
