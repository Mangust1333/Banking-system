package com.kubancevvladislav.presentation.controllers.dto.transactionsDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.math.BigInteger;

@Data
@SuperBuilder
@Schema(description = "DTO для возврата информации о транзакции внесения средств на счёт")
public class DepositTransactionDTO extends TransactionDTO {
    private BigInteger accountId;
}
