package com.kubancevvladislav.presentation.controllers;


import com.kubancevvladislav.domain.TransactionType;
import com.kubancevvladislav.domain.transactions.Transaction;
import com.kubancevvladislav.presentation.controllers.dto.CheckTransactionsDTO;
import com.kubancevvladislav.presentation.controllers.dto.OperationResponseDTO;
import com.kubancevvladislav.presentation.controllers.dto.TransactionTypeDTO;
import com.kubancevvladislav.presentation.controllers.dto.transactionsDTO.TransactionDTO;
import com.kubancevvladislav.presentation.controllers.dto.TransactionDTOMapperInterface;
import com.kubancevvladislav.services.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/transactions")
@Tag(name = "Транзакции", description = "API для работы с транзакциями")
@AllArgsConstructor
public class TransactionController {
    TransactionService transactionService;

    @Qualifier("transactionDTOMapperInterfaceImpl")
    TransactionDTOMapperInterface transactionMapper;

    @GetMapping("/check")
    @Operation(
            summary = "Получить все транзакции",
            description = "Возвращает все транзакции в системе."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Транзакции успешно получены",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = CheckTransactionsDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Ошибка сервера",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = OperationResponseDTO.class
                            )
                    )
            )
    })
    public ResponseEntity<CheckTransactionsDTO> checkAllTransaction() {
        List<Transaction> transaction = transactionService.getAllTransactions();
        CheckTransactionsDTO transactionsDTO = new CheckTransactionsDTO(new ArrayList<TransactionDTO>());
        for (Transaction transaction_index : transaction) {
            transactionsDTO.transactions().add(transactionMapper.toDto(transaction_index));
        }
        return ResponseEntity.ok().body(transactionsDTO);
    }

    @GetMapping("/account/{id}")
    @Operation(
            summary = "Получить транзакции по аккаунту и типу \n(\"deposit\" \n" +
                    "\"withdraw\"\n" +
                    "\"transfer\")",
            description = "Возвращает транзакции для указанного аккаунта и типа."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Транзакции успешно получены",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(
                                            implementation = TransactionDTO.class
                                    )
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ошибка: неизвестный тип транзакции",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = OperationResponseDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Неизвестная ошибка",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = OperationResponseDTO.class
                            )
                    )
            )
    })
    public ResponseEntity<List<TransactionDTO>> getByAccountAndType(
            @PathVariable BigInteger id,
            @RequestParam TransactionTypeDTO type
    ) {

        List<Transaction> transactions = transactionService.getAccountTransactionsByType(
                id,
                TransactionType.valueOf(type.name())
        );
        List<TransactionDTO> result = transactions.stream()
                .map(transactionMapper::toDto)
                .toList();

        return ResponseEntity.ok(result);
    }
}
