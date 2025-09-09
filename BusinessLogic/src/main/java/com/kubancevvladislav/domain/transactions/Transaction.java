package com.kubancevvladislav.domain.transactions;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * Абстрактный класс, представляющий общую модель транзакции.
 * @see DepositTransaction
 * @see WithdrawTransaction
 * @see TransferTransaction
 * @author Кубанцев Владислав
 * @see <a href="https://github.com/Mangust1333">GitHub: Mangust1333</a>
 * @version 1.2
 * @since 1.0
 */
@Data
@SuperBuilder
public class Transaction {
    /**
     * Уникальный идентификатор транзакции.
     * Идентификатор транзакции создается автоматически с использованием генератора.
     */
    protected BigInteger transactionId;
    /**
     * Сумма транзакции.
     * Это может быть как сумма депозита, так и сумма снятия средств в зависимости от типа транзакции.
     */
    protected BigDecimal amount;
    /**
     * Временная метка, указывающая время совершения транзакции.
     */
    protected LocalDateTime timestamp;
}
