package com.kubancevvladislav.domain;

import com.kubancevvladislav.entities.AccountEventEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;

@Data
@AllArgsConstructor
public class AccountEvent {
    private BigInteger accountId;
    private Object payload;

    public AccountEventEntity toAccountEventEntity() {
        AccountEventEntity accountEventEntity = new AccountEventEntity();
        accountEventEntity.setAccountId(accountId);
        accountEventEntity.setPayload(payload);
        return accountEventEntity;
    }

    public static AccountEvent fromAccountEventEntity(AccountEventEntity accountEventEntity) {
        return new AccountEvent(
                accountEventEntity.getAccountId(),
                accountEventEntity.getPayload()
        );
    }
}