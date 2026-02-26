package by.itbatia.psp.individualsapi.service;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import by.itbatia.individualsapi.dto.TokenResponse;
import by.itbatia.psp.individualsapi.client.KeycloakClient;
import by.itbatia.psp.individualsapi.service.impl.TokenServiceImpl;
import by.itbatia.psp.individualsapi.util.TokenResponseUtil;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * @author Batsian_SV
 */
@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {

    private static final String EMAIL = "test@example.com";
    private static final String PASSWORD = "password";

    @Mock
    private KeycloakClient keycloakClient;

    @InjectMocks
    private TokenServiceImpl tokenService;

    @Test
    @DisplayName("Test: successful user login.")
    void whenUserLogin_thenUserLoggedInSuccessfully() {
        // given
        TokenResponse tokenResponse = TokenResponseUtil.build();

        when(keycloakClient.requestToken(anyString(), anyString()))
            .thenReturn(Mono.just(tokenResponse));

        // when
        Mono<@NonNull TokenResponse> result = tokenService.login(EMAIL, PASSWORD);

        // then
        StepVerifier.create(result.doOnNext(System.out::println))
            .expectNext(tokenResponse)
            .verifyComplete();

        verify(keycloakClient).requestToken(EMAIL, PASSWORD);
        verify(keycloakClient, times(1)).requestToken(eq(EMAIL), eq(PASSWORD));
    }
}
