package com.kubancevvladislav.domain.transactions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import java.math.BigInteger;

/**
 * Класс представляет транзакцию снятия средств, которая наследует от класса {@link Transaction}.
 * Этот класс используется для выполнения операций по снятию средств с баланса учетной записи.
 *
 * @author Кубанцев Владислав
 * @see <a href="https://github.com/Mangust1333">GitHub: Mangust1333</a>
 * @version 1.2
 * @since 1.0
 * @see Transaction
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class WithdrawTransaction extends Transaction {
    /**
    * Идентификатор учетной записи, с которой были сняты средства.
    */
    private BigInteger accountId;
}
