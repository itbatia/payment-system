package by.itbatia.psp.individualsapi.service.impl;

import by.itbatia.psp.individualsapi.dto.TokenResponse;
import by.itbatia.psp.individualsapi.dto.UserInfoResponse;
import by.itbatia.psp.individualsapi.dto.UserRegistrationRequest;
import by.itbatia.psp.individualsapi.client.KeycloakClient;
import by.itbatia.psp.individualsapi.enums.Meter;
import by.itbatia.psp.individualsapi.service.MetricsService;
import by.itbatia.psp.individualsapi.service.TokenService;
import by.itbatia.psp.individualsapi.service.UserService;
import io.micrometer.core.instrument.Timer;
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
public class UserServiceImpl implements UserService {

    private final TokenService tokenService;
    private final KeycloakClient keycloakClient;
    private final MetricsService metricsService;

    /**
     * 1. Create a new user in Keycloak;<br>
     * 2. Then obtain tokens for this user.
     */
    @Override
    public Mono<@NonNull TokenResponse> register(UserRegistrationRequest request) {
        Timer.Sample sample = metricsService.startTimer();
        return keycloakClient.createUser(request.getEmail(), request.getPassword())
            .then(tokenService.login(request.getEmail(), request.getPassword()))
            .doOnSuccess(_ -> {
                metricsService.incrementSuccessfulRegistration();
                metricsService.stopTimerOnSuccess(sample, Meter.KC_REGISTRATION_LATENCY);
                log.info("User with [email={}] registered successfully", request.getEmail());
            })
            .doOnError(_ -> {
                metricsService.incrementFailedRegistration();
                metricsService.stopTimerOnError(sample, Meter.KC_REGISTRATION_LATENCY);
            });
    }

    @Override
    public Mono<@NonNull UserInfoResponse> getCurrentUser(String userId) {
        return keycloakClient.fetchUserInfo(userId);
    }
}
