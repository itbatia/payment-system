package by.itbatia.psp.individualsapi.service.impl;

import by.itbatia.individualsapi.dto.TokenResponse;
import by.itbatia.psp.individualsapi.client.KeycloakClient;
import by.itbatia.psp.individualsapi.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author Batsian_SV
 */
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final KeycloakClient keycloakClient;

    /**
     * @param username is a user email
     * @apiNote Calling Keycloak for a token using {@code grant_type = password}
     */
    @Override
    public Mono<@NonNull TokenResponse> login(String username, String password) {
        return keycloakClient.requestToken(username, password);
    }

    /**
     * @apiNote Calling Keycloak to refresh a token using {@code grant_type = refresh_token}
     */
    @Override
    public Mono<@NonNull TokenResponse> refresh(String refreshToken) {
        return keycloakClient.refreshToken(refreshToken);
    }
}
