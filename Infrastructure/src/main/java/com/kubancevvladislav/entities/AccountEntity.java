package com.kubancevvladislav.entities;

import jakarta.persistence.*;
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
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountEntity {
    /**
     * Уникальный идентификатор счёта.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private BigInteger id;
    /**
     * Логин пользователя, связанный с учетной записью.
     */
    @Column(name = "user_login", nullable = false)
    private String userLogin;
    /**
     * Баланс учетной записи пользователя.
     */
    @Column(name = "balance", nullable = false)
    private BigDecimal balance;
    /**
     * Проверка перед вставкой или обновлением записи в БД.
     */
    @PrePersist
    @PreUpdate
    private void validateBalance() {
        if (balance == null || balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new PersistenceException("Balance must be >= 0");
        }
    }
}
