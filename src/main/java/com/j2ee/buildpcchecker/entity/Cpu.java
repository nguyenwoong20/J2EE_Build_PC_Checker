package com.j2ee.buildpcchecker.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "cpu")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Cpu {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(nullable = false)
    String name;

    @ManyToOne
    @JoinColumn(name = "socket_id", nullable = false)
    Socket socket;

    @Column(name = "vrm_min")
    Integer vrmMin;

    @Column(nullable = false)
    Boolean igpu;

    @Column(nullable = false)
    Integer tdp;

    @ManyToOne
    @JoinColumn(name = "pcie_version_id", nullable = false)
    PcieVersion pcieVersion;

    @Column(nullable = false)
    Integer score;

    @Column(name = "image_url", columnDefinition = "TEXT")
    String imageUrl;

    @Column(columnDefinition = "TEXT")
    String description;
}
