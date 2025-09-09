package com.kubancevvladislav.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigInteger;
import java.time.Instant;

@Entity
@Data
public class AccountEventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigInteger accountId;
    @Lob
    @Convert(converter = JsonAttributeConverter.class)
    private Object payload;
    private Instant timestamp = Instant.now();
}