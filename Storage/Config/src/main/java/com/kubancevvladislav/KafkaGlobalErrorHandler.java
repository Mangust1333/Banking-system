package com.kubancevvladislav;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KafkaGlobalErrorHandler extends DefaultErrorHandler {

    private static final Logger log = LoggerFactory.getLogger(KafkaGlobalErrorHandler.class);

    @Override
    public void handleRemaining(Exception ex,
                                List<ConsumerRecord<?, ?>> records,
                                Consumer<?, ?> consumer,
                                MessageListenerContainer container) {
        for (ConsumerRecord<?, ?> record : records) {
            log.error("Ошибка обработки Kafka-сообщения. Топик: {}, Раздел: {}, Смещение: {}, Ключ: {}, Значение: {}, Ошибка: {}",
                    record.topic(), record.partition(), record.offset(), record.key(), record.value(), ex.getMessage(), ex);
        }

        super.handleRemaining(ex, records, consumer, container);
    }
}

