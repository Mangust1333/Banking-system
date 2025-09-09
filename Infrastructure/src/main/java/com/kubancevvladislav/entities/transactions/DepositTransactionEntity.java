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

import java.math.BigInteger;

/**
 * Класс представляет транзакцию депозита, которая наследует от класса {@link TransactionEntity}.
 * Этот класс используется для выполнения операции пополнения баланса на счёт.
 * @author Кубанцев Владислав
 * @version 1.0
 * @since 1.0
 * @see TransactionEntity
 * @see <a href="https://github.com/Mangust1333">GitHub: Mangust1333</a>
 */
@Entity
@Table(name = "deposit_transactions")
@PrimaryKeyJoinColumn(name = "transaction_id")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class DepositTransactionEntity extends TransactionEntity {
    /** Номер счёта, на который был произведен депозит. */
    @Column(name = "account_id", nullable = false)
    private BigInteger accountId;
}
