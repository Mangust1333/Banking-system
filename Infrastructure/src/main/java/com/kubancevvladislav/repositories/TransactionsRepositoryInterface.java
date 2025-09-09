package com.kubancevvladislav.repositories;

import com.kubancevvladislav.entities.transactions.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

/**
 * Интерфейс репозитория для работы с транзакциями.
 * Определяет методы для создания и получения списка транзакций.
 * @author Кубанцев Владислав
 * @version 1.2
 * @since 1.0
 * @see <a href="https://github.com/Mangust1333">GitHub: Mangust1333</a>
 */
@Repository
public interface TransactionsRepositoryInterface extends JpaRepository<TransactionEntity, BigInteger> {
    /**
     * Возвращает список всех транзакций, хранящихся в репозитории.
     * @return список объектов {@link TransactionEntity}
     */
    @Query("SELECT t FROM TransactionEntity t")
    List<TransactionEntity> getAllTransactions();

    @Query("""
    SELECT t FROM TransactionEntity t
    WHERE (
            (:transactionType LIKE "DEPOSIT" AND TREAT(t AS DepositTransactionEntity).accountId = :accountId)
            OR
            (:transactionType LIKE "WITHDRAW" AND TREAT(t AS WithdrawTransactionEntity).accountId = :accountId)
            OR
            (:transactionType LIKE "TRANSFER" AND 
                (:accountId = TREAT(t AS TransferTransactionEntity).senderAccountId 
                OR :accountId = TREAT(t AS TransferTransactionEntity).receiverAccountId)
            )
        )
    """)
    List<TransactionEntity> findByAccountIdAndTransactionType(BigInteger accountId, String transactionType);
}
