package com.kubancevvladislav.domainServices;

import com.kubancevvladislav.domain.DTO.UserDTO;
import com.kubancevvladislav.domain.Gender;
import com.kubancevvladislav.domain.HairColor;
import com.kubancevvladislav.domain.User;
import com.kubancevvladislav.services.result.types.AddUserFriendResultType;
import com.kubancevvladislav.services.result.types.CreateUserResultType;
import com.kubancevvladislav.services.result.types.RemoveUserFriendResultType;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

/**
 * Интерфейс для управления пользователями.
 * Определяет методы для создания пользователей, получения информации о них и управления дружескими связями.
 * @author Кубанцев Владислав
 * @see <a href="https://github.com/Mangust1333">GitHub: Mangust1333</a>
 * @version 1.2
 * @since 1.0
 */
@Service
public interface UserServiceInterface
{
    /**
     * Создает нового пользователя на основе переданных данных.
     * @param userDTO объект {@link UserDTO}, содержащий информацию о пользователе
     * @return результат создания пользователя {@link CreateUserResultType}
     * @see UserDTO
     * @see CreateUserResultType
     */
    @Transactional
    CreateUserResultType createUser(UserDTO userDTO);

    /**
     * Возвращает пользователя по его логину.
     * @param login логин пользователя
     * @return объект {@link User}, обернутый в {@link Optional}. Если пользователь не найден, возвращается пустое значение.
     * @see Optional
     */
    Optional<User> getUserByLogin(String login);

    Optional<User> getUserById(BigInteger id);

    List<User> getUsersByHairColorAndGender(HairColor hairColor, Gender gender);

    /**
     * Добавляет пользователей в друзья по логину.
     * @param firstUserLogin  логин первого пользователя
     * @param secondUserLogin логин второго пользователя
     * @return результат добавления друга {@link AddUserFriendResultType}
     * @see AddUserFriendResultType
     */
    @Transactional
    AddUserFriendResultType addUserFriendByLogin(String firstUserLogin, String secondUserLogin);

    /**
    /**
     * Возвращает друзей пользователя по логину
     * @param login
     * @return список друзей пользователя
     */
    List<String> getUserFriendsByLogin(String login);
}
