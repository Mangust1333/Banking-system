package com.kubancevvladislav.presentation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Основное приложение, которое запускает графический пользовательский интерфейс (GUI) для банковской системы.
 * Используется библиотека Lanterna для создания консольных GUI.
 */
@SpringBootApplication(scanBasePackages = "com.kubancevvladislav")
@EnableJpaRepositories(basePackages = "com.kubancevvladislav.repositories")
@EntityScan(basePackages = "com.kubancevvladislav.entities")
public class PresentationApp {
    /**
     * Главный метод приложения. Создает и запускает графический интерфейс для взаимодействия с пользователем.
     * @param args аргументы командной строки (не используются в данном приложении)
     */
    public static void main(String[] args) {
        SpringApplication.run(PresentationApp.class, args);
    }
}
