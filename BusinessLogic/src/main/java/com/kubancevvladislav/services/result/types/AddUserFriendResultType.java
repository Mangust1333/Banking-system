package com.kubancevvladislav.services.result.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Абстрактный класс, представляющий результаты операции добавления пользователя в друзья.
 * Классы-наследники содержат различные варианты результатов операции, такие как успех или ошибки.
 * <p>Этот класс используется для обработки результатов операции добавления друга для пользователя в системе.</p>
 * <p>Класс наследник может быть успешным</p>
 * <ul>
 *     <li>({@link Success})
 * </ul>
 * <p>Может содержать ошибку</p>
 * <ul>
 *     <li> Пользователи уже друзья - {@link UsersAreAlreadyFriends}.
 *     <li> Пользователя не существет - {@link UserDoNotExists}
 * </ul>
 * @see <a href="https://github.com/Mangust1333">GitHub: Mangust1333</a>
 */
public abstract class AddUserFriendResultType {
    /**
     * Приватный конструктор, чтобы предотвратить создание экземпляров базового класса.
     */
    private AddUserFriendResultType() {}

    public static Success success() {
        return new Success();
    }

    public static UsersAreAlreadyFriends usersAreAlreadyFriends() {
        return new UsersAreAlreadyFriends();
    }

    public static UserDoNotExists userDoNotExists(String login) {
        return new UserDoNotExists(login);
    }

    public static UserCanNotAddHimself userCanNotAddHimself() {
        return new UserCanNotAddHimself();
    }

    /**
     * Успешный результат добавления пользователя в друзья.
     */
    @NoArgsConstructor
    public final static class Success extends AddUserFriendResultType {}

    /**
     * Результат, если пользователи уже являются друзьями.
     */
    @NoArgsConstructor
    public final static class UsersAreAlreadyFriends extends AddUserFriendResultType {}

    /**
     * Результат, если один из пользователей не существует.
     */
    @Getter
    @AllArgsConstructor
    public final static class UserDoNotExists extends AddUserFriendResultType {
        private String login;
    }

    @NoArgsConstructor
    public final static class UserCanNotAddHimself extends AddUserFriendResultType {}
}
