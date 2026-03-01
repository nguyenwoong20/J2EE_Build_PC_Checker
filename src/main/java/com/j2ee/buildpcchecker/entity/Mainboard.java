package com.j2ee.buildpcchecker.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "mainboard")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Mainboard {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(nullable = false)
    String name;

    @ManyToOne
    @JoinColumn(name = "socket_id", nullable = false)
    Socket socket;

    @Column(name = "vrm_phase", nullable = false)
    Integer vrmPhase;

    @Column(name = "cpu_tdp_support", nullable = false)
    Integer cpuTdpSupport;

    @ManyToOne
    @JoinColumn(name = "ram_type_id", nullable = false)
    RamType ramType;

    @Column(name = "ram_bus_max", nullable = false)
    Integer ramBusMax;

    @Column(name = "ram_slot", nullable = false)
    Integer ramSlot;

    @Column(name = "ram_max_capacity", nullable = false)
    Integer ramMaxCapacity;

    @ManyToOne
    @JoinColumn(name = "size_id", nullable = false)
    CaseSize size;

    @ManyToOne
    @JoinColumn(name = "pcie_vga_version_id", nullable = false)
    PcieVersion pcieVgaVersion;

    @Column(name = "m2_slot")
    Integer m2Slot;

    @Column(name = "sata_slot")
    Integer sataSlot;

    @Column(columnDefinition = "TEXT")
    String description;
}

