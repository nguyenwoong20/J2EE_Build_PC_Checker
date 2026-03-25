package com.j2ee.buildpcchecker.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Comparison result for a specific component")
public class ComponentComparison<T> {
    T build1;
    T build2;
    @Schema(description = "Winner of the comparison: build1, build2, or equal")
    String winner;
}
