package com.kubancevvladislav.services.result.types;

import lombok.*;

import java.math.BigInteger;

/**
 * Абстрактный класс, представляющий результат операции депозита на счет.
 * Классы-наследники содержат различные варианты результатов операции, такие как успех или ошибки.
 * Используется для описания различных возможных исходов операции.
 * <p>Класс наследник может быть успешным</p>
 * <ul>
 *     <li>({@link Success})
 * </ul>
 * <p>Может содержать ошибку</p>
 * <ul>
 *     <li> Счёт не существует - {@link AccountDoesNotExists}.
 *     <li> Отрицательный баланс - {@link NegativeBalance}
 * </ul>
 * @author Кубанцев Владислав
 * @version 1.2
 * @since 1.0
 * @see <a href="https://github.com/Mangust1333">GitHub: Mangust1333</a>
 */
public abstract class AccountDepositResultType
{
    /**
     * Приватный конструктор, чтобы предотвратить создание экземпляров базового класса.
     */
    private AccountDepositResultType() {}

    public static Success success() {
        return new Success();
    }

    public static AccountDoesNotExists accountDoesNotExists(BigInteger id) {
        return new AccountDoesNotExists(id);
    }

    public static NegativeBalance negativeBalance() {
        return new NegativeBalance();
    }

    /**
     * Класс, обозначающий успешный результат операции депозита.
     */
    @Getter
    @NoArgsConstructor
    public static final class Success extends AccountDepositResultType {}

    /**
     * Класс, обозначающий ошибку, когда счет с указанным идентификатором не найден.
     */
    @Getter
    @AllArgsConstructor
    public final static class AccountDoesNotExists extends AccountDepositResultType {
        /** Номер счёта */
        private BigInteger id;
    }

    /**
     * Класс, обозначающий ошибку, когда сумма депозита отрицательная.
     */
    @Getter
    @NoArgsConstructor
    public final static class NegativeBalance extends AccountDepositResultType {}
}
