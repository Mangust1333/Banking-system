package com.kubancevvladislav.presentation.controllers.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.math.BigInteger;

@Schema(description = "DTO для возврата информации о счёте")
public record CheckAccountDTO(BigInteger id, String userLogin, BigDecimal balance) {}
