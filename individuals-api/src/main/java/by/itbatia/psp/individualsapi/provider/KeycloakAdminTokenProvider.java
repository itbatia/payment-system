package by.itbatia.psp.individualsapi.provider;

import static by.itbatia.psp.individualsapi.util.KeycloakUtil.CLIENT_ID;
import static by.itbatia.psp.individualsapi.util.KeycloakUtil.CLIENT_SECRET;
import static by.itbatia.psp.individualsapi.util.KeycloakUtil.GRANT_TYPE;
import static by.itbatia.psp.individualsapi.util.KeycloakUtil.GRANT_TYPE_CLIENT_CREDENTIALS;
import static by.itbatia.psp.individualsapi.util.KeycloakUtil.RECEIVING_TOKEN_URI;

import by.itbatia.psp.individualsapi.dto.KeycloakTokenResponse;
import by.itbatia.psp.individualsapi.property.KeycloakProperties;
import by.itbatia.psp.individualsapi.util.KeycloakUtil;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @author Batsian_SV
 */
@Component
@RequiredArgsConstructor
public class KeycloakAdminTokenProvider {

    private static final int DELTA_FOR_RESERVE = 10;

    private final WebClient keycloakWebClient;
    private final KeycloakProperties properties;

    private volatile String cachedToken;
    private volatile long tokenExpiryTime = 0;

    public Mono<@NonNull String> getValidAdminToken() {
        if (isTokenValid()) {
            return Mono.just(cachedToken);
        }
        return refreshToken();
    }

    /**
     * {@code isTokenValid()} - re-check in case another thread has already refreshed the token.
     *
     * @return token for admin requests.
     */
    private synchronized Mono<@NonNull String> refreshToken() {
        if (isTokenValid()) {
            return Mono.just(this.cachedToken);
        }
        return getAdminToken();
    }

    private boolean isTokenValid() {
        return this.cachedToken != null && System.currentTimeMillis() < tokenExpiryTime;
    }

    private Mono<@NonNull String> getAdminToken() {
        return keycloakWebClient.post()
            .uri(RECEIVING_TOKEN_URI, properties.getRealm())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData(GRANT_TYPE, GRANT_TYPE_CLIENT_CREDENTIALS)
                .with(CLIENT_ID, properties.getClientId())
                .with(CLIENT_SECRET, properties.getClientSecret()))
            .retrieve()
            .onStatus(HttpStatusCode::isError, KeycloakUtil::handleKeycloakException)
            .bodyToMono(KeycloakTokenResponse.class)
            .doOnNext(response -> {
                this.cachedToken = response.getAccessToken();
                this.tokenExpiryTime = calculateTokenExpiryTime(response);
            })
            .map(KeycloakTokenResponse::getAccessToken);
    }

    private long calculateTokenExpiryTime(KeycloakTokenResponse response) {
        long tokenExpiryInMillis = (response.getExpiresIn() - DELTA_FOR_RESERVE) * 1000L;
        return System.currentTimeMillis() + tokenExpiryInMillis;
    }
}
