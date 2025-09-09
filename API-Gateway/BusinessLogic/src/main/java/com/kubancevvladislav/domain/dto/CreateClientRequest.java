package com.kubancevvladislav.domain.dto;

import java.util.List;

public record CreateClientRequest(
        String name,
        String login,
        String password,
        short age,
        GenderDTO gender,
        HairColorDTO hairColor,
        List<String> friends) {}
