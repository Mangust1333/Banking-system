package com.kubancevvladislav.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;
import java.util.Objects;

/**
 * Класс представляет собой сущность пользователя, принадлежащую доменной области.
 * <p>Класс содержит методы для получения информации о пользователе, а также для управления списком друзей.</p>
 * @author Кубанцев Владислав
 * @see <a href="https://github.com/Mangust1333">GitHub: Mangust1333</a>
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "login")
@ToString
public class UserEntity {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;
    /** Уникальный идентификатор пользователя или по другому логин*/
    @Column(name = "login", nullable = false, unique = true)
    private String login;
    /** Имя пользователя */
    @Column(name = "name", nullable = false)
    private String name;
    /** Возраст пользователя */
    @Column(name = "age", nullable = false)
    private short age;
    /** Пол пользователя представлен с помощью enum {@code Gender}
     * @see GenderEntity
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private GenderEntity gender;
    /** Цвет волос пользователя представлен с помощью enum {@code HairColor}
     * @see HairColorEntity
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "hair_color", nullable = false)
    private HairColorEntity hairColor;
    /**
     * Метод валидации, вызываемый перед сохранением или обновлением сущности в базе данных.
     * <p>Проверяет, что обязательные поля {@code login}, {@code name}, {@code gender} и {@code hairColor}
     * не равны {@code null}, а также что логин не является пустым и возраст неотрицательный.</p>
     *
     * @throws NullPointerException если одно из обязательных полей имеет значение {@code null}
     * @throws IllegalArgumentException если логин пустой или возраст отрицательный
     */
    @PrePersist
    @PreUpdate
    private void validate() {
        Objects.requireNonNull(login, "Login cannot be null");
        Objects.requireNonNull(name, "Name cannot be null");
        Objects.requireNonNull(gender, "Gender cannot be null");
        Objects.requireNonNull(hairColor, "HairColor cannot be null");

        if (login.isBlank()) {
            throw new IllegalArgumentException("Login cannot be empty");
        }

        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative");
        }
    }
}
