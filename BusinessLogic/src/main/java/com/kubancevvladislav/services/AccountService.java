package com.kubancevvladislav.services;

import com.kubancevvladislav.domain.Account;
import com.kubancevvladislav.domain.Event;
import com.kubancevvladislav.domain.transactions.DepositTransaction;
import com.kubancevvladislav.domain.transactions.Transaction;
import com.kubancevvladislav.domain.transactions.TransferTransaction;
import com.kubancevvladislav.domain.transactions.WithdrawTransaction;
import com.kubancevvladislav.domainServices.AccountServiceInterface;
import com.kubancevvladislav.entities.AccountEntity;
import com.kubancevvladislav.entities.UserEntity;
import com.kubancevvladislav.repositories.AccountRepositoryInterface;
import com.kubancevvladislav.repositories.FriendsRepositoryInterface;
import com.kubancevvladislav.repositories.TransactionsRepositoryInterface;
import com.kubancevvladislav.repositories.UserRepositoryInterface;
import com.kubancevvladislav.services.mapper.TransactionMapperInterface;
import com.kubancevvladislav.services.result.types.*;
import com.kubancevvladislav.services.mapper.AccountMapperInterface;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Сервис для управления счетами пользователей.
 * Реализует операции создания счёта, депозита, снятия средств, перевода и другие связанные с счетами операции.
 * Взаимодействует с репозиториями пользователей, счетов и транзакций.
 * ({@link UserRepositoryInterface}, {@link AccountRepositoryInterface}, {@link TransactionsRepositoryInterface})
 * @author Кубанцев Владислав
 * @version 1.2
 * @since 1.0
 * @see <a href="https://github.com/Mangust1333">GitHub: Mangust1333</a>
 */
@Service
@AllArgsConstructor
public class AccountService implements AccountServiceInterface {
    /** Репозиторий, в котором хранится информация о пользователях */
    private final UserRepositoryInterface userRepository;
    /** Репозиторий, в котором хранится информация о счетах */
    private final AccountRepositoryInterface accountRepository;
    /** Репозиторий, в котором хранится информация о транзакциях */
    private final TransactionsRepositoryInterface transactionsRepository;
    /** Репозиторий, в котором хранится информация о друзьях пользователей */
    private final FriendsRepositoryInterface friendsRepository;
    /** Маппер для перевода domain в entity и наоборот */
    @Qualifier("accountMapperInterfaceImpl")
    private final AccountMapperInterface accountMapper;
    @Qualifier("transactionMapperInterfaceImpl")
    private final TransactionMapperInterface transactionMapper;
    /** инстанс публишера кафки */
    private final PublisherService kafkaPublisher;


    /**
     * Создает новый счёт для пользователя с указанным логином.
     * Если пользователь не существует, возвращается ошибка
     * @param userLogin логин пользователя
     * @return результат создания счёта, либо ошибка, если пользователь не найден
     */
    @Transactional
    @Override
    public CreateAccountResultType createAccount(String userLogin) {
        if (!userRepository.existsByLogin(userLogin)) {
            return CreateAccountResultType.userDoesNotExist();
        }

        Account account = Account.builder()
                .userLogin(userLogin)
                .balance(BigDecimal.ZERO)
                .build();
        AccountEntity savedEntity = accountRepository.save(accountMapper.toEntity(account));
        Event event = Event.builder()
                .eventName("Создание счёта")
                .eventDescription("Время события: " + Instant.now().toString())
                .eventData(List.of(savedEntity))
                .build();
        kafkaPublisher.sendAccountEvent(savedEntity.getId(), event);
        return CreateAccountResultType.success(accountMapper.toDomain(savedEntity));
    }


    /**
     * Возвращает счёт по номеру.
     *
     * @param id номер счёта
     * @return {@link Optional<Account>} с найденным счётом или пустое значение, если счёт не найден
     */
    @Override
    public Optional<Account> getAccountById(BigInteger id) {
        return this.accountRepository.findById(id).map(accountMapper::toDomain);
    }

