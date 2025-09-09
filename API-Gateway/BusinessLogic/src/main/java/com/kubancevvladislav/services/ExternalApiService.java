package com.kubancevvladislav.services;

import com.kubancevvladislav.clients.ClientApi;
import com.kubancevvladislav.domain.dto.*;
import lombok.AllArgsConstructor;
import org.apache.catalina.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ExternalApiService{
    private final ClientApi clientApi;

    public String createClientAccount(CreateClientRequest request) {
        return clientApi.createClientAccount(request);
    }

    public String createAdminAccount(CreateAdminRequest request) {
        return clientApi.createAdminAccount(request);
    }

    public List<UserDTO> filterUsers(UserFilerByHairColorAndGenderRequest request) {
        return clientApi.filterUsers(request);
    }

    public UserDTO getUserById(Long userId) {
        return clientApi.getUserById(userId);
    }

    public List<AccountDTO> getAllAccounts() {
        return clientApi.getAllAccounts();
    }

    public List<AccountDTO> getUserAccountsByLogin(String userLogin) {
        return clientApi.getUserAccountsByLogin(userLogin);
    }

    public Map<String, Object> getAccountDetails(Long accountId) {
        AccountDTO account = clientApi.getAccountById(accountId);
        List<Object> allTransactions = clientApi.getTransactions(accountId);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("account", account);
        responseBody.put("transactions", allTransactions);

        return responseBody;
    }

    public LoginResponse authenticate(LoginRequest loginRequest) {
        return clientApi.authenticate(loginRequest);
    }

    public UserDTO getMyInfo(Authentication auth) {
        return clientApi.getUserInfoByLogin(auth.getName());
    }

    public List<AccountDTO> getUserAccountsByLogin(Authentication auth) {
        return  clientApi.getUserAccountsByLogin(auth);
    }

    public AccountDTO getMyAccountById(BigInteger accountId, Authentication auth) throws AccountNotFoundException {
        doesAccountBelongToUser(accountId, auth.getName());
        return clientApi.getAccountById(accountId);
    }

    public OperationResponseDTO addFriend(String friendLogin, Authentication auth) {
        return clientApi.addFriend(friendLogin, auth);
    }

    public List<FriendInfoDTO> checkMyFriends(Authentication auth) {
        UserDTO user = getMyInfo(auth);

        if (user == null || user.friends() == null || user.friends().isEmpty()) {
            return List.of();
        }

        List<FriendInfoDTO> friendInfos = new ArrayList<>();

        for (String friendLogin : user.friends()) {
            UserDTO friend = clientApi.getUserInfoByLogin(friendLogin);
            if (friend != null) {
                List<AccountDTO> accounts = clientApi.getUserAccountsByLogin(friendLogin);
                List<BigInteger> accountIds = accounts.stream()
                        .map(AccountDTO::id)
                        .toList();
                friendInfos.add(new FriendInfoDTO(friend.name(), accountIds));
            }
        }

        return friendInfos;
    }

    public OperationResponseDTO depositToAccount(BigInteger id, BigDecimal amount, Authentication auth) throws AccountNotFoundException {
        doesAccountBelongToUser(id, auth.getName());
        return clientApi.depositToAccount(id, amount);
    }

    public OperationResponseDTO withdrawFromAccount(BigInteger id, BigDecimal amount, Authentication auth) throws AccountNotFoundException {
        doesAccountBelongToUser(id, auth.getName());
        return clientApi.withdrawFromAccount(id, amount);
    }

    public OperationResponseDTO transferMoney(BigInteger fromAccountId, BigInteger toAccountId, BigDecimal amount, Authentication auth) throws AccountNotFoundException {
        doesAccountBelongToUser(fromAccountId, auth.getName());
        return clientApi.transferMoney(fromAccountId, toAccountId, amount);
    }

    private void doesAccountBelongToUser(BigInteger accountId, String username) throws AccountNotFoundException {
        AccountDTO account = clientApi.getAccountById(accountId);
        if (!username.equals(account.userLogin())) {
            throw new AccountNotFoundException("Вы не имеете доступа к этому счёту");
        }
    }
}
