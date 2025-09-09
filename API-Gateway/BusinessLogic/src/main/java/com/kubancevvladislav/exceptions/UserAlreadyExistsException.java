package com.kubancevvladislav.exceptions;

import lombok.Getter;

@Getter
public class UserAlreadyExistsException extends RuntimeException {
    private final String login;

    public UserAlreadyExistsException(String login) {
        super("Пользователь уже существует: " + login);
        this.login = login;
    }
}
