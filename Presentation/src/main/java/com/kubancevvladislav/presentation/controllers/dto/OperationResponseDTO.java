package com.kubancevvladislav.presentation.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@AllArgsConstructor
@Schema(description = "Ответ на операцию")
public class OperationResponseDTO {
    private boolean success;
    private String message;

    public static OperationResponseDTO operationSuccess(String message) {
        return new OperationResponseDTO(true, message);
    }

    public static OperationResponseDTO operationFailed(String message) {
        return new OperationResponseDTO(false, message);
    }
}