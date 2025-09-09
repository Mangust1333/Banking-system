package com.kubancevvladislav.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kubancevvladislav.domain.AccountEvent;
import com.kubancevvladislav.domain.ClientEvent;
import com.kubancevvladislav.services.EventStorageService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventConsumer {
    private final EventStorageService storageService;

    @KafkaListener(topics = "client-topic", groupId = "storage-group")
    public void consumeClient(ConsumerRecord<String, Object> record) {
        storageService.handleClientEvent(record.key(), record.value());
    }

    @KafkaListener(topics = "account-topic", groupId = "storage-group")
    public void consumeAccount(ConsumerRecord<String, Object> record) {
        storageService.handleAccountEvent(record.key(), record.value());
    }
}
