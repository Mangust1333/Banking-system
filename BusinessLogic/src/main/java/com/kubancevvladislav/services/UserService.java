package com.kubancevvladislav.services;

import com.kubancevvladislav.domain.DTO.UserDTO;
import com.kubancevvladislav.domain.Event;
import com.kubancevvladislav.domain.Gender;
import com.kubancevvladislav.domain.HairColor;
import com.kubancevvladislav.domain.User;
import com.kubancevvladislav.entities.*;
import com.kubancevvladislav.repositories.FriendsRepositoryInterface;
import com.kubancevvladislav.repositories.UserRepositoryInterface;
import com.kubancevvladislav.domainServices.UserServiceInterface;
import com.kubancevvladislav.services.mapper.UserMapperInterface;
import com.kubancevvladislav.services.result.types.AddUserFriendResultType;
import com.kubancevvladislav.services.result.types.CreateUserResultType;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с пользователями, реализующий {@link UserServiceInterface}.
 * Этот класс предоставляет методы для создания пользователей, добавления и удаления друзей,
 * а также получения информации о пользователях.
 *
 * <p>Основные функции:
 * 1. Создание пользователя.
 * 2. Получение пользователя по логину.
 * 3. Добавление друга пользователю.
 * 4. Удаление друга у пользователя.
 *
 * @author Кубанцев Владислав
 * @version 1.2
 * @since 1.0
 * @see <a href="https://github.com/Mangust1333">GitHub: Mangust1333</a>
 */
@Service
@AllArgsConstructor
public class UserService implements UserServiceInterface {
    /** Репозиторий, в котором хранится информация о пользователях */
    private final UserRepositoryInterface userRepository;
    /** Репозиторий, в котором храниться информация о друзьях пользователей */
    private final FriendsRepositoryInterface friendsRepository;
    /** Мапер для перевода из entity в domain и наоборот */
    @Qualifier("userMapperInterfaceImpl")
    private final UserMapperInterface userMapper;
    /** инстанс публишера кафки */
    private final PublisherService kafkaPublisher;


    /**
     * Создает нового пользователя в системе.
     * Если пользователь с таким логином уже существует, возвращает ошибку.
     * Также проверяет, существуют ли все указанные друзья, и если хотя бы один не существует,
     * возвращает ошибку.
     * @param userDTO данные нового пользователя
     * @return результат операции создания пользователя
     */
    @Override
    @Transactional
    public CreateUserResultType createUser(UserDTO userDTO) {
        if (userRepository.existsByLogin(userDTO.login())) {
            return CreateUserResultType.userAlreadyExists();
        }

        for (String friend : userDTO.friends()) {
            if (!userRepository.existsByLogin(friend)) {
                return CreateUserResultType.friendDoesNotExists(friend);
            }
        }
        User user = userMapper.toDomain(userDTO);
        UserEntity entity = userMapper.toEntity(user);
        UserEntity savedEntity;

        try {
            savedEntity = userRepository.save(entity);
        } catch (DataIntegrityViolationException e) {
            return CreateUserResultType.userAlreadyExists();
        }

        userDTO.friends().forEach(friend -> addUserFriendByLogin(friend, userDTO.login()));

        Event event = Event.builder()
                .eventName("Пользователь создан")
                .eventDescription("Время события: " + Instant.now().toString())
                .eventData(List.of(entity))
                .build();

        kafkaPublisher.sendUserEvent(entity.getLogin(), event);
        return CreateUserResultType.success(userMapper.toDomain(savedEntity));
    }


    /**
     * Возвращает пользователя по его логину.
     * @param login логин пользователя
     * @return объект {@link Optional}, содержащий пользователя, если он найден
     */
    @Override
    public Optional<User> getUserByLogin(String login) {
        return userRepository.findByLogin(login).map(userMapper::toDomain);
    }

    @Override
    public Optional<User> getUserById(BigInteger id) {
        return userRepository.findUserEntityById(id).map(userMapper::toDomain);
    }

    /**
     * Возвращает список пользователей, отфильтрованных по цвету волос и гендеру.
     * @param hairColor цвет волос
     * @param gender гендер
     * @return список пользователей, соответствующих фильтрам
     */
    @Override
    public List<User> getUsersByHairColorAndGender(HairColor hairColor, Gender gender) {
        return userRepository.findByHairColorAndGender(
                HairColorEntity.valueOf(hairColor.toString()),
                GenderEntity.valueOf(gender.toString())
        ).stream().map(userMapper::toDomain).toList();
    }

    /**
     * Добавляет пользователей в друзья по логинам.
     * Если один или оба пользователя не существуют, возвращается ошибка.
     * Если пользователи уже являются друзьями, возвращается ошибка.
     * @param firstUserLogin логин первого пользователя
     * @param secondUserLogin логин второго пользователя
     * @return результат операции добавления в друзья
     */
    @Override
    @Transactional
    public AddUserFriendResultType addUserFriendByLogin(String firstUserLogin, String secondUserLogin) {
        if(firstUserLogin.equals(secondUserLogin)) {
            return AddUserFriendResultType.userCanNotAddHimself();
        }

        UserEntity user1 = userRepository.findByLogin(firstUserLogin).orElse(null);
        if (user1 == null) { return AddUserFriendResultType.userDoNotExists(firstUserLogin); }

        UserEntity user2 = userRepository.findByLogin(secondUserLogin).orElse(null);
        if (user2 == null) { return AddUserFriendResultType.userDoNotExists(secondUserLogin); }

        if (friendsRepository.existsByUser_LoginAndFriend_Login(user1.getLogin(), user2.getLogin())
                || friendsRepository.existsByUser_LoginAndFriend_Login(user2.getLogin(), user1.getLogin())) {
            return AddUserFriendResultType.usersAreAlreadyFriends();
        } else {
            friendsRepository.save(new UserFriendsEntity(user1, user2));
            friendsRepository.save(new UserFriendsEntity(user2, user1));

            Event event = Event.builder()
                    .eventName("Пользователи добавлены в друзья")
                    .eventDescription("Время события: " + Instant.now().toString())
                    .eventData(List.of(userMapper.toDomain(user1), userMapper.toDomain(user2)))
                    .build();

            kafkaPublisher.sendUserEvent(user1.getLogin(), event);
            kafkaPublisher.sendUserEvent(user2.getLogin(), event);

            return AddUserFriendResultType.success();
        }
    }

    /**
     * Возвращает список друзей пользователя по логину
     * Если пользователь не существует, то вернёт пустой список
     * Если пользователь существует, но друзья не найдены, то вернёт пустой список
     * @param login логин пользователя
     * @return список друзей пользователя
     */
    @Override
    public List<String> getUserFriendsByLogin(String login) {
        if(!userRepository.existsByLogin(login)) {
            return new ArrayList<>();
        }

        return friendsRepository.getFriends(login);
    }

    public List<String> getUserFriendsById(BigInteger id) {
        Optional<UserEntity> userEntity = userRepository.findUserEntityById(id);
        if(userEntity.isEmpty()) {
            return new ArrayList<>();
        }

        return friendsRepository.getFriends(userEntity.get().getLogin());
    }


}
