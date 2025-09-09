package com.kubancevvladislav.repositories;

import com.kubancevvladislav.entities.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

/**
 * Интерфейс репозитория для управления счетами.
 * Определяет методы для создания, поиска и обновления счетов.
 * @author Кубанцев Владислав
 * @see <a href="https://github.com/Mangust1333">GitHub: Mangust1333</a>
 * @version 1.2
 * @since 1.0
 */
@Repository
public interface AccountRepositoryInterface extends JpaRepository<AccountEntity, BigInteger>
{
    /**
     * Ищет счета, принадлежащие пользователю с указанным логином.
     * @param login логин пользователя
     * @return список счетов, принадлежащих пользователю
     */
    List<AccountEntity> findByUserLogin(String login);
}
