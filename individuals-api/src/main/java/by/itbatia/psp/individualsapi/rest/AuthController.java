package by.itbatia.psp.individualsapi.rest;

import by.itbatia.individualsapi.dto.ErrorResponse;
import by.itbatia.individualsapi.dto.TokenResponse;
import by.itbatia.individualsapi.dto.UserRegistrationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author Batsian_SV
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Operation(
        method = "POST",
        summary = "User registration",
        description = """
            **Description:**
            - Registers a new user in the system.
//            - Поля `id`, `created_at`, `updated_at` генерируются автоматически.
//            - Статус активности (`active`) по умолчанию — `false`.
//            - Статус сущности в БД (`status`) по умолчанию — `IN_USE`.
//            
//            **Требуемые права:** Только аутентифицированный запрос от `BPC_UI`.
//            
//            **Особенности:**
//            - Обязательные поля: `name`, `system_name`.
//            - Имя и системное имя должны быть уникальными.
//            - Поле `nspk_id` имеет строгое наполнение в 5 цифр.
//            - Поле `acq_bin` имеет строгое наполнение в 6 цифр.
//            - Поле `settlement_id` имеет ограничение 2-3 цифры.
            """,
        tags = {"UserRegistrationRequest"},
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "New user details",
            required = true,
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = UserRegistrationRequest.class),
                examples = @ExampleObject(
                    name = "Example of creating a user",
                    value = """
                        {
                          "email": "example@qmail.com",
                          "password": "password",
                          "confirm_password": "password"
                        }
                        """))),
        security = {
            @SecurityRequirement(name = "oauth2")
        }
    )
    @ApiResponses( {
        @ApiResponse(
            responseCode = "201",
            description = "Successful registration",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = TokenResponse.class),
                examples = @ExampleObject(
                    name = "Successful response",
                    value = """
                        {
                            "access_token": "",
                            "refresh_token": "",
                            "expires_in": 0,
                            "token_type": ""
                        }
                        """,
                    summary = "Example of a response"))),
        @ApiResponse(
            responseCode = "400",
            description = "Validation error",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    name = "Error response",
                    value = """
                        {
                          "error": ""
                          "status": 400
                        }
                        """,
                    summary = "Example of a response"))),
        @ApiResponse(
            responseCode = "409",
            description = "User already exists",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    name = "Error response",
                    value = """
                        {
                          "error": ""
                          "status": 409
                        }
                        """,
                    summary = "Example of a response"))),
    })
    @PostMapping("/registration")
    public  Mono<@NonNull ResponseEntity<@NonNull Object>> userRegistration(@RequestBody UserRegistrationRequest userRegistrationRequest) {
//        return transactionService.create(TranType.TOPUP, merchantUserId, dto).flatMap(this::buildResponse);
        return Mono.empty();
    }
}
