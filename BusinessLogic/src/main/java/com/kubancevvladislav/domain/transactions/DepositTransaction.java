package com.kubancevvladislav.domain.transactions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.math.BigInteger;

/**
 * Класс представляет транзакцию депозита, которая наследует от класса {@link Transaction}.
 * Этот класс используется для выполнения операции пополнения баланса на счёт.
 * @author Кубанцев Владислав
 * @version 1.2
 * @since 1.0
 * @see Transaction
 * @see <a href="https://github.com/Mangust1333">GitHub: Mangust1333</a>
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class DepositTransaction extends Transaction {
    /**
    * Номер счёта, на который был произведен депозит.
    */
    private BigInteger accountId;
}
