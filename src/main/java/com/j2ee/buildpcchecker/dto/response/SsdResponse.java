package com.j2ee.buildpcchecker.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SsdResponse {

    String id;
    String name;
    SsdTypeResponse ssdType;
    FormFactorResponse formFactor;
    InterfaceTypeResponse interfaceType;
    Integer capacity;
    Integer tdp;
    String imageUrl;
    String description;
}
