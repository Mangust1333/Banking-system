package com.kubancevvladislav.entities.transactions;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Класс представляет транзакцию перевода средств между двумя учетными записями.
 * Этот класс наследует от {@link TransactionEntity} и включает в себя информацию об отправителе,
 * получателе и комиссии, связанной с переводом средств.
 *
 * @author Кубанцев Владислав
 * @see <a href="https://github.com/Mangust1333">GitHub: Mangust1333</a>
 * @version 1.0
 * @since 1.0
 * @see TransactionEntity
 */
@Entity
@Table(name = "transfer_transactions")
@PrimaryKeyJoinColumn(name = "transaction_id")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class TransferTransactionEntity extends TransactionEntity {
    /** Номер счёта отправителя. */
    @Column(name = "sender_account_id", nullable = false)
    private BigInteger senderAccountId;
    /** Номер счёта получателя. */
    @Column(name = "receiver_account_id", nullable = false)
    private BigInteger receiverAccountId;
    /** Комиссия за выполнение перевода. */
    @Column(name = "commission", nullable = false)
    private BigDecimal commission;
}
