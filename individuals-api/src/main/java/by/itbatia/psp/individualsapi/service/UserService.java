package by.itbatia.psp.individualsapi.service;

import by.itbatia.individualsapi.dto.TokenResponse;
import by.itbatia.individualsapi.dto.UserRegistrationRequest;
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
public class UserService {

    private final KeycloakClient keycloakClient;
    private final TokenService tokenService;

    /**
     * 1. Create a new user in Keycloak;<br>
     * 2. Then obtain tokens for this user.
     */
    public Mono<@NonNull TokenResponse> register(UserRegistrationRequest request) {
        return keycloakClient.createUser(request.getEmail(), request.getPassword())
            .then(tokenService.login(request.getEmail(), request.getPassword()));
    }

//    public Mono<UserInfoResponse> getCurrentUser(String userId) {
//        return keycloakClient.fetchUserInfo(userId);
//    }
}
