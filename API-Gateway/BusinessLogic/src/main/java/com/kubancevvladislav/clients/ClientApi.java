package com.kubancevvladislav.clients;

import com.kubancevvladislav.domain.dto.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Component
public interface ClientApi {
    String createClientAccount(CreateClientRequest request);

    String createAdminAccount(CreateAdminRequest request);

    List<UserDTO> filterUsers(UserFilerByHairColorAndGenderRequest request);

    UserDTO getUserById(Long userId);

    List<AccountDTO> getAllAccounts();

    List<AccountDTO> getUserAccountsByLogin(String userLogin);

    AccountDTO getAccountById(Long accountId);

    LoginResponse authenticate(LoginRequest loginRequest);

    UserDTO getUserInfoByLogin(String login);

    List<AccountDTO> getUserAccountsByLogin(Authentication auth);

    AccountDTO getAccountById(BigInteger accountId);

    OperationResponseDTO addFriend(String friendLogin, Authentication auth);

    OperationResponseDTO depositToAccount(BigInteger id, BigDecimal amount);

    OperationResponseDTO withdrawFromAccount(BigInteger id, BigDecimal amount);

    OperationResponseDTO transferMoney(BigInteger fromAccountId, BigInteger toAccountId, BigDecimal amount);

    List<Object> getTransactions(Long accountId);
}
