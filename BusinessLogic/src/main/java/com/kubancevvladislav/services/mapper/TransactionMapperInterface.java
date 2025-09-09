package com.kubancevvladislav.services.mapper;


import com.kubancevvladislav.domain.transactions.DepositTransaction;
import com.kubancevvladislav.domain.transactions.Transaction;
import com.kubancevvladislav.domain.transactions.TransferTransaction;
import com.kubancevvladislav.domain.transactions.WithdrawTransaction;
import com.kubancevvladislav.entities.transactions.DepositTransactionEntity;
import com.kubancevvladislav.entities.transactions.TransactionEntity;
import com.kubancevvladislav.entities.transactions.TransferTransactionEntity;
import com.kubancevvladislav.entities.transactions.WithdrawTransactionEntity;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = false))
public interface TransactionMapperInterface {
    DepositTransaction toDomain(DepositTransactionEntity transactionEntity);
    DepositTransactionEntity toEntity(DepositTransaction transaction);

    TransferTransaction toDomain(TransferTransactionEntity transactionEntity);
    TransferTransactionEntity toEntity(TransferTransaction transaction);

    WithdrawTransaction toDomain(WithdrawTransactionEntity transactionEntity);
    WithdrawTransactionEntity toEntity(WithdrawTransaction transaction);

    default Transaction toDomain(TransactionEntity transactionEntity) {
        return switch (transactionEntity) {
            case DepositTransactionEntity depositTransactionEntity -> toDomain(depositTransactionEntity);
            case TransferTransactionEntity transferTransactionEntity -> toDomain(transferTransactionEntity);
            case WithdrawTransactionEntity withdrawTransactionEntity -> toDomain(withdrawTransactionEntity);
            case null, default ->
                    throw new IllegalArgumentException("Unknown entity type: " + transactionEntity.getClass());
        };
    }

    default TransactionEntity toEntity(Transaction transaction) {
        return switch (transaction) {
            case DepositTransaction depositTransaction -> toEntity(depositTransaction);
            case TransferTransaction transferTransaction -> toEntity(transferTransaction);
            case WithdrawTransaction withdrawTransaction -> toEntity(withdrawTransaction);
            case null, default ->
                    throw new IllegalArgumentException("Unknown entity type: " + transaction.getClass());
        };
    }

    default Class<? extends TransactionEntity> toEntity(Class<? extends Transaction> transactionType) {
        if (transactionType == DepositTransaction.class) {
            return DepositTransactionEntity.class;
        } else if (transactionType == WithdrawTransaction.class) {
            return WithdrawTransactionEntity.class;
        } else if (transactionType == TransferTransaction.class) {
            return TransferTransactionEntity.class;
        }
        throw new IllegalArgumentException("Unknown transaction type: " + transactionType);
    }
}
