package com.kubancevvladislav.services;

import com.kubancevvladislav.domain.AccountEvent;
import com.kubancevvladislav.domain.ClientEvent;
import com.kubancevvladislav.entities.AccountEventEntity;
import com.kubancevvladislav.entities.ClientEventEntity;
import com.kubancevvladislav.repositories.AccountEventRepository;
import com.kubancevvladislav.repositories.ClientEventRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
@AllArgsConstructor
public class EventStorageService {
    private final AccountEventRepository accountEventRepository;
    private final ClientEventRepository clientEventRepository;

    public void handleClientEvent(String key, Object payload) {
        saveClientEvent(new ClientEvent(key, payload));
    }

    public void handleAccountEvent(String key, Object payload) {
        saveAccountEvent(new AccountEvent(new BigInteger(key), payload));
    }

    public void saveClientEvent(ClientEvent clientEvent) {
        clientEventRepository.save(clientEvent.toClientEventEntity());
    }


    public void saveAccountEvent(AccountEvent accountEvent) {
        accountEventRepository.save(accountEvent.toAccountEventEntity());
    }

    public ClientEvent getClientEventById(Long id) {
        ClientEventEntity entity = clientEventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ClientEvent с id " + id + " не найден"));


        return ClientEvent.fromClientEventEntity(entity);
    }

    public AccountEvent getAccountEventById(Long id) {
        AccountEventEntity entity = accountEventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("AccountEvent с id " + id + " не найден"));

        return AccountEvent.fromAccountEventEntity(entity);
    }

    public List<AccountEvent> getAccountEventsByAccountId(BigInteger accountId) {
        return accountEventRepository.findAccountEventEntitiesByAccountId(accountId).stream()
                .map(AccountEvent::fromAccountEventEntity)
                .toList();
    }

    public List<ClientEvent> getClientEventsByLogin(String login) {
        return clientEventRepository.findClientEventEntitiesByClientLogin(login).stream()
                .map(ClientEvent::fromClientEventEntity)
                .toList();
    }
}
