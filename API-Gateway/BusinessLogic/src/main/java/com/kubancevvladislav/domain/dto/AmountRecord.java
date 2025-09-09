package com.kubancevvladislav.domain.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public record AmountRecord(BigDecimal amount) implements Serializable {
}
