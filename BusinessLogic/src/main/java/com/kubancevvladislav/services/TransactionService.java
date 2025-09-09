package com.kubancevvladislav.services;

import com.kubancevvladislav.domain.TransactionType;
import com.kubancevvladislav.domain.transactions.Transaction;
import com.kubancevvladislav.domainServices.TransactionServiceInterface;
import com.kubancevvladislav.entities.transactions.TransactionEntity;
import com.kubancevvladislav.repositories.AccountRepositoryInterface;
import com.kubancevvladislav.repositories.TransactionsRepositoryInterface;
import com.kubancevvladislav.services.mapper.AccountMapperInterface;
import com.kubancevvladislav.services.mapper.TransactionMapperInterface;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для работы с транзакциями, реализующий {@link TransactionServiceInterface}.
 * Этот класс предоставляет методы для получения транзакций по пользователю и счёту,
 * а также для работы с общими транзакциями в системе.
 * @author Кубанцев Владислав
 * @version 1.2
 * @since 1.0
 * @see <a href="https://github.com/Mangust1333">GitHub: Mangust1333</a>
 */
@Service
@AllArgsConstructor
public class TransactionService implements TransactionServiceInterface {
    /** Репозиторий, в котором хранится информация о транзакциях */
    private final TransactionsRepositoryInterface transactionsRepository;
    /** Репозиторий, в котором хранится информация о счетах */
    private final AccountRepositoryInterface accountRepository;
    /** Маппер для перевода из домена в entity и наоборот */
    @Qualifier("accountMapperInterfaceImpl")
    private final AccountMapperInterface accountMapper;
    /** Маппер для перевода из домена в entity и наоборот */
    @Qualifier("transactionMapperInterfaceImpl")
    private final TransactionMapperInterface transactionMapper;

    /**
     * Возвращает все транзакции в системе.
     * @return список всех транзакций
     */
    @Override
    public List<Transaction> getAllTransactions() {
        List<TransactionEntity> transactionEntities = transactionsRepository.getAllTransactions();
        List<Transaction> transactions = transactionEntities.stream()
                .map(transactionMapper::toDomain)
                .collect(Collectors.toList());
        return transactions;
    }

    @Override
    public List<Transaction> getAccountTransactionsByType(
            BigInteger accountId,
            TransactionType transactionType) {
        List<TransactionEntity> entities = transactionsRepository
                .findByAccountIdAndTransactionType(
                        accountId,
                        transactionType.name()
                );

        return entities.stream()
                .map(transactionMapper::toDomain)
                .toList();
    }
}
