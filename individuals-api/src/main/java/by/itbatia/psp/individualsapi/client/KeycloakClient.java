package by.itbatia.psp.individualsapi.client;

import static by.itbatia.psp.individualsapi.util.KeycloakUtil.BEARER_PREFIX;
import static by.itbatia.psp.individualsapi.util.KeycloakUtil.CLIENT_ID;
import static by.itbatia.psp.individualsapi.util.KeycloakUtil.CLIENT_SECRET;
import static by.itbatia.psp.individualsapi.util.KeycloakUtil.GRANT_TYPE;
import static by.itbatia.psp.individualsapi.util.KeycloakUtil.GRANT_TYPE_PASSWORD;
import static by.itbatia.psp.individualsapi.util.KeycloakUtil.GRANT_TYPE_REFRESH_TOKEN;
import static by.itbatia.psp.individualsapi.util.KeycloakUtil.PASSWORD;
import static by.itbatia.psp.individualsapi.util.KeycloakUtil.RECEIVING_TOKEN_URI;
import static by.itbatia.psp.individualsapi.util.KeycloakUtil.REFRESH_TOKEN;
import static by.itbatia.psp.individualsapi.util.KeycloakUtil.REGISTRATION_URI;
import static by.itbatia.psp.individualsapi.util.KeycloakUtil.USERNAME;

import by.itbatia.individualsapi.dto.TokenResponse;
import by.itbatia.psp.individualsapi.dto.KeycloakUserDto;
import by.itbatia.psp.individualsapi.property.KeycloakProperties;
import by.itbatia.psp.individualsapi.provider.KeycloakAdminTokenProvider;
import by.itbatia.psp.individualsapi.util.KeycloakUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @author Batsian_SV
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KeycloakClient {

    private final WebClient keycloakWebClient;
    private final KeycloakProperties properties;
    private final KeycloakAdminTokenProvider keycloakAdminTokenProvider;

    public Mono<@NonNull Void> createUser(String email, String password) {
        KeycloakUserDto userDto = KeycloakUserDto.build(email, password);

        return keycloakAdminTokenProvider.getValidAdminToken()
            .flatMap(token -> registerUser(userDto, token));
    }

    /**
     * <ul>
     *     Possible responses:
     *     <li>{@code 201} - Created</li>
     *     <li>{@code 400} - Bad Request</li>
     *     <li>{@code 403} - Forbidden</li>
     *     <li>{@code 409} - Conflict</li>
     *     <li>{@code 500} - Internal Server Error</li>
     * </ul>
     * <a href="https://www.keycloak.org/docs-api/latest/rest-api/index.html#_post_adminrealmsrealmusers">
     * <i>See Keycloak docs</i></a><br>
     */
    private Mono<@NonNull Void> registerUser(KeycloakUserDto userDto, String token) {
        return keycloakWebClient.post()
            .uri(REGISTRATION_URI, properties.getRealm())
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + token)
            .body(BodyInserters.fromValue(userDto))
            .retrieve()
            .onStatus(HttpStatusCode::isError, KeycloakUtil::handleKeycloakException)
            .toBodilessEntity()
            .then();
    }

    public Mono<@NonNull TokenResponse> requestToken(String username, String password) {
        return keycloakWebClient.post()
            .uri(RECEIVING_TOKEN_URI, properties.getRealm())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData(GRANT_TYPE, GRANT_TYPE_PASSWORD)
                .with(CLIENT_ID, properties.getClientId())
                .with(CLIENT_SECRET, properties.getClientSecret())
                .with(USERNAME, username)
                .with(PASSWORD, password))
            .retrieve()
            .onStatus(HttpStatusCode::isError, KeycloakUtil::handleKeycloakException)
            .bodyToMono(TokenResponse.class);
    }

    public Mono<@NonNull TokenResponse> refreshToken(String refreshToken) {
        return keycloakWebClient.post()
            .uri(RECEIVING_TOKEN_URI, properties.getRealm())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData(GRANT_TYPE, GRANT_TYPE_REFRESH_TOKEN)
                .with(CLIENT_ID, properties.getClientId())
                .with(CLIENT_SECRET, properties.getClientSecret())
                .with(REFRESH_TOKEN, refreshToken))
            .retrieve()
            .onStatus(HttpStatusCode::isError, KeycloakUtil::handleKeycloakException)
            .bodyToMono(TokenResponse.class);
    }
}
