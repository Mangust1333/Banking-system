package com.kubancevvladislav.entities;
import java.io.Serializable;
import java.math.BigInteger;

import jakarta.persistence.Embeddable;
import lombok.*;
/**
 * Класс представляет составной первичный ключ для сущности {@code UserFriendsEntity},
 * содержащий логины пользователя и его друга.
 * Используется для идентификации записи о дружбе между двумя пользователями.
 * @author Кубанцев Владислав
 * @see <a href="https://github.com/Mangust1333">GitHub: Mangust1333</a>
 * @version 1.0
 * @since 1.0
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFriendsPK implements Serializable {
    /** Логин пользователя */
    private BigInteger user_id;
    /** Логин пользователя, добавленного в друзья. */
    private BigInteger friend_id;
}
