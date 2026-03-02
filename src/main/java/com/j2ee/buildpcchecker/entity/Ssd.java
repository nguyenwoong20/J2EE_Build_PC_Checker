package com.j2ee.buildpcchecker.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "ssd")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Ssd {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(nullable = false)
    String name;

    @ManyToOne
    @JoinColumn(name = "ssd_type_id", nullable = false)
    SsdType ssdType; // SATA / NVME

    @ManyToOne
    @JoinColumn(name = "form_factor_id", nullable = false)
    FormFactor formFactor; // 2.5", M.2 2280

    @ManyToOne
    @JoinColumn(name = "interface_type_id", nullable = false)
    InterfaceType interfaceType; // SATA III / PCIe 3.0 / PCIe 4.0

    @Column(nullable = false)
    Integer capacity; // GB

    @Column(nullable = false)
    Integer tdp; // W

    @Column(name = "image_url", columnDefinition = "TEXT")
    String imageUrl;

    @Column(columnDefinition = "TEXT")
    String description;
}
