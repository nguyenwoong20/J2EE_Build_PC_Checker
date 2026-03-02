package com.j2ee.buildpcchecker.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RamUpdateRequest {

    String name;
    String ramTypeId;
    Integer ramBus;
    Integer ramCas;
    Integer capacityPerStick;
    Integer quantity;
    Integer tdp;
    String imageUrl;
    String description;
}

