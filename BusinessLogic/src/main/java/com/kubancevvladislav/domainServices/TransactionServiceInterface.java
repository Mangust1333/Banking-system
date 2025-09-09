package com.kubancevvladislav.domainServices;

import com.kubancevvladislav.domain.TransactionType;
import com.kubancevvladislav.domain.transactions.Transaction;
import com.kubancevvladislav.entities.transactions.TransactionEntity;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

/**
 * Интерфейс для сервиса управления транзакциями.
 * Определяет методы для получения информации о транзакциях в системе.
 * @author Кубанцев Владислав
 * @see <a href="https://github.com/Mangust1333">GitHub: Mangust1333</a>
 * @version 1.2
 * @since 1.0
 */
@Service
public interface TransactionServiceInterface {
    /**
     * Возвращает список всех транзакций в системе.
     * @return список всех транзакций
     * @see Transaction
     */
    List<Transaction> getAllTransactions();

    List<Transaction> getAccountTransactionsByType(
            BigInteger accountId,
            TransactionType transactionType);
}
