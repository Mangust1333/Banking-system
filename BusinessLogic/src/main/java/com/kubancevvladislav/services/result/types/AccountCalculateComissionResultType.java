package com.kubancevvladislav.services.result.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Абстрактный класс, представляющий результат вычисления комиссии для учетной записи.
 * Классы-наследники содержат различные варианты результатов операции, такие как успех или ошибки.
 * <p>Класс наследник может быть успешным</p>
 * <ul>
 *     <li>({@link Success})
 * </ul>
 * <p>Может содержать ошибку</p>
 * <ul>
 *     <li> eсли владелец счета не найден - {@link CanNotFindAccountOwner}.
 * </ul>
 * @author Кубанцев Владислав
 * @see <a href="https://github.com/Mangust1333">GitHub: Mangust1333</a>
 * @version 1.2
 * @since 1.0
 */
public abstract sealed class AccountCalculateComissionResultType
        permits AccountCalculateComissionResultType.Success,
        AccountCalculateComissionResultType.CanNotFindAccountOwner
{
    /**
     * Приватный конструктор, чтобы предотвратить создание экземпляров базового класса.
     */

    private AccountCalculateComissionResultType() {}
    public static Success success(BigDecimal commission) {
        return new Success(commission);
    }

    public static CanNotFindAccountOwner canNotFindOwner(BigInteger accountId) {
        return new CanNotFindAccountOwner(accountId);
    }

    /**
     * Класс, представляющий успешный результат вычисления комиссии.
     */
    @AllArgsConstructor
    @Getter
    public static final class Success extends AccountCalculateComissionResultType {
        /** Значение комиссии */
        private final BigDecimal comission;
    }

    /**
     * Класс, представляющий ошибку: не удалось найти владельца учетной записи.
     */
    @AllArgsConstructor
    @Getter
    public static final class CanNotFindAccountOwner extends AccountCalculateComissionResultType {
        /** Номер счёт */
        private final BigInteger accountId;
    }
}
