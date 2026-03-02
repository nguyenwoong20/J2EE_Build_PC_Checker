package com.j2ee.buildpcchecker.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "hdd")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Hdd {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(nullable = false)
    String name;

    @ManyToOne
    @JoinColumn(name = "form_factor_id", nullable = false)
    FormFactor formFactor; // 3.5" / 2.5"

    @ManyToOne
    @JoinColumn(name = "interface_type_id", nullable = false)
    InterfaceType interfaceType; // SATA_3 / SAS

    @Column(nullable = false)
    Integer capacity; // GB

    @Column(nullable = false)
    Integer tdp; // W

    @Column(name = "image_url", columnDefinition = "TEXT")
    String imageUrl;

    @Column(columnDefinition = "TEXT")
    String description;
}
