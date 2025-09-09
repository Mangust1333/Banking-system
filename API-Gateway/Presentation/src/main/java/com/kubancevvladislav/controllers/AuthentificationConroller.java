package com.kubancevvladislav.controllers;

import com.kubancevvladislav.domain.dto.LoginRequest;
import com.kubancevvladislav.services.ExternalApiService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping
public class AuthentificationConroller {
    ExternalApiService externalApiService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(externalApiService.authenticate(loginRequest));
    }
}
