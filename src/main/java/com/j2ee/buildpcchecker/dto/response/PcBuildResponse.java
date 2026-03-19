package com.j2ee.buildpcchecker.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PcBuildResponse {

    String id;
    String userId;
    String name;
    String description;
    Map<String, String> parts;  // key: partType, value: partId
    LocalDateTime createdAt;
}

