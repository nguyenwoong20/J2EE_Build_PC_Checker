package com.j2ee.buildpcchecker.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Table(name = "psu")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Psu {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    Integer wattage; // W

    @Column(nullable = false)
    String efficiency; // 80+ Bronze / Gold / Platinum

    @ManyToMany
    @JoinTable(
            name = "psu_pcie_connector",
            joinColumns = @JoinColumn(name = "psu_id"),
            inverseJoinColumns = @JoinColumn(name = "pcie_connector_id")
    )
    Set<PcieConnector> pcieConnectors; // 2X8PIN, 3X8PIN, 12VHPWR

    @Column(name = "sata_connector", nullable = false)
    Integer sataConnector; // số đầu SATA

    @Column(columnDefinition = "TEXT")
    String description;
}
