package com.kubancevvladislav.services.result.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Абстрактный класс, представляющий результаты операции перевода средств между счетами.
 * Классы-наследники содержат различные варианты результатов операции, такие как успех или ошибки.
 * <p>Класс наследник может быть успешным</p>
 * <ul>
 *     <li>({@link Success})
 * </ul>
 * <p>Может содержать ошибку</p>
 * <ul>
 *     <li> Счёт не существует - {@link AccountDoesNotExists}.
 *     <li> Не хватает денег для совершения перевода - {@link InsufficientFunds}
 *     <li> Не Возвращается найти владельца счёта - {@link CanNotFindAccountOwner}
 *     <li> Отрицательный баланс - {@link NegativeBalance}
 *     <li> Другие причины - {@link CanNotFindAccountOwner}
 * </ul>
 * @see <a href="https://github.com/Mangust1333">GitHub: Mangust1333</a>
 */
public abstract class AccountTransferResultType {
    /**
     * Приватный конструктор, чтобы предотвратить создание экземпляров базового класса.
     */
    private AccountTransferResultType() {}

    public static Success success() {
        return new Success();
    }

    public static AccountDoesNotExists accountDoesNotExists(BigInteger id) {
        return new AccountDoesNotExists(id);
    }

    public static InsufficientFunds insufficientFunds(BigDecimal accountFunds, BigDecimal requestedFunds) {
        return new InsufficientFunds(accountFunds, requestedFunds);
    }

    public static CanNotFindAccountOwner canNotFindAccountOwner(BigInteger accountId) {
        return new CanNotFindAccountOwner(accountId);
    }

    public static Failed failed() {
        return new Failed();
    }

    public static NegativeBalance negativeBalance() {
        return new NegativeBalance();
    }
    /**
     * Успешный результат перевода.
     */
    @NoArgsConstructor
    public final static class Success extends AccountTransferResultType {}

    /**
     * Результат, если счёт, на который осуществляется перевод, не существует.
     */
    @Getter
    @AllArgsConstructor
    public final static class AccountDoesNotExists extends AccountTransferResultType {
        /** Номер несуществующего счёта */
        private BigInteger id;
    }

    /**
     * Результат, если на счету недостаточно средств для перевода.
     */
    @Getter
    @AllArgsConstructor
    public final static class InsufficientFunds extends AccountTransferResultType {
        private final BigDecimal AccountFunds;
        private final BigDecimal RequestedFunds;
    }

    /**
     * Результат, если не удается найти владельца счёта.
     */
    @Getter
    @AllArgsConstructor
    public static final class CanNotFindAccountOwner extends AccountTransferResultType {
        /** Номер счёта, владелец которого отсутствует */
        private final BigInteger accountId;
    }

    /**
     * Результат, если операция перевода не удалась по неизвестной причине.
     */
    @NoArgsConstructor
    public final static class Failed extends AccountTransferResultType {}

    /**
     * Класс, обозначающий ошибку, когда сумма депозита отрицательная.
     */
    @NoArgsConstructor
    public final static class NegativeBalance extends AccountTransferResultType {}
}
