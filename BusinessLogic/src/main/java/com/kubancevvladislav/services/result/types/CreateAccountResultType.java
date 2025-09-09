package com.kubancevvladislav.services.result.types;

import com.kubancevvladislav.domain.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Абстрактный класс, представляющий результаты операции создания счёта.
 * <p>Этот класс используется для обработки результатов создания счёта в системе.</p>
 * Классы-наследники содержат различные варианты результатов операции, такие как успех или ошибка.
 * <p>Класс наследник может быть успешным</p>
 * <ul>
 *     <li>({@link Success})
 * </ul>
 * <p>Может содержать ошибку</p>
 * <ul>
 *     <li> Пользователя, к которому привязывается счёт не существует - {@link UserDoesNotExist}.
 * </ul>
 * @see <a href="https://github.com/Mangust1333">GitHub: Mangust1333</a>
 */
public abstract class CreateAccountResultType
{
    /**
     * Приватный конструктор, чтобы предотвратить создание экземпляров базового класса.
     */
    private CreateAccountResultType() {}

    /**
     * Статический фабричный метод для создания успешного результата с информацией о счёте.
     */
    public static Success success(Account account) {
        return new Success(account);
    }

    /**
     * Статический фабричный метод для создания ошибки, если пользователь не существует.
     */
    public static UserDoesNotExist userDoesNotExist() {
        return new UserDoesNotExist();
    }

    /**
     * Успешный результат создания аккаунта.
     * Содержит информацию о созданном аккаунте.
     */
    @Getter
    @AllArgsConstructor
    public static final class Success extends CreateAccountResultType {
        /** Экземпляр успешно созданного счёта */
        private Account account;
    }

    /**
     * Результат, если пользователь к которому привязывается счёт не найден.
     */
    @NoArgsConstructor
    public static final class UserDoesNotExist extends CreateAccountResultType {}
}
