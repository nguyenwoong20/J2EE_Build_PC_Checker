package com.j2ee.buildpcchecker.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "vga")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Vga {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(nullable = false)
    String name;

    @Column(name = "length_mm", nullable = false)
    Integer lengthMm;

    @Column(nullable = false)
    Integer tdp;

    @ManyToOne
    @JoinColumn(name = "pcie_version_id", nullable = false)
    PcieVersion pcieVersion;

    @ManyToOne
    @JoinColumn(name = "power_connector_id")
    PcieConnector powerConnector;

    @Column(nullable = false)
    Integer score;

    @Column(columnDefinition = "TEXT")
    String description;
}

