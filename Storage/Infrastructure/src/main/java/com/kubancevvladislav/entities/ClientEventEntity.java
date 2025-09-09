package com.kubancevvladislav.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Data
public class ClientEventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clientLogin;
    @Lob
    @Convert(converter = JsonAttributeConverter.class)
    private Object payload;
    private Instant timestamp = Instant.now();
}