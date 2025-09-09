package com.kubancevvladislav.services.result.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Абстрактный класс, представляющий результаты операции удаления друга пользователя.
 * Классы-наследники содержат различные варианты результатов операции, такие как успех или ошибки.
 * <p>Класс-наследник может быть успешным</p>
 * <ul>
 *     <li>({@link Success})
 * </ul>
 * <p>Может содержать ошибку</p>
 * <ul>
 *     <li> Пользователи и так не были друзьями - {@link UsersAreNoLongerFriends}.
 *     <li> Пользователя с таким логином не существует - {@link UserDoNotExists}
 * </ul>
 *
 * @see <a href="https://github.com/Mangust1333">GitHub: Mangust1333</a>
 */
public abstract class RemoveUserFriendResultType {
    /**
     * Приватный конструктор, чтобы предотвратить создание экземпляров базового класса.
     */
    private RemoveUserFriendResultType() {}

    /**
     * Фабричный метод для успешного результата.
     */
    public static Success success() {
        return new Success();
    }

    /**
     * Фабричный метод, если пользователи уже не являются друзьями.
     */
    public static UsersAreNoLongerFriends usersAreNoLongerFriends() {
        return new UsersAreNoLongerFriends();
    }

    /**
     * Фабричный метод, если пользователь не существует.
     * @param login логин несуществующего пользователя
     * @return результат ошибки
     */
    public static UserDoNotExists userDoNotExists(String login) {
        return new UserDoNotExists(login);
    }

    /**
     * Успешный результат удаления друга.
     */
    @NoArgsConstructor
    public final static class Success extends RemoveUserFriendResultType {}

    /**
     * Результат, если пользователи больше не являются друзьями.
     */
    @NoArgsConstructor
    public final static class UsersAreNoLongerFriends extends RemoveUserFriendResultType {}

    /**
     * Результат, если пользователь не существует.
     * Содержит имя пользователя, который не был найден в системе.
     */
    @Getter
    @AllArgsConstructor
    public final static class UserDoNotExists extends RemoveUserFriendResultType {
        /** Логин несуществующего пользователя */
        private String login;
    }
}
