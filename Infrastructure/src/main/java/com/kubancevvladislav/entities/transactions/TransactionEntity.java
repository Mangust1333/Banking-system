package com.kubancevvladislav.entities.transactions;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * Абстрактный класс, представляющий общую модель транзакции.
 * @see DepositTransactionEntity
 * @see WithdrawTransactionEntity
 * @see TransferTransactionEntity
 * @author Кубанцев Владислав
 * @see <a href="https://github.com/Mangust1333">GitHub: Mangust1333</a>
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "transactions")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
public class TransactionEntity {
    /**
     * Уникальный идентификатор транзакции.
     * Идентификатор транзакции создается автоматически с использованием генератора.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id", nullable = false, unique = true)
    protected BigInteger transactionId;
    /**
     * Сумма транзакции.
     * Это может быть как сумма депозита, так и сумма снятия средств в зависимости от типа транзакции.
     */
    @Column(name = "amount", nullable = false)
    protected BigDecimal amount;
    /** Временная метка, указывающая время совершения транзакции. */
    @Column(name = "timestamp", nullable = false)
    @CreationTimestamp
    protected LocalDateTime timestamp;
}
