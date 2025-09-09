package com.kubancevvladislav.services.result.types;

import com.kubancevvladislav.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Абстрактный класс, представляющий результаты операции создания пользователя.
 * Классы-наследники содержат различные варианты результатов операции, такие как успех или ошибки.
 * <p>Класс наследник может быть успешным</p>
 * <ul>
 *     <li>({@link Success})
 * </ul>
 * <p>Может содержать ошибку</p>
 * <ul>
 *     <li> Друга не существует - {@link FriendDoesNotExists}.
 *     <li> Пользователь с таким логином уже существует - {@link UserAlreadyExists}
 * </ul>
 * @see <a href="https://github.com/Mangust1333">GitHub: Mangust1333</a>
 */
public abstract class CreateUserResultType
{
    /**
     * Приватный конструктор, чтобы предотвратить создание экземпляров базового класса.
     */
    private CreateUserResultType () {}

    public static Success success(User createdUser) {
        return new Success(createdUser);
    }

    public static FriendDoesNotExists friendDoesNotExists(String friendLogin) {
        return new FriendDoesNotExists(friendLogin);
    }

    public static UserAlreadyExists userAlreadyExists() {
        return new UserAlreadyExists();
    }

    /**
     * Успешный результат создания пользователя.
     * Содержит информацию о созданном пользователе.
     */
    @Getter
    @AllArgsConstructor
    public final static class Success extends CreateUserResultType
    {
        private User createdUser;
    }

    /**
     * Результат, если друг пользователя не существует.
     * Содержит имя друга, который не был найден.
     */
    @Getter
    @AllArgsConstructor
    public final static class FriendDoesNotExists extends CreateUserResultType {
        /** Логин друга, который не существует */
        private String friendLogin;
    }

    /**
     * Результат, если пользователь с таким логином уже существует.
     */
    @NoArgsConstructor
    public final static class UserAlreadyExists extends CreateUserResultType {}
}
