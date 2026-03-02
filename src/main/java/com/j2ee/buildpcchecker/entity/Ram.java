package com.j2ee.buildpcchecker.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "ram")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Ram {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(nullable = false)
    String name;

    @ManyToOne
    @JoinColumn(name = "ram_type_id", nullable = false)
    RamType ramType;

    @Column(name = "ram_bus", nullable = false)
    Integer ramBus;

    @Column(name = "ram_cas", nullable = false)
    Integer ramCas;

    @Column(name = "capacity_per_stick", nullable = false)
    Integer capacityPerStick;

    @Column(nullable = false)
    Integer quantity;

    @Column(nullable = false)
    Integer tdp;

    @Column(name = "image_url", columnDefinition = "TEXT")
    String imageUrl;

    @Column(columnDefinition = "TEXT")
    String description;
}

