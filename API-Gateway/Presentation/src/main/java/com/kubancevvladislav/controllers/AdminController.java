package com.kubancevvladislav.controllers;

import com.kubancevvladislav.domain.dto.*;
import com.kubancevvladislav.services.ExternalApiService;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {
    private final ExternalApiService externalApiService;

    @PostMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createClientAccount(@RequestBody CreateClientRequest request) {
        return ResponseEntity.ok(externalApiService.createClientAccount(request));
    }

    @PostMapping("/admins")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createAdminAccount(@RequestBody CreateAdminRequest request) {
        return ResponseEntity.ok(externalApiService.createAdminAccount(request));
    }

    @GetMapping("/users/filter")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> filterUsers(@RequestBody UserFilerByHairColorAndGenderRequest request) {
        List<UserDTO> users = externalApiService.filterUsers(request);
        return ResponseEntity.ok(users);
    }


    @GetMapping("/users/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        UserDTO user = externalApiService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/accounts")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllAccounts() {
        List<AccountDTO> accounts = externalApiService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/user/accounts/{userLogin}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUserAccountsByLogin(@PathVariable String userLogin) {
        return ResponseEntity.ok(externalApiService.getUserAccountsByLogin(userLogin));
    }

    @GetMapping("/accounts/{accountId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAccountDetails(@PathVariable Long accountId) {
        return ResponseEntity.ok(externalApiService.getAccountDetails(accountId));
    }
}
