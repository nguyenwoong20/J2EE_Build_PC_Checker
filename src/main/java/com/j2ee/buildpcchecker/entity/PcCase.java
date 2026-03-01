package com.j2ee.buildpcchecker.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "pc_case")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PcCase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(nullable = false)
    String name;

    @ManyToOne
    @JoinColumn(name = "size_id", nullable = false)
    CaseSize size;

    @Column(name = "max_vga_length_mm", nullable = false)
    Integer maxVgaLengthMm;

    @Column(name = "max_cooler_height_mm", nullable = false)
    Integer maxCoolerHeightMm; // mm (cho tản khí)

    @Column(name = "max_radiator_size", nullable = false)
    Integer maxRadiatorSize; // 120 / 240 / 360

    @Column(name = "drive_35_slot", nullable = false)
    Integer drive35Slot; // số ổ 3.5"

    @Column(name = "drive_25_slot", nullable = false)
    Integer drive25Slot; // số ổ 2.5"

    @Column(columnDefinition = "TEXT")
    String description;
}
