package com.kubancevvladislav.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kubancevvladislav.domain.GetRequest;
import com.kubancevvladislav.domain.PostRequest;
import com.kubancevvladislav.domain.RegistrationDTO;
import com.kubancevvladislav.domain.Role;
import com.kubancevvladislav.domain.dto.*;
import com.kubancevvladislav.exceptions.ExternalApiException;
import com.kubancevvladislav.exceptions.UserAlreadyExistsException;
import com.kubancevvladislav.services.JwtService;
import com.kubancevvladislav.services.UserService;
import com.kubancevvladislav.services.result.types.UserServiceRegisterResultType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ClientApiHttpImpl implements ClientApi {
    private final ExternalApiClientInterface externalApiClient;
    private final UserService userService;
    private final String externalApiBaseUrl;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public ClientApiHttpImpl(
            UserService userService,
            ExternalApiClientInterface clientApi,
            @Value("${url-of-api}") String externalApiBaseUrl,
            JwtService jwtService,
            AuthenticationManager authenticationManager
    ) {
        this.userService = userService;
        this.externalApiClient = clientApi;
        this.externalApiBaseUrl = externalApiBaseUrl;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public String createClientAccount(CreateClientRequest request) {
        var result = userService.register(
                new RegistrationDTO(request.login(), request.password(), Role.CLIENT)
        );
        if (result instanceof UserServiceRegisterResultType.UserWithLoginAlreadyExists fail) {
            throw new UserAlreadyExistsException(fail.getLogin());
        }

        String json = buildUserJson(request);
        PostRequest sendRequest = PostRequest.builder()
                .resource(externalApiBaseUrl + "/users")
                .payload(json)
                .build();

        return externalApiClient.post(sendRequest, String.class)
                .orElseThrow(() -> new ExternalApiException("Ошибка при создании пользователя"));
    }

    @Override
    public String createAdminAccount(CreateAdminRequest request) {
        UserServiceRegisterResultType result = userService.register(
                new RegistrationDTO(request.name(), request.password(), Role.ADMIN)
        );

        if (result instanceof UserServiceRegisterResultType.UserWithLoginAlreadyExists fail) {
            throw new UserAlreadyExistsException(fail.getLogin());
        }

        return "success";
    }

    @Override
    public List<UserDTO> filterUsers(UserFilerByHairColorAndGenderRequest request) {
        String url = String.format("%s/users/filter?hairColor=%s&gender=%s",
                externalApiBaseUrl, request.hairColor(), request.gender());

        var getRequest = GetRequest.builder()
                .resource(url)
                .build();

        return externalApiClient.get(getRequest, UserDTO[].class)
                .map(List::of)
                .orElseThrow(() -> new ExternalApiException("Ошибка при фильтрации пользователей"));
    }

    @Override
    public UserDTO getUserById(Long userId) {
        var getRequest = GetRequest.builder()
                .resource(String.format("%s/users/id/%d", externalApiBaseUrl, userId))
                .build();

        return externalApiClient.get(getRequest, UserDTO.class)
                .orElseThrow(() -> new ExternalApiException("Пользователь с ID " + userId + " не найден"));
    }

    @Override
    public List<AccountDTO> getAllAccounts() {
        var request = GetRequest.builder()
                .resource(externalApiBaseUrl + "/accounts")
                .build();

        return externalApiClient.get(request, AccountDTO[].class)
                .map(List::of)
                .orElseThrow(() -> new ExternalApiException("Не удалось получить список счетов"));
    }

    @Override
    public List<AccountDTO> getUserAccountsByLogin(String userLogin) {
        var getRequest = GetRequest.builder()
                .resource(externalApiBaseUrl + "/accounts")
                .build();

        AccountDTO[] allAccounts = externalApiClient.get(getRequest, AccountDTO[].class)
                .orElseThrow(() -> new ExternalApiException("Ошибка при получении списка счетов"));

        return Stream.of(allAccounts)
                .filter(account -> userLogin.equals(account.userLogin()))
                .collect(Collectors.toList());
    }

    @Override
    public AccountDTO getAccountById(Long accountId) {
        var getRequest = GetRequest.builder()
                .resource(String.format("%s/accounts/%d", externalApiBaseUrl, accountId))
                .build();

        return externalApiClient.get(getRequest, AccountDTO.class)
                .orElseThrow(() -> new ExternalApiException("Счёт с ID " + accountId + " не найден"));
    }

    @Override
    public LoginResponse authenticate(LoginRequest loginRequest) {
        var authToken = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        authenticationManager.authenticate(authToken);

        UserDetails user = userService.loadUserByUsername(loginRequest.getUsername());
        String token = jwtService.generateToken(user.getUsername(), user.getAuthorities());

        return new LoginResponse(token);
    }

    @Override
    public UserDTO getUserInfoByLogin(String login) {
        String url = String.format("%s/users/%s", externalApiBaseUrl, login);

        GetRequest request = GetRequest.builder()
                .resource(url)
                .build();

        return externalApiClient.get(request, UserDTO.class)
                .orElseThrow(() -> new ExternalApiException("Ошибка при получении информации о пользователе"));
    }

    @Override
    public List<AccountDTO> getUserAccountsByLogin(Authentication auth) {
        String url = String.format("%s/accounts", externalApiBaseUrl);

        GetRequest getRequest = GetRequest.builder()
                .resource(url)
                .build();

        AccountDTO[] allAccounts = externalApiClient.get(getRequest, AccountDTO[].class)
                .orElseThrow(() -> new ExternalApiException("Ошибка при получении списка счетов"));

        String userLogin = auth.getName();

        return Stream.of(allAccounts)
                .filter(account -> userLogin.equals(account.userLogin()))
                .toList();
    }

    @Override
    public AccountDTO getAccountById(BigInteger accountId) {
        GetRequest request = GetRequest.builder()
                .resource(String.format("%s/accounts/%d", externalApiBaseUrl, accountId))
                .build();

        return externalApiClient.get(request, AccountDTO.class)
                .orElseThrow(() -> new ExternalApiException("Счёт с ID " + accountId + " не найден"));
    }

    @Override
    public OperationResponseDTO addFriend(String friendLogin, Authentication auth) {
        PostRequest postRequest = PostRequest.builder()
                .resource(String.format("%s/users/friend/%s/%s", externalApiBaseUrl, auth.getName(), friendLogin))
                .build();

        return externalApiClient.post(postRequest, OperationResponseDTO.class)
                .orElseThrow(() -> new ExternalApiException("Ошибка при добавлении друга"));
    }

    @Override
    public OperationResponseDTO depositToAccount(BigInteger id, BigDecimal amount) {
        PostRequest postRequest = PostRequest.builder()
                .resource(String.format("%s/accounts/%d/deposit", externalApiBaseUrl, id))
                .payload(amount)
                .build();

        return externalApiClient.post(postRequest, OperationResponseDTO.class)
                .orElseThrow(() -> new ExternalApiException("Ошибка при внесении средств"));
    }

    @Override
    public OperationResponseDTO withdrawFromAccount(BigInteger id, BigDecimal amount) {
        PostRequest postRequest = PostRequest.builder()
                .resource(String.format("%s/accounts/%d/withdraw", externalApiBaseUrl, id))
                .payload(amount)
                .build();

        return externalApiClient.post(postRequest, OperationResponseDTO.class)
                .orElseThrow(() -> new ExternalApiException("Ошибка при снятии средств"));
    }

    @Override
    public OperationResponseDTO transferMoney(BigInteger fromAccountId, BigInteger toAccountId, BigDecimal amount) {
        PostRequest postRequest = PostRequest.builder()
                .resource(String.format("%s/accounts/transfer/%d/%d", externalApiBaseUrl, fromAccountId, toAccountId))
                .payload(amount)
                .build();

        return externalApiClient.post(postRequest, OperationResponseDTO.class)
                .orElseThrow(() -> new ExternalApiException("Ошибка при переводе средств"));
    }

    public List<Object> getTransactions(Long accountId) {
        String[] types = {"deposit", "withdrawal", "transfer"};
        List<Object> allTransactions = new ArrayList<>();

        for (String type : types) {
            String url = String.format("%s/accounts/%d?type=%s", externalApiBaseUrl, accountId, type.toUpperCase());

            GetRequest request = GetRequest.builder()
                    .resource(url)
                    .build();

            Optional<Object> transactionResponse = externalApiClient.get(request, Object.class);
            transactionResponse.ifPresent(allTransactions::add);
        }

        return allTransactions;
    }

    private String buildUserJson(CreateClientRequest request) {
        return String.format("""
                            {
                              "login": "%s",
                              "name": "%s",
                              "age": %d,
                              "gender": "%s",
                              "hairColor": "%s",
                              "friends": %s
                            }
                        """,
                request.login(),
                request.name(),
                request.age(),
                request.gender().name(),
                request.hairColor().name(),
                new ObjectMapper().valueToTree(request.friends()).toString()
        );
    }
}
