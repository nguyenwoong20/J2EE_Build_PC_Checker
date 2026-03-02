package com.j2ee.buildpcchecker.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CoolerResponse {

    String id;
    String name;
    CoolerTypeResponse coolerType;
    Integer radiatorSize;
    Integer heightMm;
    Integer tdpSupport;
    String imageUrl;
    String description;
}
