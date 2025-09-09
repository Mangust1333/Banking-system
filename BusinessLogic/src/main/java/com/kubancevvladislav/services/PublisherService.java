package com.kubancevvladislav.services;

import com.kubancevvladislav.domain.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
@RequiredArgsConstructor
public class PublisherService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendUserEvent(String userLogin, Event eventData) {
        kafkaTemplate.send("client-topic", userLogin, eventData);
    }

    public void sendAccountEvent(BigInteger accountId, Event eventData) {
        kafkaTemplate.send("account-topic", accountId.toString(), eventData);
    }
}

