package com.kubancevvladislav.presentation.controllers.dto;

import com.kubancevvladislav.presentation.controllers.dto.transactionsDTO.TransactionDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "DTO для возврата списка транзакций")
public record CheckTransactionsDTO (List<TransactionDTO> transactions) {}
