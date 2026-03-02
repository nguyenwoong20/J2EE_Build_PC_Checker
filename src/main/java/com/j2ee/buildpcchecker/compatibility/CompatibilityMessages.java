package com.j2ee.buildpcchecker.compatibility;

/**
 * Centralized compatibility error and warning messages
 */
public final class CompatibilityMessages {

    private CompatibilityMessages() {}

    // ========== LAYER 1: CPU ↔ MAINBOARD ==========
    public static final String CPU_MAINBOARD_SOCKET_MISMATCH =
            "CPU socket '%s' không khớp với Mainboard socket '%s'";

    public static final String CPU_MAINBOARD_VRM_INSUFFICIENT =
            "Mainboard VRM (%d phase) không đủ cho CPU (yêu cầu tối thiểu %d phase)";

    public static final String CPU_MAINBOARD_TDP_INSUFFICIENT =
            "Mainboard chỉ hỗ trợ TDP %dW, không đủ cho CPU %dW";

    // ========== LAYER 2: RAM ==========
    public static final String RAM_TYPE_MISMATCH =
            "RAM type '%s' không khớp với Mainboard RAM type '%s'";

    public static final String RAM_BUS_EXCEEDS =
            "RAM bus %d MHz vượt quá giới hạn Mainboard (%d MHz)";

    public static final String RAM_QUANTITY_EXCEEDS =
            "Số lượng RAM (%d thanh) vượt quá số khe của Mainboard (%d khe)";

    public static final String RAM_CAPACITY_EXCEEDS =
            "Tổng dung lượng RAM (%d GB) vượt quá giới hạn Mainboard (%d GB)";

    // ========== LAYER 3: CASE + VGA ==========
    public static final String CASE_SIZE_INCOMPATIBLE =
            "Mainboard size '%s' không được Case size '%s' hỗ trợ";

    public static final String VGA_LENGTH_EXCEEDS =
            "VGA dài %d mm vượt quá giới hạn Case (%d mm)";

    public static final String VGA_PCIE_DOWNGRADE =
            "VGA PCIe %s sẽ chạy ở tốc độ thấp hơn do Mainboard chỉ hỗ trợ %s (backward compatible)";

    // ========== LAYER 4: STORAGE ==========
    public static final String STORAGE_M2_SLOT_INSUFFICIENT =
            "Số lượng SSD M.2 (%d) vượt quá số khe M.2 của Mainboard (%d)";

    public static final String STORAGE_SATA_SLOT_INSUFFICIENT =
            "Tổng số ổ SATA (%d SSD + %d HDD = %d) vượt quá số cổng SATA của Mainboard (%d)";

    public static final String STORAGE_CASE_35_BAY_INSUFFICIENT =
            "Số lượng HDD 3.5\" (%d) vượt quá số khay 3.5\" của Case (%d)";

    public static final String STORAGE_CASE_25_BAY_INSUFFICIENT =
            "Số lượng ổ 2.5\" (%d) vượt quá số khay 2.5\" của Case (%d)";

    // ========== LAYER 5: COOLER ==========
    public static final String COOLER_TDP_INSUFFICIENT =
            "Cooler chỉ hỗ trợ TDP %dW, không đủ cho CPU %dW";

    public static final String COOLER_AIR_HEIGHT_EXCEEDS =
            "Tản khí cao %d mm vượt quá giới hạn Case (%d mm)";

    public static final String COOLER_AIO_RADIATOR_INCOMPATIBLE =
            "Case không hỗ trợ radiator %d mm (giới hạn: %d mm)";

    // ========== LAYER 6: POWER ==========
    public static final String PSU_WATTAGE_INSUFFICIENT =
            "PSU chỉ có %dW, không đủ cho hệ thống (khuyến nghị tối thiểu: %dW)";

    public static final String PSU_PCIE_CONNECTOR_MISSING =
            "PSU không có đủ connector '%s' cho VGA";

    public static final String PSU_SATA_CONNECTOR_INSUFFICIENT =
            "PSU chỉ có %d đầu SATA, không đủ cho %d ổ SATA";

    // ========== WARNINGS ==========
    public static final String WARNING_NO_VGA_NO_IGPU =
            "Không có VGA và CPU không có iGPU - Hệ thống không thể xuất hình";

    public static final String WARNING_CPU_VGA_BOTTLENECK =
            "Có thể xảy ra bottleneck: CPU score (%d) và VGA score (%d) chênh lệch > 40%%";

    public static final String WARNING_RAM_SINGLE_CHANNEL =
            "Chỉ có 1 thanh RAM - Khuyến nghị dùng 2 thanh để kích hoạt Dual Channel";
}

