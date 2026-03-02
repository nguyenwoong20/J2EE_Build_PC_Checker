package com.j2ee.buildpcchecker.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HddUpdateRequest {

    String name;
    String formFactorId;
    String interfaceTypeId;
    Integer capacity;
    Integer tdp;
    String imageUrl;
    String description;
}
