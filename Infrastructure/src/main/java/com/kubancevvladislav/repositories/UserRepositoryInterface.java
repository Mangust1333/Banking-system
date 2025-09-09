package com.kubancevvladislav.repositories;

import com.kubancevvladislav.entities.GenderEntity;
import com.kubancevvladislav.entities.HairColorEntity;
import com.kubancevvladislav.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

/**
 * Интерфейс репозитория для работы с пользователями.
 * Определяет методы для создания, поиска, обновления и проверки существования пользователей.
 *
 * @author Кубанцев Владислав
 * @version 1.2
 * @since 1.0
 * @see <a href="https://github.com/Mangust1333">GitHub: Mangust1333</a>
 */
@Repository
public interface UserRepositoryInterface extends JpaRepository<UserEntity, String> {
    /**
     * Возвращает пользователя по его логину.
     * @param login логин пользователя
     * @return объект {@link UserEntity}, обернутый в {@link Optional}, или пустой {@link Optional}, если пользователь не найден
     */
    Optional<UserEntity> findByLogin(String login);


    /**
     * Проверяет, существует ли пользователь с данным логином.
     * @param login логин пользователя
     * @return {@code true}, если пользователь существует, иначе {@code false}
     */
    boolean existsByLogin(String login);

    Optional<UserEntity> findUserEntityById(BigInteger id);

    List<UserEntity> findByHairColorAndGender(HairColorEntity hairColor, GenderEntity gender);
}
