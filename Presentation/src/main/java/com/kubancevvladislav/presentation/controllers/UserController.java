package com.kubancevvladislav.presentation.controllers;

import com.kubancevvladislav.domain.DTO.UserDTO;
import com.kubancevvladislav.domain.Gender;
import com.kubancevvladislav.domain.HairColor;
import com.kubancevvladislav.domain.User;
import com.kubancevvladislav.presentation.controllers.dto.CheckAccountDTO;
import com.kubancevvladislav.presentation.controllers.dto.CheckUserDTO;
import com.kubancevvladislav.presentation.controllers.dto.CreateUserDTO;
import com.kubancevvladislav.presentation.controllers.dto.OperationResponseDTO;
import com.kubancevvladislav.services.UserService;
import com.kubancevvladislav.services.result.types.AddUserFriendResultType;
import com.kubancevvladislav.services.result.types.CreateUserResultType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Tag(name = "Пользователи", description = "API для управления пользователями и их друзьями")
public class UserController {

    UserService userService;

    @PostMapping()
    @Operation(
            summary = "Создание нового пользователя",
            description = "Создаёт нового пользователя в системе.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователь успешно создан",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = OperationResponseDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ошибка: пользователь с таким логином уже существует или друг не найден",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = OperationResponseDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Неизвестная ошибка при создании пользователя",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = OperationResponseDTO.class
                            )
                    )
            )
    })
    public ResponseEntity<OperationResponseDTO> createUser(@RequestBody CreateUserDTO userDTO) {
        CreateUserResultType result = userService.createUser(
                new UserDTO(
                    userDTO.login(),
                    userDTO.name(),
                    userDTO.age(),
                    userDTO.gender(),
                    userDTO.hairColor(),
                    userDTO.friends()
            )
        );

        return switch (result) {
            case CreateUserResultType.Success success ->
                    ResponseEntity.ok(
                            OperationResponseDTO.operationSuccess(
                                    "Пользователь успешно создан"
                            )
                    );
            case CreateUserResultType.FriendDoesNotExists e ->
                    ResponseEntity.badRequest().body(
                            OperationResponseDTO.operationFailed(
                                    "Друг с логином "
                                            + e.getFriendLogin()
                                            + " не существует"
                            )
                    );
            case CreateUserResultType.UserAlreadyExists userAlreadyExists ->
                    ResponseEntity.badRequest().body(
                            OperationResponseDTO.operationFailed(
                                    "Пользователь с таким логином уже существует"
                            )
                    );
            case null, default ->
                    ResponseEntity.internalServerError().body(
                            OperationResponseDTO.operationFailed(
                                    "Неизвестная ошибка при создании пользователя"
                            )
                    );
        };
    }

    @GetMapping("/{login}")
    @Operation(
            summary = "Получение пользователя по логину",
            description = "Возвращает информацию о пользователе по его логину.")
    @ApiResponses(
            value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователь найден",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = CheckUserDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ошибка: пользователь не найден",
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
    public ResponseEntity<CheckUserDTO> checkUser(@PathVariable String login) {
        User user = userService.getUserByLogin(login).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok().body(
                new CheckUserDTO(
                        user.getLogin(),
                        user.getName(),
                        user.getAge(),
                        user.getGender(),
                        user.getHairColor(),
                        userService.getUserFriendsByLogin(login)
                )
        );
    }

    @GetMapping("/id/{id}")
    @Operation(
            summary = "Получение пользователя по ID",
            description = "Возвращает информацию о пользователе по его ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователь найден",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = CheckUserDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Пользователь не найден",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = OperationResponseDTO.class
                            )
                    )
            )
    })
    public ResponseEntity<CheckUserDTO> checkUser(@PathVariable BigInteger id) {
        User user = userService.getUserById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok().body(
                new CheckUserDTO(
                        user.getLogin(),
                        user.getName(),
                        user.getAge(),
                        user.getGender(),
                        user.getHairColor(),
                        userService.getUserFriendsById(id)
                )
        );
    }

    @PostMapping("/friend/{login}/{friend}")
    @Operation(
            summary = "Добавить друга",
            description = "Добавляет другого пользователя в друзья.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователи теперь друзья",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = OperationResponseDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ошибка: пользователи уже друзья или один из пользователей не существует",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = OperationResponseDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Неизвестная ошибка при добавлении в друзья",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = OperationResponseDTO.class
                            )
                    )
            )
    })
    public ResponseEntity<OperationResponseDTO> AddFriend(@PathVariable String login, @PathVariable String friend) {
        AddUserFriendResultType result = userService.addUserFriendByLogin(login, friend);

        return switch (result) {
            case AddUserFriendResultType.Success success ->
                    ResponseEntity.ok(
                            OperationResponseDTO.operationSuccess(
                                    "Пользователи теперь друзья"
                            )
                    );
            case AddUserFriendResultType.UsersAreAlreadyFriends usersAreAlreadyFriends ->
                    ResponseEntity.badRequest().body(
                            OperationResponseDTO.operationFailed(
                                    "Пользователи уже являются друзьями"
                            )
                    );
            case AddUserFriendResultType.UserDoNotExists e ->
                    ResponseEntity.badRequest().body(
                            OperationResponseDTO.operationFailed(
                                    "Пользователь с логином "
                                            + e.getLogin()
                                            + " не существует"
                            )
                    );
            case AddUserFriendResultType.UserCanNotAddHimself userCanNotAddHimself ->
                    ResponseEntity.badRequest().body(
                            OperationResponseDTO.operationFailed(
                                    "Пользователь не может добавить самого себя в друзья"
                            )
                    );
            case null, default ->
                    ResponseEntity.internalServerError().body(
                            OperationResponseDTO.operationFailed(
                                    "Неизвестная ошибка при добавлении в друзья"
                            )
                    );
        };
    }


    @GetMapping("/friend/{id}")
    @Operation(
            summary = "Получить список друзей",
            description = "Возвращает список друзей пользователя по его ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Список друзей пользователя",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(
                                            implementation = String.class
                                    )
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Пользователь не найден",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = OperationResponseDTO.class
                            )
                    )
            )
    })
    public ResponseEntity<List<String>> getFriends(@PathVariable BigInteger id) {
        return ResponseEntity.ok(userService.getUserFriendsById(id));
    }

    /**
     * Эндпоинт для получения пользователей по фильтру (цвет волос и гендер)
     */
    @GetMapping("/filter")
    @Operation(summary = "Фильтрация пользователей", description = "Получение пользователей по фильтру: цвет волос и гендер.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Список пользователей по фильтру",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(
                                            implementation = String.class
                                    )
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ошибка фильтрации данных",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = OperationResponseDTO.class
                            )
                    )
            )
    })
    public ResponseEntity<List<CheckUserDTO>> getUsersByFilter(@RequestParam HairColor hairColor, @RequestParam Gender gender) {
        List<User> filteredUsers = userService.getUsersByHairColorAndGender(hairColor, gender);

        List<CheckUserDTO> userDTOList = filteredUsers.stream()
                .map(user -> new CheckUserDTO(
                        user.getLogin(),
                        user.getName(),
                        user.getAge(),
                        user.getGender(),
                        user.getHairColor(),
                        userService.getUserFriendsByLogin(user.getLogin())
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(userDTOList);
    }
}
