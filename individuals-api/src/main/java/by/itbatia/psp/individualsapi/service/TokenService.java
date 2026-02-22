package by.itbatia.psp.individualsapi.service;

import by.itbatia.individualsapi.dto.TokenResponse;
import by.itbatia.psp.individualsapi.client.KeycloakClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author Batsian_SV
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private final KeycloakClient keycloakClient;

    /**
     * @apiNote Calling Keycloak for a token using {@code grant_type = password}
     */
    public Mono<@NonNull TokenResponse> login(String username, String password) {
        return keycloakClient.requestToken(username, password);
    }

    /**
     * @apiNote Calling Keycloak to refresh a token
     */
    public Mono<@NonNull TokenResponse> refresh(String refreshToken) {
        return keycloakClient.refreshToken(refreshToken);
    }
}
