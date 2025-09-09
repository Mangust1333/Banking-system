package com.kubancevvladislav.domain.dto;

import java.math.BigDecimal;
import java.math.BigInteger;

public record AccountDTO(BigInteger id, String userLogin, BigDecimal balance) {}
