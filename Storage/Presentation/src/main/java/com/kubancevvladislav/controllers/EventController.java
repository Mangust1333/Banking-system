package com.kubancevvladislav.controllers;

import com.kubancevvladislav.domain.AccountEvent;
import com.kubancevvladislav.domain.ClientEvent;
import com.kubancevvladislav.services.EventStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventStorageService eventStorageService;

    @GetMapping("/account/{id}")
    public ResponseEntity<AccountEvent> getAccountEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventStorageService.getAccountEventById(id));
    }

    @GetMapping("/client/{id}")
    public ResponseEntity<ClientEvent> getClientEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventStorageService.getClientEventById(id));
    }

    @GetMapping("/account/key/{accountId}")
    public ResponseEntity<List<AccountEvent>> getAccountEventsByAccountId(@PathVariable BigInteger accountId) {
        return ResponseEntity.ok(eventStorageService.getAccountEventsByAccountId(accountId));
    }

    @GetMapping("/client/key/{login}")
    public ResponseEntity<List<ClientEvent>> getClientEventsByLogin(@PathVariable String login) {
        return ResponseEntity.ok(eventStorageService.getClientEventsByLogin(login));
    }

}
