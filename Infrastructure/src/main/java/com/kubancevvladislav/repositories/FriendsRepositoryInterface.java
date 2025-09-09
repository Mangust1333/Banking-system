package com.kubancevvladislav.repositories;

import com.kubancevvladislav.entities.UserFriendsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

/**
 * Интерфейс репозитория для управления друзьями пользователей.
 * Определяет методы для добавления, удаления и получения друзей пользователй.
 * @author Кубанцев Владислав
 * @see <a href="https://github.com/Mangust1333">GitHub: Mangust1333</a>
 * @version 1.2
 * @since 1.0
 */
@Repository
public interface FriendsRepositoryInterface extends JpaRepository<UserFriendsEntity, BigInteger> {

    /**
     * Проверяет, существует ли дружба между двумя пользователями.
     * @param userLogin логин первого пользователя
     * @param friendLogin логин второго пользователя
     * @return true, если дружба существует
     */
    boolean existsByUser_LoginAndFriend_Login(String userLogin, String friendLogin);


    /**
     * Находит все дружбы для указанного пользователя.
     * @param userLogin логин пользователя
     * @return список объектов {@link UserFriendsEntity}, представляющих все дружбы пользователя
     */
    @Query("select u.friend.login from UserFriendsEntity u where u.user.login = :userLogin")
    List<String> getFriends(@Param("userLogin") String userLogin);
}
