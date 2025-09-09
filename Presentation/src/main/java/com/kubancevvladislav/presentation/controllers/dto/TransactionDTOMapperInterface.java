package com.kubancevvladislav.presentation.controllers.dto;

import com.kubancevvladislav.domain.transactions.DepositTransaction;
import com.kubancevvladislav.domain.transactions.Transaction;
import com.kubancevvladislav.domain.transactions.TransferTransaction;
import com.kubancevvladislav.domain.transactions.WithdrawTransaction;
import com.kubancevvladislav.presentation.controllers.dto.transactionsDTO.DepositTransactionDTO;
import com.kubancevvladislav.presentation.controllers.dto.transactionsDTO.TransactionDTO;
import com.kubancevvladislav.presentation.controllers.dto.transactionsDTO.TransferTransactionDTO;
import com.kubancevvladislav.presentation.controllers.dto.transactionsDTO.WithdrawTransactionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionDTOMapperInterface {
    WithdrawTransactionDTO toDto(WithdrawTransaction transaction);
    DepositTransactionDTO toDto(DepositTransaction transaction);
    TransferTransactionDTO toDto(TransferTransaction transaction);
    
    default TransactionDTO toDto(Transaction transaction) {
        return switch (transaction) {
            case DepositTransaction depositTransaction -> toDto(depositTransaction);
            case TransferTransaction transferTransaction -> toDto(transferTransaction);
            case WithdrawTransaction withdrawTransaction -> toDto(withdrawTransaction);
            case null, default -> throw new IllegalArgumentException("Unknown  type: " + transaction.getClass());
        };
    }
}
