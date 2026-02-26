package by.itbatia.psp.individualsapi.service;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import by.itbatia.individualsapi.dto.TokenResponse;
import by.itbatia.individualsapi.dto.UserRegistrationRequest;
import by.itbatia.psp.individualsapi.client.KeycloakClient;
import by.itbatia.psp.individualsapi.exception.api.KeycloakException;
import by.itbatia.psp.individualsapi.service.impl.UserServiceImpl;
import by.itbatia.psp.individualsapi.util.TokenResponseUtil;
import by.itbatia.psp.individualsapi.util.UserRegistrationRequestUtil;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * @author Batsian_SV
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private static final String EMAIL = "test@example.com";
    private static final String PASSWORD = "password";
    private static final String ERROR_MSG = "User exists with same email";

    @Mock
    private TokenService tokenService;

    @Mock
    private KeycloakClient keycloakClient;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Test: successful user registration")
    void whenRegisterUser_thenUserCreatedSuccessfully() {
        // given
        UserRegistrationRequest request = UserRegistrationRequestUtil.build(EMAIL, PASSWORD);
        TokenResponse tokenResponse = TokenResponseUtil.build();

        when(keycloakClient.createUser(anyString(), anyString()))
            .thenReturn(Mono.empty());
        when(tokenService.login(anyString(), anyString()))
            .thenReturn(Mono.just(tokenResponse));

        // when
        Mono<@NonNull TokenResponse> result = userService.register(request);

        // then
        StepVerifier.create(result.doOnNext(System.out::println))
            .expectNext(tokenResponse)
            .verifyComplete();

        verify(keycloakClient).createUser(EMAIL, PASSWORD);
        verify(tokenService).login(EMAIL, PASSWORD);
        verify(keycloakClient, times(1)).createUser(eq(EMAIL), eq(PASSWORD));
        verify(tokenService, times(1)).login(eq(EMAIL), eq(PASSWORD));
    }

    @Test
    @DisplayName("Test: unsuccessful user registration")
    void givenRegisterUser_whenKeycloakFails_thenExceptionIsThrown() {
        // given
        UserRegistrationRequest request = UserRegistrationRequestUtil.build(EMAIL, PASSWORD);

        when(keycloakClient.createUser(anyString(), anyString()))
            .thenReturn(Mono.error(new KeycloakException(HttpStatus.CONFLICT, ERROR_MSG)));
        when(tokenService.login(anyString(), anyString()))
            .thenReturn(Mono.empty());

        // when
        Mono<@NonNull TokenResponse> result = userService.register(request);

        // then
        StepVerifier.create(result.doOnNext(System.out::println))
            .expectErrorMatches(throwable -> throwable.getMessage().contains(ERROR_MSG))
            .verify();
    }
}
