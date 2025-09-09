package com.kubancevvladislav.services.mapper;

import com.kubancevvladislav.domain.Account;
import com.kubancevvladislav.entities.AccountEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapperInterface {
    Account toDomain(AccountEntity accountEntity);

    AccountEntity toEntity(Account account);
}