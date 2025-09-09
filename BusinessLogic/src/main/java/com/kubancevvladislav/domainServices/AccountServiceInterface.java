package com.kubancevvladislav.domainServices;

import com.kubancevvladislav.domain.Account;
import com.kubancevvladislav.services.result.types.AccountDepositResultType;
import com.kubancevvladislav.services.result.types.AccountTransferResultType;
import com.kubancevvladislav.services.result.types.AccountWithdrawResultType;
import com.kubancevvladislav.services.result.types.CreateAccountResultType;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

/**
 * Интерфейс для сервиса управления счетами пользователей.
 * Содержит методы для создания счёта, пополнения, снятия и перевода средств между счетами.
 * Также предоставляет функциональность для получения информации о счетах.
 * @author Кубанцев Владислав
 * @see <a href="https://github.com/Mangust1333">GitHub: Mangust1333</a>
 * @version 1.2
 * @since 1.0
 */
@Service
public interface AccountServiceInterface
{
    /**
     * Создает новый счёт пользователя.
     * @param userLogin логин пользователя, для которого создается счёт.
     * @return результат создания счёта {@link CreateAccountResultType}
     * @see CreateAccountResultType
     */
    @Transactional
    CreateAccountResultType createAccount(String userLogin);

    /**
     * Получает счёт по номеру.
     * @param id уникальный номер
     * @return счёт пользователя, обернутый в {@link Optional}. Если счёт не найден, возвращает пустое значение.
     */
    Optional<Account> getAccountById(BigInteger id);

    List<Account> getAllAccounts();

    /**
     * Получает список счетов пользователя по его логину.
     * @param login логин пользователя, чьи счета необходимо получить
     * @return список счетов пользователя
     * @see Account
     */
    List<Account> getAccountsByUserLogin(String login);

    /**
     * Выполняет пополнение счета на указанную сумму.
     * @param accountId номер счёта, на которую будет зачислена сумма
     * @param amount сумма, на которую будет пополнен счёт
     * @return результат операции пополнения счета {@link AccountDepositResultType}
     * @see AccountDepositResultType
     */
    @Transactional
    AccountDepositResultType deposit(BigInteger accountId, BigDecimal amount);

    /**
     * Выполняет снятие средств с указанного счета.
     * @param accountId номер счёта, с которой будет снята сумма
     * @param amount сумма для снятия
     * @return результат операции снятия средств с счета {@link AccountWithdrawResultType}
     * @see AccountWithdrawResultType
     */
    @Transactional
    AccountWithdrawResultType withdraw(BigInteger accountId, BigDecimal amount);

    /**
     * Выполняет перевод средств между двумя счетами.
     * @param fromAccountId номер счёта отправителя
     * @param toAccountId номер счёта получателя
     * @param amount сумма перевода
     * @return результат операции перевода средств между счетами {@link AccountTransferResultType}
     * @see AccountTransferResultType
     */
    @Transactional
    AccountTransferResultType transfer(BigInteger fromAccountId, BigInteger toAccountId, BigDecimal amount);
}
