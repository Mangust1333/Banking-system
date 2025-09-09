package com.kubancevvladislav.presentation.controllers.dto;

import com.kubancevvladislav.domain.Gender;
import com.kubancevvladislav.domain.HairColor;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "DTO для создания пользователя")
public record CreateUserDTO(
        String login,
        String name,
        short age,
        Gender gender,
        HairColor hairColor,
        List<String> friends) {}