package com.j2ee.buildpcchecker.exception;

import com.j2ee.buildpcchecker.dto.request.ApiResponse;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.Objects;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final String MIN_ATTRIBUTE = "min";

    // Handle RuntimeException với ErrorCode UNCATEGORIZED_EXCEPTION nếu không phải
    // lỗi đến từ AppException
    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException exception) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(apiResponse);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException exception) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        String enumKey = exception.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.INVALID_KEY;

        Map<String, Object> attributes = null;

        try {
            errorCode = ErrorCode.valueOf(enumKey);

            var constraintViolation = exception.getBindingResult()
                    .getAllErrors().get(0).unwrap(ConstraintViolation.class);

            attributes = constraintViolation.getConstraintDescriptor().getAttributes();
            log.info(attributes.toString());
        } catch (IllegalArgumentException ex) {
            // Nếu không tìm thấy enum tương ứng, giữ nguyên errorCode là INVALID_KEY
        }

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(Objects.nonNull(attributes) ? mapAttribute(errorCode.getMessage(), attributes)
                : errorCode.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    ResponseEntity<ApiResponse> handlingHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(999);
        apiResponse.setMessage("Malformed JSON request");

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    ResponseEntity<ApiResponse> handlingDataIntegrityViolationException(DataIntegrityViolationException exception) {
        ErrorCode errorCode = determineErrorCodeFromConstraintViolation(exception);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(apiResponse);
    }

    /**
     * Determine specific error code based on the constraint violation message
     */
    private ErrorCode determineErrorCodeFromConstraintViolation(DataIntegrityViolationException exception) {
        String message = exception.getMessage();
        if (message == null) {
            return ErrorCode.FOREIGN_KEY_VIOLATION;
        }

        String lowerMessage = message.toLowerCase();

        // Socket constraints
        if (lowerMessage.contains("socket_id")) {
            if (lowerMessage.contains("cpu")) {
                return ErrorCode.SOCKET_IN_USE_BY_CPU;
            } else if (lowerMessage.contains("mainboard")) {
                return ErrorCode.SOCKET_IN_USE_BY_MAINBOARD;
            }
        }

        // RAM Type constraints
        if (lowerMessage.contains("ram_type_id")) {
            if (lowerMessage.contains("ram") && !lowerMessage.contains("mainboard")) {
                return ErrorCode.RAM_TYPE_IN_USE_BY_RAM;
            } else if (lowerMessage.contains("mainboard")) {
                return ErrorCode.RAM_TYPE_IN_USE_BY_MAINBOARD;
            }
        }

        // PCIe Version constraints
        if (lowerMessage.contains("pcie_version_id")) {
            if (lowerMessage.contains("cpu")) {
                return ErrorCode.PCIE_VERSION_IN_USE_BY_CPU;
            } else if (lowerMessage.contains("mainboard")) {
                return ErrorCode.PCIE_VERSION_IN_USE_BY_MAINBOARD;
            } else if (lowerMessage.contains("vga")) {
                return ErrorCode.PCIE_VERSION_IN_USE_BY_VGA;
            }
        }

        // SSD Type constraints
        if (lowerMessage.contains("ssd_type_id")) {
            return ErrorCode.SSD_TYPE_IN_USE;
        }

        // Interface Type constraints
        if (lowerMessage.contains("interface_type_id") || lowerMessage.contains("interface_id")) {
            if (lowerMessage.contains("ssd")) {
                return ErrorCode.INTERFACE_TYPE_IN_USE_BY_SSD;
            } else if (lowerMessage.contains("hdd")) {
                return ErrorCode.INTERFACE_TYPE_IN_USE_BY_HDD;
            }
        }

        // Form Factor constraints
        if (lowerMessage.contains("form_factor_id") || lowerMessage.contains("form_factor")) {
            if (lowerMessage.contains("ssd")) {
                return ErrorCode.FORM_FACTOR_IN_USE_BY_SSD;
            } else if (lowerMessage.contains("hdd")) {
                return ErrorCode.FORM_FACTOR_IN_USE_BY_HDD;
            }
        }

        // Cooler Type constraints
        if (lowerMessage.contains("cooler_type_id")) {
            return ErrorCode.COOLER_TYPE_IN_USE;
        }

        // Case Size constraints
        if (lowerMessage.contains("size_id") || lowerMessage.contains("case_size_id")) {
            if (lowerMessage.contains("mainboard")) {
                return ErrorCode.CASE_SIZE_IN_USE_BY_MAINBOARD;
            } else if (lowerMessage.contains("case") || lowerMessage.contains("pccase")) {
                return ErrorCode.CASE_SIZE_IN_USE_BY_CASE;
            }
        }

        // PCIe Connector constraints
        if (lowerMessage.contains("pcie_connector_id")) {
            return ErrorCode.PCIE_CONNECTOR_IN_USE;
        }

        // Default foreign key violation
        return ErrorCode.FOREIGN_KEY_VIOLATION;
    }

    private String mapAttribute(String message, Map<String, Object> attributes) {
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));

        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
    }

}
