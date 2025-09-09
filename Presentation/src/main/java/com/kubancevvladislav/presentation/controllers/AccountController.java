package com.kubancevvladislav.presentation.controllers;

import com.kubancevvladislav.domain.Account;
import com.kubancevvladislav.presentation.controllers.dto.CheckAccountDTO;
import com.kubancevvladislav.presentation.controllers.dto.OperationResponseDTO;
import com.kubancevvladislav.services.AccountService;
import com.kubancevvladislav.services.result.types.AccountDepositResultType;
import com.kubancevvladislav.services.result.types.AccountTransferResultType;
import com.kubancevvladislav.services.result.types.AccountWithdrawResultType;
import com.kubancevvladislav.services.result.types.CreateAccountResultType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/accounts")
@AllArgsConstructor
@Tag(name = "Аккаунты", description = "Операции с банковскими счетами")
public class AccountController {
    AccountService accountService;

    @PostMapping()
    @Operation(
            summary = "Создать счёт",
            description = "Создание нового счёта для указанного пользователя"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", 
                    description = "Счёт успешно создан",
                    content = @Content(
                            schema = @Schema(
                                    implementation = OperationResponseDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Пользователь не существует",
                    content = @Content(
                            schema = @Schema(
                                    implementation = OperationResponseDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Неизвестная ошибка",
                    content = @Content(
                            schema = @Schema(
                                    implementation = OperationResponseDTO.class
                            )
                    )
            )
    })
    public ResponseEntity<OperationResponseDTO> createAccount(@RequestParam String userLogin) {
        var result = accountService.createAccount(userLogin);

        return switch (result) {
            case CreateAccountResultType.Success success ->
                    ResponseEntity.ok(
                            OperationResponseDTO.operationSuccess(
                                    "Счёт успешно создан."
                            )
                    );
            case CreateAccountResultType.UserDoesNotExist userDoesNotExist ->
                    ResponseEntity.badRequest().body(
                            OperationResponseDTO.operationFailed(
                                    "Пользователь не существует."
                            )
                    );
            case null, default ->
                    ResponseEntity.status(500).body(
                            OperationResponseDTO.operationFailed(
                                    "Неизвестная ошибка при создании счёта."
                            )
                    );
        };
    }

    @GetMapping()
    @Operation(summary = "Получить список всех счетов")
    @ApiResponse(
            responseCode = "200", 
            description = "Список счетов успешно получен",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(
                            schema = @Schema(
                                    implementation = CheckAccountDTO.class
                            )
                    )
            )
    )
    public ResponseEntity<List<CheckAccountDTO>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();

        List<CheckAccountDTO> accountDTOList = accounts.stream()
                .map(account -> new CheckAccountDTO(
                        account.getId(),
                        account.getUserLogin(),
                        account.getBalance()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(accountDTOList);
    }

    @GetMapping("/{accountId}")
    @Operation(summary = "Получить счёт по ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Счёт найден",
                    content = @Content(
                            schema = @Schema(
                                    implementation = CheckAccountDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Счёт не найден",
                    content = @Content(
                            schema = @Schema(
                                    implementation = OperationResponseDTO.class
                            )
                    )
            )
    })
    public ResponseEntity<CheckAccountDTO> checkAccount(@PathVariable BigInteger accountId) {
        Account account = accountService.getAccountById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Счёт не найден"));

        return ResponseEntity.ok(
                new CheckAccountDTO(
                    account.getId(),
                    account.getUserLogin(),
                    account.getBalance()
                )
        );
    }

    @PostMapping("/{id}/deposit")
    @Operation(
            summary = "Пополнение счёта",
            description = "Позволяет пополнить счёт на указанную сумму"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", 
                    description = "Счёт успешно пополнен",
                    content = @Content(
                            schema = @Schema(
                                    implementation = OperationResponseDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Счёт не найден или сумма отрицательная",
                    content = @Content(
                            schema = @Schema(
                                    implementation = OperationResponseDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500", 
                    description = "Ошибка при пополнении счёта",
                    content = @Content(
                            schema = @Schema(
                                    implementation = OperationResponseDTO.class
                            )
                    )
            )
    })
    public ResponseEntity<OperationResponseDTO> deposit(@PathVariable("id") BigInteger accountId, @RequestBody BigDecimal amount) {
        var result = accountService.deposit(accountId, amount);

        return switch (result) {
            case AccountDepositResultType.Success success ->
                    ResponseEntity.ok(
                            OperationResponseDTO.operationSuccess(
                                    "Счёт успешно пополнен."
                            )
                    );
            case AccountDepositResultType.AccountDoesNotExists accError ->
                    ResponseEntity.badRequest().body(
                            OperationResponseDTO.operationFailed(
                                    "Счёт с ID " + accError.getId() + " не найден."
                            )
                    );
            case AccountDepositResultType.NegativeBalance negativeBalance ->
                    ResponseEntity.badRequest().body(
                            OperationResponseDTO.operationFailed(
                                    "Сумма депозита не может быть отрицательной."
                            )
                    );
            case null, default ->
                    ResponseEntity.status(500).body(
                            OperationResponseDTO.operationFailed(
                                    "Ошибка при пополнении счёта."
                            )
                    );
        };
    }

    @PostMapping("/{id}/withdraw")
    @Operation(
            summary = "Снятие средств",
            description = "Позволяет снять указанную сумму со счёта"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", 
                    description = "Снятие выполнено успешно",
                    content = @Content(
                            schema = @Schema(
                                    implementation = OperationResponseDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", 
                    description = "Недостаточно средств, счёт не найден или сумма отрицательная",
                    content = @Content(
                            schema = @Schema(
                                    implementation = OperationResponseDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Ошибка при снятии средств",
                    content = @Content(
                            schema = @Schema(
                                    implementation = OperationResponseDTO.class
                            )
                    )
            )
    })
    public ResponseEntity<OperationResponseDTO> withdraw(@PathVariable("id") BigInteger accountId, @RequestBody BigDecimal amount) {
        AccountWithdrawResultType result = accountService.withdraw(accountId, amount);
        return switch (result) {
            case AccountWithdrawResultType.Success success ->
                    ResponseEntity.ok(
                            OperationResponseDTO.operationSuccess(
                                    "Снятие выполнено успешно."
                            )
                    );
            case AccountWithdrawResultType.NegativeBalance negativeBalance ->
                    ResponseEntity.badRequest().body(
                            OperationResponseDTO.operationFailed(
                                    "Сумма снятия не может быть отрицательной."
                            )
                    );
            case AccountWithdrawResultType.AccountDoesNotExists accountError ->
                    ResponseEntity.badRequest().body(
                            OperationResponseDTO.operationFailed(
                                    "Счёт с ID " + accountError.getId() + " не существует."
                            )
                    );
            case AccountWithdrawResultType.InsufficientFunds fundsError ->
                    ResponseEntity.badRequest().body(
                            OperationResponseDTO.operationFailed(
                                    "Недостаточно средств. Баланс: "
                                            + fundsError.getAccountFunds()
                                            + ", запрошено: "
                                            + fundsError.getRequestedFunds()
                            )
                    );
            case null, default ->
                    ResponseEntity.status(500).body(
                            OperationResponseDTO.operationFailed(
                                    "Неизвестная ошибка при снятии средств."
                            )
                    );
        };
    }

    @PostMapping("/transfer/{fromAccountId}/{toAccountId}")
    @Operation(
            summary = "Перевод средств между счетами",
            description = "Позволяет перевести средства с одного счёта на другой"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Перевод выполнен успешно",
                    content = @Content(
                            schema = @Schema(
                                    implementation = OperationResponseDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ошибка в запросе: неверные ID или недостаточно средств",
                    content = @Content(
                            schema = @Schema(
                                    implementation = OperationResponseDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Серверная ошибка при переводе",
                    content = @Content(
                            schema = @Schema(
                                    implementation = OperationResponseDTO.class
                            )
                    )
            )
    })
    public ResponseEntity<OperationResponseDTO> transfer(
            @PathVariable BigInteger fromAccountId,
            @PathVariable BigInteger toAccountId,
            @RequestBody BigDecimal amount) {
        var result = accountService.transfer(fromAccountId, toAccountId, amount);

        return switch (result) {
            case AccountTransferResultType.Success success ->
                    ResponseEntity.ok(
                            OperationResponseDTO.operationSuccess(
                                    "Перевод успешно выполнен."
                            )
                    );
            case AccountTransferResultType.AccountDoesNotExists err ->
                    ResponseEntity.badRequest().body(
                            OperationResponseDTO.operationFailed(
                                    "Счёт с ID " + err.getId() + " не найден."
                            )
                    );
            case AccountTransferResultType.NegativeBalance negativeBalance ->
                    ResponseEntity.badRequest().body(
                            OperationResponseDTO.operationFailed(
                                    "Сумма перевода не может быть отрицательной."
                            )
                    );
            case AccountTransferResultType.InsufficientFunds err ->
                    ResponseEntity.badRequest().body(
                            OperationResponseDTO.operationFailed(
                                    "Недостаточно средств: баланс "
                                            + err.getAccountFunds()
                                            + ", запрошено "
                                            + err.getRequestedFunds()
                            )
                    );
            case AccountTransferResultType.CanNotFindAccountOwner err ->
                    ResponseEntity.badRequest().body(
                            OperationResponseDTO.operationFailed(
                                    "Владелец счёта "
                                            + err.getAccountId()
                                            + " не найден."
                            )
                    );
            case AccountTransferResultType.Failed failed ->
                    ResponseEntity.status(500).body(
                            OperationResponseDTO.operationFailed(
                                    "Перевод завершился неудачей."
                            )
                    );
            case null, default ->
                    ResponseEntity.status(500).body(
                            OperationResponseDTO.operationFailed(
                                    "Ошибка при переводе средств."
                            )
                    );
        };
    }
}
