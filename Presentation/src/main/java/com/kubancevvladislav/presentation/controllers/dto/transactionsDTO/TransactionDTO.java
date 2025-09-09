package com.kubancevvladislav.presentation.controllers.dto.transactionsDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@SuperBuilder
@Schema(description = "DTO для возврата информации о транзакции")
public class TransactionDTO {
    private BigInteger transactionId;
    private BigDecimal amount;
    private LocalDateTime timestamp;
}