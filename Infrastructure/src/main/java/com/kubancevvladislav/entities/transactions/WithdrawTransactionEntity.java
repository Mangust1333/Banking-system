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
 * Класс представляет транзакцию снятия средств, которая наследует от класса {@link TransactionEntity}.
 * Этот класс используется для выполнения операций по снятию средств с баланса учетной записи.
 * @author Кубанцев Владислав
 * @see <a href="https://github.com/Mangust1333">GitHub: Mangust1333</a>
 * @version 1.0
 * @since 1.0
 * @see TransactionEntity
 */
@Entity
@Table(name = "withdraw_transactions")
@PrimaryKeyJoinColumn(name = "transaction_id")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class WithdrawTransactionEntity extends TransactionEntity {
    /**
    * Идентификатор учетной записи, с которой были сняты средства.
    */
    @Column(name = "account_id", nullable = false)
    private BigInteger accountId;
}
