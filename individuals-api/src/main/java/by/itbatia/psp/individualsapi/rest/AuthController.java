package by.itbatia.psp.individualsapi.rest;

import by.itbatia.psp.individualsapi.api.AuthApi;
import by.itbatia.psp.individualsapi.dto.TokenRefreshRequest;
import by.itbatia.psp.individualsapi.dto.TokenResponse;
import by.itbatia.psp.individualsapi.dto.UserInfoResponse;
import by.itbatia.psp.individualsapi.dto.UserLoginRequest;
import by.itbatia.psp.individualsapi.dto.UserRegistrationRequest;
import by.itbatia.psp.individualsapi.service.RestValidationService;
import by.itbatia.psp.individualsapi.service.TokenService;
import by.itbatia.psp.individualsapi.service.UserService;
import by.itbatia.psp.individualsapi.util.RestUtil;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author Batsian_SV
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController implements AuthApi {

    private final UserService userService;
    private final TokenService tokenService;
    private final RestValidationService restValidationService;

    @Override
    public Mono<@NonNull ResponseEntity<@NonNull TokenResponse>> registerUser(Mono<UserRegistrationRequest> request, ServerWebExchange exchange) {
        return request
            .doOnNext(restValidationService::validate)
            .flatMap(userService::register)
            .map(tokenResponse -> ResponseEntity.status(HttpStatus.CREATED).body(tokenResponse));
    }

    @Override
    public Mono<@NonNull ResponseEntity<@NonNull TokenResponse>> login(Mono<UserLoginRequest> request, ServerWebExchange exchange) {
        return request
            .doOnNext(restValidationService::validate)
            .flatMap(req -> tokenService.login(req.getEmail(), req.getPassword()))
            .map(tokenResponse -> ResponseEntity.status(HttpStatus.OK).body(tokenResponse));
    }

    @Override
    public Mono<@NonNull ResponseEntity<@NonNull TokenResponse>> refreshToken(Mono<TokenRefreshRequest> request, ServerWebExchange exchange) {
        return request
            .doOnNext(restValidationService::validate)
            .flatMap(req -> tokenService.refresh(req.getRefreshToken()))
            .map(tokenResponse -> ResponseEntity.status(HttpStatus.OK).body(tokenResponse));
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public Mono<@NonNull ResponseEntity<@NonNull UserInfoResponse>> getCurrentUser(ServerWebExchange exchange) {
        return ReactiveSecurityContextHolder.getContext()
            .mapNotNull(SecurityContext::getAuthentication)
            .cast(JwtAuthenticationToken.class)
            .map(JwtAuthenticationToken::getToken)
            .flatMap(principal -> {
                String userId = principal.getSubject();
                return userService.getCurrentUser(userId)
                    .doOnNext(user -> RestUtil.enrichWithRoles(user, principal))
                    .map(ResponseEntity::ok);
            });
    }
}
