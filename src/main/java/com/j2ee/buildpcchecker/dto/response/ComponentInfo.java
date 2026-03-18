package com.j2ee.buildpcchecker.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Component info containing name and benchmark score")
public class ComponentInfo {

    @Schema(description = "Component name", example = "Intel Core i3-10100F")
    String name;

    @Schema(description = "PassMark benchmark score", example = "8716")
    int score;
}
