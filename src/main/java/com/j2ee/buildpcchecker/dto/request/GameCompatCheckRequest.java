package com.j2ee.buildpcchecker.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GameCompatCheckRequest {

    @NotBlank(message = "CPU_ID_REQUIRED")
    String cpuId;

    @NotBlank(message = "GPU_ID_REQUIRED")
    String vgaId;

    @NotBlank(message = "RAM_ID_REQUIRED")
    String ramId;

    /** Số thanh RAM đang dùng. Nếu null thì dùng quantity của Ram entity. */
    @PositiveOrZero(message = "RAM_QUANTITY_INVALID")
    Integer ramQuantity;
}
