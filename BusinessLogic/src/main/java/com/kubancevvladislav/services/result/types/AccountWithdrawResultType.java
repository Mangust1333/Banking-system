package com.kubancevvladislav.services.result.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Абстрактный класс, представляющий результаты операции снятия средств со счёта.
 * Классы-наследники содержат различные варианты результатов операции, такие как успех или ошибки.
 * <p>Этот класс используется для обработки результатов операции снятия средств со счёта в системе.</p>
 * <p>Класс наследник может быть успешным</p>
 * <ul>
 *     <li>({@link Success})
 * </ul>
 * <p>Может содержать ошибку</p>
 * <ul>
 *     <li> Счёт не существует - {@link AccountDoesNotExists}.
 *     <li> Не хватает денег для снятия - {@link InsufficientFunds}
 *     <li> На вход подаётся отрицательный баланс - {@link NegativeBalance}
 * </ul>
 * @see <a href="https://github.com/Mangust1333">GitHub: Mangust1333</a>
 */
public abstract class AccountWithdrawResultType
{
    /**
     * Приватный конструктор, чтобы предотвратить создание экземпляров базового класса.
     */
    private AccountWithdrawResultType() {}

    public static Success success() {
        return new Success();
    }

    public static AccountDoesNotExists accountDoesNotExists(BigInteger id) {
        return new AccountDoesNotExists(id);
    }

    public static InsufficientFunds insufficientFunds(BigDecimal accountFunds, BigDecimal requestedFunds) {
        return new InsufficientFunds(accountFunds, requestedFunds);
    }

    public static NegativeBalance negativeBalance() {
        return new NegativeBalance();
    }
    /**
     * Успешный результат снятия средств.
     */
    @NoArgsConstructor
    public static class Success extends AccountWithdrawResultType {}

    /**
     * Результат, если счёт, с которого пытаются снять средства, не существует.
     */
    @Getter
    @AllArgsConstructor
    public final static class AccountDoesNotExists extends AccountWithdrawResultType {
        private BigInteger id;
    }

    /**
     * Результат, если на счету недостаточно средств для снятия.
     */
    @Getter
    @AllArgsConstructor
    public static class InsufficientFunds extends AccountWithdrawResultType {
        private final BigDecimal AccountFunds;
        private final BigDecimal RequestedFunds;
    }

    /**
     * Результат, если на вход подаётся отрицательная сумма денег.
     */
    @NoArgsConstructor
    public static class NegativeBalance extends AccountWithdrawResultType {}
}
