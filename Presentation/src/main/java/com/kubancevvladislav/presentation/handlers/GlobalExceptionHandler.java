package com.kubancevvladislav.presentation.handlers;

import com.kubancevvladislav.presentation.controllers.dto.OperationResponseDTO;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.PersistenceException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<OperationResponseDTO> handleDatabaseError(DataAccessException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
                OperationResponseDTO.operationFailed(
                        "Ошибка доступа к базе данных:" + ex.getMessage()
                )
        );
    }

    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<OperationResponseDTO> handlePersistenceError(PersistenceException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                OperationResponseDTO.operationFailed(
                        "Ошибка при сохранении данных: " + ex.getMessage()
                )
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<OperationResponseDTO> handleUnexpectedError(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                OperationResponseDTO.operationFailed(
                        "Внутренняя ошибка сервера:" + ex.getMessage()
                )
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<OperationResponseDTO> handleIllegalArgs(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                OperationResponseDTO.operationFailed(
                        ex.getMessage()
                )
        );
    }
}