    /**
     * Возвращает все счета в системе.
     *
     * @return список всех {@link Account}, соответствующих всем пользователям
     */
    @Override
    public List<Account> getAllAccounts() {
        List<AccountEntity> accountEntities = accountRepository.findAll();
        return accountEntities.stream()
                .map(accountMapper::toDomain)
                .collect(Collectors.toList());
    }

    /**
     * Возвращает список счетов по логину пользователя.
     *
     * @param login логин пользователя
     * @return список {@link Account}, соответствующих пользователю
     */
    @Override
    public List<Account> getAccountsByUserLogin(String login) {
        List<AccountEntity> accountEntities = this.accountRepository.findByUserLogin(login);

        List<Account> accounts = accountEntities.stream()
                .map(accountMapper::toDomain)
                .collect(Collectors.toList());
        return accounts;
    }


    /**
     * Выполняет депозит на указанный счёт.
     * Проверяет, чтобы сумма депозита была положительной и счёт существовал.
     *
     * @param accountId номер счёта
     * @param amount сумма депозита
     * @return результат операции депозита
     */
    @Transactional
    @Override
    public AccountDepositResultType deposit(BigInteger accountId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            return AccountDepositResultType.negativeBalance();
        }

        Account account = getAccountById(accountId).orElse(null);
        if (account == null) {
            return AccountDepositResultType.accountDoesNotExists(accountId);
        }

        BigDecimal new_balance = account.getBalance().add(amount);
        account.setBalance(new_balance);
        this.accountRepository.save(accountMapper.toEntity(account));

        Transaction depositTransaction = DepositTransaction.builder()
                .accountId(accountId)
                .amount(amount)
                .build();
        this.transactionsRepository.save(transactionMapper.toEntity(depositTransaction));

