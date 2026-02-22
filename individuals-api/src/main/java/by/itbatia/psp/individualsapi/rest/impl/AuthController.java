package by.itbatia.psp.individualsapi.rest.impl;

import by.itbatia.individualsapi.dto.TokenRefreshRequest;
import by.itbatia.individualsapi.dto.TokenResponse;
import by.itbatia.individualsapi.dto.UserRegistrationRequest;
import by.itbatia.psp.individualsapi.rest.AuthApi;
import by.itbatia.psp.individualsapi.service.TokenService;
import by.itbatia.psp.individualsapi.service.UserService;
import by.itbatia.psp.individualsapi.validator.RestValidator;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author Batsian_SV
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController implements AuthApi {

    private final UserService userService;
    private final TokenService tokenService;

    public Mono<@NonNull ResponseEntity<@NonNull TokenResponse>> registerUser(UserRegistrationRequest request) {
        RestValidator.validate(request);
        return userService.register(request)
            .map(tokenResponse -> ResponseEntity.status(HttpStatus.CREATED).body(tokenResponse));
    }

    public Mono<@NonNull ResponseEntity<@NonNull TokenResponse>> refreshToken(TokenRefreshRequest request) {
        RestValidator.validate(request);
        return tokenService.refresh(request.getRefreshToken())
            .map(ResponseEntity::ok);
    }
}
