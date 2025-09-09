package com.kubancevvladislav.services.result.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public abstract class UserServiceRegisterResultType {
    private UserServiceRegisterResultType() {}

    public static Success success() {
        return new Success();
    }

    public static UserWithLoginAlreadyExists userWithLoginAlreadyExists(String login) {
        return new UserWithLoginAlreadyExists(login);
    }

    @Getter
    @NoArgsConstructor
    public static final class Success extends UserServiceRegisterResultType {}

    @Getter
    @AllArgsConstructor
    public final static class UserWithLoginAlreadyExists extends UserServiceRegisterResultType {
        private String login;
    }
}
