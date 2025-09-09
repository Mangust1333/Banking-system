package com.kubancevvladislav.domain.DTO;

import com.kubancevvladislav.domain.Gender;
import com.kubancevvladislav.domain.HairColor;

import java.util.List;

/**
 * DTO (Data Transfer Object) для представления данных о пользователе {@link com.kubancevvladislav.domain.User}.
 * Этот класс используется для передачи данных о пользователе.
 * @author Кубанцев Владислав
 * @version 1.0
 * @since 1.0
 * @see <a href="https://github.com/Mangust1333">GitHub: Mangust1333</a>
 * @see <a href="https://ru.wikipedia.org/wiki/DTO">DTO на Wikipedia</a>
 * @see com.kubancevvladislav.domain.User
 * @param login - идентификатор пользователя
 * @param name - имя пользователя
 * @param age - возраст пользователя
 * @param gender - пол пользователя
 * @param hairColor - цвет волос пользователя
 * @param friends - список друзей пользователя
 */
public record UserDTO(
        String login,
        String name,
        short age,
        Gender gender,
        HairColor hairColor,
        List<String> friends) {}
