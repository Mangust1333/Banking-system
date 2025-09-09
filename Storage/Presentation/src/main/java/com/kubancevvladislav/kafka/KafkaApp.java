package com.kubancevvladislav.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.kubancevvladislav")
@EnableJpaRepositories(basePackages = "com.kubancevvladislav.repositories")
@EntityScan(basePackages = "com.kubancevvladislav.entities")
public class KafkaApp {
    public static void main(String[] args) {
        SpringApplication.run(KafkaApp.class, args);
    }
}


