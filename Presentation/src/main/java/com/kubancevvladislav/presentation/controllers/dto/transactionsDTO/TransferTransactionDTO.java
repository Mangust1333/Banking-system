package com.kubancevvladislav.presentation.controllers.dto.transactionsDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@SuperBuilder
@Schema(description = "ДТО для возврата информации о транзакции перевода со счёта на счёт")
public class TransferTransactionDTO extends TransactionDTO {
    private BigInteger senderAccountId;
    private BigInteger receiverAccountId;
    private BigDecimal commission;
}
