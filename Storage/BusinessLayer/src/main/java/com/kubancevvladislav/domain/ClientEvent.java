package com.kubancevvladislav.domain;

import com.kubancevvladislav.entities.ClientEventEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClientEvent {
    private String clientLogin;
    private Object payload;

    public ClientEventEntity toClientEventEntity() {
        ClientEventEntity clientEventEntity = new ClientEventEntity();
        clientEventEntity.setClientLogin(clientLogin);
        clientEventEntity.setPayload(payload);
        return clientEventEntity;
    }

    public static ClientEvent fromClientEventEntity(ClientEventEntity clientEventEntity) {
        return new ClientEvent(
                clientEventEntity.getClientLogin(),
                clientEventEntity.getPayload()
        );
    }
}