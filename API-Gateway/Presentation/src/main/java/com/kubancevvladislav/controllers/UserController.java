package com.kubancevvladislav.controllers;

import com.kubancevvladislav.domain.dto.OperationResponseDTO;
import com.kubancevvladislav.services.ExternalApiService;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.math.BigInteger;
// метод который возвращает список имен друзей с индетификаторами их аккаунтов
@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private final ExternalApiService externalApiService;

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public ResponseEntity<?> getMyInfo(Authentication auth) {
        return ResponseEntity.ok(externalApiService.getMyInfo(auth));
    }

    @GetMapping("/me/accounts")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public ResponseEntity<?> getUserAccountsByLogin(Authentication auth) {
        return ResponseEntity.ok(externalApiService.getUserAccountsByLogin(auth));
    }

    @GetMapping("/accounts/{accountId}")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public ResponseEntity<?> getMyAccountById(@PathVariable BigInteger accountId, Authentication auth) throws AccountNotFoundException {
        return ResponseEntity.ok(externalApiService.getMyAccountById(accountId, auth));
    }

    @PostMapping("/friends/{friendLogin}")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public ResponseEntity<?> addFriend(@PathVariable String friendLogin, Authentication auth) {
        return ResponseEntity.ok(externalApiService.addFriend(friendLogin, auth));
    }


    @GetMapping("/friends/get")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public ResponseEntity<?> addFriend(Authentication auth) {
        return ResponseEntity.ok(externalApiService.checkMyFriends(auth));
    }

    @PostMapping("/{id}/deposit")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public ResponseEntity<OperationResponseDTO> depositToAccount(@PathVariable BigInteger id, @RequestBody BigDecimal amount, Authentication auth) throws AccountNotFoundException {
        return ResponseEntity.ok(externalApiService.depositToAccount(id, amount, auth));
    }

    @PostMapping("/{id}/withdraw")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public ResponseEntity<OperationResponseDTO> withdrawFromAccount(@PathVariable BigInteger id, @RequestBody BigDecimal amount, Authentication auth) throws AccountNotFoundException {
        return ResponseEntity.ok(externalApiService.withdrawFromAccount(id, amount, auth));
    }

    @PostMapping("/accounts/{fromAccountId}/{toAccountId}/transfer")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public ResponseEntity<OperationResponseDTO> transferMoney(@PathVariable BigInteger fromAccountId, @PathVariable BigInteger toAccountId, @RequestBody BigDecimal amount, Authentication auth) throws AccountNotFoundException {
        return ResponseEntity.ok(externalApiService.transferMoney(fromAccountId, toAccountId, amount, auth));
    }
}
