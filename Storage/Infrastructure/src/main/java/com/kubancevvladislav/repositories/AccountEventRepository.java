package com.kubancevvladislav.repositories;

import com.kubancevvladislav.entities.AccountEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface AccountEventRepository extends JpaRepository<AccountEventEntity, Long> {
    List<AccountEventEntity> findAccountEventEntitiesByAccountId(BigInteger accountId);
}
