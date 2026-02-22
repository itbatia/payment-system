package by.itbatia.psp.individualsapi.rest;

import by.itbatia.individualsapi.dto.ErrorResponse;
import by.itbatia.individualsapi.dto.TokenRefreshRequest;
import by.itbatia.individualsapi.dto.TokenResponse;
import by.itbatia.individualsapi.dto.UserLoginRequest;
import by.itbatia.individualsapi.dto.UserRegistrationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.jspecify.annotations.NonNull;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

/**
 * @author Batsian_SV
 */
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "API for user registration and authentication")
public interface AuthApi {

    @Operation(
        method = "POST",
        summary = "User registration",
        description = """
            Registers a new user and returns authentication tokens.
            
            **Validation rules:**
            - Email must be in valid format
            - Password must meet security requirements
            - Password and confirmation must match exactly
            """,
        tags = {"Authentication"},
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "User registration details",
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
                        """,
                    description = """
                        **Field requirements:**
                        - `email`: valid email format (e.g., user@example.com)
                        - `password`: at least 8 characters, including uppercase, lowercase, and digits
                        - `confirm_password`: must exactly match `password`
                        """
                )
            )
        )
    )
    @ApiResponses( {
        @ApiResponse(
            responseCode = "201",
            description = "User created successfully. Returns tokens.",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = TokenResponse.class),
                examples = @ExampleObject(
                    name = "Successful response",
                    value = """
                        {
                            "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ...<truncated>",
                            "refresh_token": "eyJhbGciOiJIUzUxMiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI...<truncated>",
                            "expires_in": 300,
                            "token_type": "Bearer"
                        }
                        """,
                    summary = "Example of a response"
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Bad request: validation failed",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    name = "Error response",
                    value = """
                        {
                          "error": "Password and confirmation do not match",
                          "status": 400
                        }
                        """,
                    summary = "Example of a response"
                )
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Forbidden: server misconfiguration (contact admin).",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    name = "Error response",
                    value = """
                        {
                          "error": "You don't have permission to access this resource.",
                          "status": 403
                        }
                        """,
                    summary = "Example of a response"
                )
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Conflict: user with this email already exists.",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    name = "Error response",
                    value = """
                        {
                          "error": "User exists with same email",
                          "status": 409
                        }
                        """,
                    summary = "Example of a response"
                )
            )
        ),
    })
    @PostMapping("/registration")
    Mono<@NonNull ResponseEntity<@NonNull TokenResponse>> registerUser(@RequestBody UserRegistrationRequest request);

    @Operation(
        summary = "User login",
        description = """
            Authenticates user credentials and returns access/refresh tokens.
            
            **Important:**
            - No local database lookup — credentials are validated directly against Keycloak
            - Returns 401 if email/password combination is invalid
            - This endpoint is public (no authentication required)
            """,
        tags = {"Authentication"},
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "User login credentials",
            required = true,
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = UserLoginRequest.class),
                examples = @ExampleObject(
                    name = "Login request example",
                    value = """
                        {
                          "email": "user@example.com",
                          "password": "SecurePass123!"
                        }
                        """,
                    summary = "Valid login credentials"
                )
            )
        )
    )
    @ApiResponses( {
        @ApiResponse(
            responseCode = "200",
            description = "Authentication successful. Returns access and refresh tokens.",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = TokenResponse.class),
                examples = @ExampleObject(
                    name = "Successful login response",
                    value = """
                        {
                          "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ...<truncated>",
                          "refresh_token": "eyJhbGciOiJIUzUxMiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI...<truncated>",
                          "expires_in": 300,
                          "token_type": "Bearer"
                        }
                        """,
                    summary = "Tokens issued successfully"
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Bad request: validation failed (e.g., missing fields or invalid email format)",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    name = "Validation error",
                    value = """
                        {
                          "error": "Email must be a valid email address",
                          "status": 400
                        }
                        """,
                    summary = "Input validation failed"
                )
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized: invalid email or password",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    name = "Invalid credentials",
                    value = """
                        {
                          "error": "Invalid user credentials",
                          "status": 401
                        }
                        """,
                    summary = "Authentication failed"
                )
            )
        )
    })
    @PostMapping("/login")
    Mono<@NonNull ResponseEntity<@NonNull TokenResponse>> login(@RequestBody UserLoginRequest request);

    @Operation(
        summary = "Refresh access token",
        description = """
            Exchanges a valid refresh token for a new access token (and optionally a new refresh token).
            
            **Important:**
            - The provided refresh token must be valid and not expired
            - This endpoint is public (no authentication required)
            - Client credentials are handled internally by the service
            """,
        tags = {"Authentication"},
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Refresh token request",
            required = true,
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = TokenRefreshRequest.class),
                examples = @ExampleObject(
                    name = "Refresh token example",
                    value = """
                        {
                          "refresh_token": "eyJhbGciOiJIUzUxMiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI...<truncated>"
                        }
                        """,
                    summary = "Valid refresh token request"
                )
            )
        )
    )
    @ApiResponses( {
        @ApiResponse(
            responseCode = "200",
            description = "Access token successfully refreshed",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = TokenResponse.class),
                examples = @ExampleObject(
                    name = "Successful refresh response",
                    value = """
                        {
                          "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ...<truncated>",
                          "refresh_token": "eyJhbGciOiJIUzUxMiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI...<truncated>",
                          "expires_in": 300,
                          "token_type": "Bearer"
                        }
                        """,
                    summary = "New tokens issued"
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Bad request: missing or invalid refresh token",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    name = "Missing refresh token",
                    value = """
                        {
                          "error": "RefreshToken field is required",
                          "status": 400
                        }
                        """,
                    summary = "Validation error"
                )
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized: invalid, expired, or revoked refresh token",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    name = "Invalid refresh token",
                    value = """
                        {
                          "error": "Token is not active",
                          "status": 401
                        }
                        """,
                    summary = "Token validation failed"
                )
            )
        )
    })
    @PostMapping("/refresh-token")
    Mono<@NonNull ResponseEntity<@NonNull TokenResponse>> refreshToken(@RequestBody TokenRefreshRequest request);
}
