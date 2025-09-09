package com.kubancevvladislav.domain.dto;

public record CreateAdminRequest (
    String name,
    String password
) {}
