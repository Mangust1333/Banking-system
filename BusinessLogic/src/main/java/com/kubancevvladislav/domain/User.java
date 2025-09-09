package com.kubancevvladislav.domain;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.function.Supplier;
import java.util.Objects;

/**
 * Класс представляет собой сущность пользователя, принадлежащую доменной области.
 * <p>Класс содержит методы для получения информации о пользователе, а также для управления списком друзей.</p>
 * @author Кубанцев Владислав
 * @see <a href="https://github.com/Mangust1333">GitHub: Mangust1333</a>
 * @version 1.2
 * @since 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class User {
    BigInteger id;
    /** Уникальный идентификатор пользователя или по другому логин*/
    private String login;
    /** Имя пользователя */
    private String name;
    /** Возраст пользователя */
    private short age;
    /** Пол пользователя представлен с помощью enum {@code Gender}
     * @see Gender
     */
    private Gender gender;
    /** Цвет волос пользователя представлен с помощью enum {@code HairColor}
     * @see HairColor
     */
    private HairColor hairColor;
    /**
     * Метод валидации, вызываемый перед сохранением или обновлением сущности.
     * <p>Проверяет, что обязательные поля {@code login}, {@code name}, {@code gender} и {@code hairColor}
     * не равны {@code null}, а также что логин не является пустым и возраст неотрицательный.</p>
     *
     * @throws NullPointerException если одно из обязательных полей имеет значение {@code null}
     * @throws IllegalArgumentException если логин пустой или возраст отрицательный
     */
    @PrePersist
    @PreUpdate
    private void validate() {
        Objects.requireNonNull(id, "Id cannot be null");
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
