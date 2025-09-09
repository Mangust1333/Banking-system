package com.kubancevvladislav.repositories;

import com.kubancevvladislav.entities.ClientEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientEventRepository extends JpaRepository<ClientEventEntity, Long> {
    List<ClientEventEntity> findClientEventEntitiesByClientLogin(String clientLogin);
}

