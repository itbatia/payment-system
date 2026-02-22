package by.itbatia.psp.individualsapi.service.impl;

import by.itbatia.individualsapi.dto.TokenResponse;
import by.itbatia.individualsapi.dto.UserRegistrationRequest;
import by.itbatia.psp.individualsapi.client.KeycloakClient;
import by.itbatia.psp.individualsapi.service.TokenService;
import by.itbatia.psp.individualsapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author Batsian_SV
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final TokenService tokenService;
    private final KeycloakClient keycloakClient;

    /**
     * 1. Create a new user in Keycloak;<br>
     * 2. Then obtain tokens for this user.
     */
    @Override
    public Mono<@NonNull TokenResponse> register(UserRegistrationRequest request) {
        return keycloakClient.createUser(request.getEmail(), request.getPassword())
            .then(tokenService.login(request.getEmail(), request.getPassword()));
    }

//    public Mono<UserInfoResponse> getCurrentUser(String userId) {
//        return keycloakClient.fetchUserInfo(userId);
//    }
}
