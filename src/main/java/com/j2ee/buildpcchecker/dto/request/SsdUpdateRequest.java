package com.j2ee.buildpcchecker.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SsdUpdateRequest {

    String name;
    String ssdTypeId;
    String formFactorId;
    String interfaceTypeId;
    Integer capacity;
    Integer tdp;
    String imageUrl;
    String description;
}
