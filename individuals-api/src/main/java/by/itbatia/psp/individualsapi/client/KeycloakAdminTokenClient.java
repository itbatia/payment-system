package by.itbatia.psp.individualsapi.client;

import static by.itbatia.psp.individualsapi.util.KeycloakConstantUtil.CLIENT_ID;
import static by.itbatia.psp.individualsapi.util.KeycloakConstantUtil.CLIENT_SECRET;
import static by.itbatia.psp.individualsapi.util.KeycloakConstantUtil.GRANT_TYPE;
import static by.itbatia.psp.individualsapi.util.KeycloakConstantUtil.GRANT_TYPE_CLIENT_CREDENTIALS;
import static by.itbatia.psp.individualsapi.util.KeycloakConstantUtil.URI_TO_GET_TOKEN;

import java.time.Duration;

import by.itbatia.psp.individualsapi.dto.KeycloakTokenResponse;
import by.itbatia.psp.individualsapi.property.KeycloakProperties;
import by.itbatia.psp.individualsapi.util.KeycloakUtil;
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
public class KeycloakAdminTokenClient {

    private final WebClient keycloakWebClient;
    private final KeycloakProperties properties;
    private final Mono<String> cachedToken;

    public KeycloakAdminTokenClient(WebClient keycloakWebClient, KeycloakProperties properties) {
        this.keycloakWebClient = keycloakWebClient;
        this.properties = properties;
        this.cachedToken = Mono.defer(this::getAdminToken)
            .cache(Duration.ofSeconds(properties.getAdminTokenCachingTimeInSec()));
    }

    public Mono<@NonNull String> getValidAdminToken() {
        return cachedToken;
    }

    private Mono<@NonNull String> getAdminToken() {
        return keycloakWebClient.post()
            .uri(URI_TO_GET_TOKEN, properties.getRealm())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData(GRANT_TYPE, GRANT_TYPE_CLIENT_CREDENTIALS)
                .with(CLIENT_ID, properties.getClientId())
                .with(CLIENT_SECRET, properties.getClientSecret()))
            .retrieve()
            .onStatus(HttpStatusCode::isError, KeycloakUtil::handleKeycloakException)
            .bodyToMono(KeycloakTokenResponse.class)
            .map(KeycloakTokenResponse::getAccessToken);
    }
}
