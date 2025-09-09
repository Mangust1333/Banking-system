package com.kubancevvladislav.domain;

import jakarta.persistence.PersistenceException;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Класс представляет счёт пользователя, включающий информацию о балансе и логине пользователя.
 * @author Кубанцев Владислав
 * @see <a href="https://github.com/Mangust1333">GitHub: Mangust1333</a>
 * @version 1.2
 * @since 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    /**
     * Уникальный идентификатор счёта.
     * @param id id пользователя
     */
    private BigInteger id;
    /**
     * Логин пользователя, связанный с учетной записью.
     */
    private String userLogin;
    /**
     * Баланс учетной записи пользователя.
     */
    private BigDecimal balance;
    /**
     * Проверка перед вставкой или обновлением.
     */
    @PrePersist
    @PreUpdate
    private void validateBalance() {
        if (balance == null || balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new PersistenceException("Balance must be >= 0");
        }
    }
}
