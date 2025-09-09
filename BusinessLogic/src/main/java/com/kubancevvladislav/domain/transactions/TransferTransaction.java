package com.kubancevvladislav.domain.transactions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Класс представляет транзакцию перевода средств между двумя учетными записями.
 * Этот класс наследует от {@link Transaction} и включает в себя информацию об отправителе,
 * получателе и комиссии, связанной с переводом средств.
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
public class TransferTransaction extends Transaction {
    /**
     * Номер счёта отправителя.
     */
    private BigInteger senderAccountId;
    /**
     * Номер счёта получателя.
     */
    private BigInteger receiverAccountId;
    /**
     * Комиссия за выполнение перевода.
     */
    private BigDecimal commission;
}
