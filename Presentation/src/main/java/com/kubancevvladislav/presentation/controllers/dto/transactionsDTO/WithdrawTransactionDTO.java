package com.kubancevvladislav.presentation.controllers.dto.transactionsDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.math.BigInteger;


@Data
@SuperBuilder
@Schema(description = "ДТО для возврата информации о транзакции снятия средств со счёта")
public class WithdrawTransactionDTO extends TransactionDTO {
    private BigInteger accountId;
}