        Event event = Event.builder()
                .eventName("Пополнение счёта")
                .eventDescription("Время события: " + Instant.now().toString())
                .eventData(List.of(account, depositTransaction))
                .build();
        kafkaPublisher.sendAccountEvent(account.getId(), event);
        return AccountDepositResultType.success();
    }


    /**
     * Выполняет операцию снятия средств со счёта.
     * Проверяет, чтобы сумма снятия была положительной, а также чтобы на счете было достаточно средств.
     *
     * @param accountId номер счёта
     * @param amount сумма снятия
     * @return результат операции снятия
     */
    @Transactional
    @Override
    public AccountWithdrawResultType withdraw(BigInteger accountId, BigDecimal amount) {
        if(amount.compareTo(BigDecimal.ZERO) < 0) {
            return AccountWithdrawResultType.negativeBalance();
        }

        Account account = getAccountById(accountId).orElse(null);
        if (account == null) {
            return AccountWithdrawResultType.accountDoesNotExists(accountId);
        }

        if (validateWithdrawBalance(account, amount)) {
            account.setBalance(account.getBalance().subtract(amount));
            this.accountRepository.save(accountMapper.toEntity(account));

            Transaction withdrawTransaction = WithdrawTransaction.builder()
                    .accountId(accountId)
                    .amount(amount)
                    .build();
            this.transactionsRepository.save(transactionMapper.toEntity(withdrawTransaction));
            Event event = Event.builder()
                    .eventName("Снятие со счёта")
                    .eventDescription("Время события: " + Instant.now().toString())
                    .eventData(List.of(account, withdrawTransaction))
                    .build();
            kafkaPublisher.sendAccountEvent(account.getId(), event);
            return AccountWithdrawResultType.success();
        } else {
            return AccountWithdrawResultType.insufficientFunds(account.getBalance(), amount);
        }

    }


    /**
     * Выполняет операцию перевода средств между двумя счетами.
     * Проверяет наличие обоих счетов и достаточно ли средств на счете отправителя.
     * Проверяет, чтобы сумма перевода была положительной.
     * Вычисляет комиссию в зависимости от типа пользователей (друзья или нет).
     *
     * @param fromAccountId номер счета отправителя
     * @param toAccountId номер счета получателя
     * @param amount сумма перевода
     * @return результат операции перевода
     */
    @Transactional
    @Override
    public AccountTransferResultType transfer(BigInteger fromAccountId, BigInteger toAccountId, BigDecimal amount) {
        if(amount.compareTo(BigDecimal.ZERO) < 0) {
            return AccountTransferResultType.negativeBalance();
        }

        Account fromAccount = getAccountById(fromAccountId).orElse(null);
        Account toAccount = getAccountById(toAccountId).orElse(null);
        if (fromAccount == null) { return AccountTransferResultType.accountDoesNotExists(fromAccountId); }
        if (toAccount == null) { return AccountTransferResultType.accountDoesNotExists(toAccountId); }

        AccountCalculateComissionResultType calculateComissionResultType = calculateComission(fromAccount, toAccount, amount);

        if(calculateComissionResultType instanceof AccountCalculateComissionResultType.Success success) {
            if(validateWithdrawBalance(fromAccount, amount.add(success.getComission()))) {
                fromAccount.setBalance(fromAccount.getBalance().subtract(amount.add(success.getComission())));
                toAccount.setBalance(toAccount.getBalance().add(amount));

                this.accountRepository.save(accountMapper.toEntity(fromAccount));
                this.accountRepository.save(accountMapper.toEntity(toAccount));

                TransferTransaction transferTransaction = TransferTransaction.builder()
                        .amount(amount)
                        .commission(success.getComission())
                        .senderAccountId(fromAccountId)
                        .receiverAccountId(toAccountId)
                        .build();

                this.transactionsRepository.save(transactionMapper.toEntity(transferTransaction));
                Event event = Event.builder()
                        .eventName("Перевод денег")
                        .eventDescription("Время события: " + Instant.now().toString())
                        .eventData(List.of(fromAccount, toAccount, transferTransaction))
                        .build();
                kafkaPublisher.sendAccountEvent(fromAccount.getId(), event);
                kafkaPublisher.sendAccountEvent(toAccount.getId(), event);
            } else {
                return AccountTransferResultType.insufficientFunds(fromAccount.getBalance(), amount);
            }
        } else if(calculateComissionResultType instanceof AccountCalculateComissionResultType.CanNotFindAccountOwner fail) {
            return AccountTransferResultType.canNotFindAccountOwner(fail.getAccountId());
        }

        return AccountTransferResultType.success();
    }


    /**
     * Проверяет, достаточно ли средств на счете для проведения операции снятия.
     *
     * @param account счёт, с которого производится снятие
     * @param amount сумма снятия
     * @return {@code true}, если средств достаточно, иначе {@code false}
     */
    private boolean validateWithdrawBalance(Account account, BigDecimal amount) {
        return account.getBalance().compareTo(amount) >= 0;
    }


    /**
     * Вычисляет комиссию за перевод между пользователями в зависимости от их статуса (друзья или нет).
     * @param fromAccount счёт отправителя
     * @param toAccount счёт получателя
     * @param amount сумма перевода
     * @return результат вычисления комиссии
     */
    private AccountCalculateComissionResultType calculateComission(Account fromAccount, Account toAccount, BigDecimal amount) {
        UserEntity sender = userRepository.findByLogin(fromAccount.getUserLogin()).orElse(null);
        UserEntity receiver = userRepository.findByLogin(toAccount.getUserLogin()).orElse(null);
        if (sender == null) {
            return AccountCalculateComissionResultType.canNotFindOwner(fromAccount.getId());
        }
        if (receiver == null) {
            return AccountCalculateComissionResultType.canNotFindOwner(toAccount.getId());
        }

        BigDecimal commissionRate;

        if (sender.getLogin().equals(receiver.getLogin())) {
            commissionRate = BigDecimal.ZERO;
        } else if (friendsRepository.existsByUser_LoginAndFriend_Login(sender.getLogin(), receiver.getLogin())) {
            commissionRate = BigDecimal.valueOf(0.03);
        } else {
            commissionRate = BigDecimal.valueOf(0.10);
        }

        BigDecimal commission = amount.multiply(commissionRate);
        return AccountCalculateComissionResultType.success(commission);
    }
}
