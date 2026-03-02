package com.j2ee.buildpcchecker.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "cooler")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Cooler {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(nullable = false)
    String name;

    @ManyToOne
    @JoinColumn(name = "cooler_type_id", nullable = false)
    CoolerType coolerType; // AIR / AIO

    @Column(name = "radiator_size")
    Integer radiatorSize; // 120 / 240 / 360 (nullable cho tản khí)

    @Column(name = "height_mm")
    Integer heightMm; // chiều cao tản khí

    @Column(name = "tdp_support", nullable = false)
    Integer tdpSupport; // W

    @Column(name = "image_url", columnDefinition = "TEXT")
    String imageUrl;

    @Column(columnDefinition = "TEXT")
    String description;
}
