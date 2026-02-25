package by.itbatia.psp.individualsapi.client;

import static by.itbatia.psp.individualsapi.util.KeycloakUtil.BEARER_PREFIX;
import static by.itbatia.psp.individualsapi.util.KeycloakUtil.CLIENT_ID;
import static by.itbatia.psp.individualsapi.util.KeycloakUtil.CLIENT_SECRET;
import static by.itbatia.psp.individualsapi.util.KeycloakUtil.GRANT_TYPE;
import static by.itbatia.psp.individualsapi.util.KeycloakUtil.GRANT_TYPE_PASSWORD;
import static by.itbatia.psp.individualsapi.util.KeycloakUtil.GRANT_TYPE_REFRESH_TOKEN;
import static by.itbatia.psp.individualsapi.util.KeycloakUtil.PASSWORD;
import static by.itbatia.psp.individualsapi.util.KeycloakUtil.URI_TO_GET_TOKEN;
import static by.itbatia.psp.individualsapi.util.KeycloakUtil.REFRESH_TOKEN;
import static by.itbatia.psp.individualsapi.util.KeycloakUtil.URI_TO_CREATE_USER;
import static by.itbatia.psp.individualsapi.util.KeycloakUtil.URI_TO_GET_USER;
import static by.itbatia.psp.individualsapi.util.KeycloakUtil.USERNAME;

import by.itbatia.individualsapi.dto.TokenResponse;
import by.itbatia.individualsapi.dto.UserInfoResponse;
import by.itbatia.psp.individualsapi.dto.KeycloakUserRepresentation;
import by.itbatia.psp.individualsapi.property.KeycloakProperties;
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
    private final KeycloakAdminTokenClient keycloakAdminTokenClient;

    //-//-//-//-//--------------------------   Create user   --------------------------//-//-//-//-//

    /**
     * Keycloak docs:<br>
     * <br>
     * Request:
     * <a href="https://www.keycloak.org/docs-api/latest/rest-api/index.html#_post_adminrealmsrealmusers">
     * <i>POST /admin/realms/{realm}/users</i></a><br>
     * Request:
     * <a href="https://www.keycloak.org/docs-api/latest/rest-api/index.html#UserRepresentation">
     * <i>UserRepresentation</i></a><br>
     * <br>
     * Keycloak {@code UserRepresentation} this is the custom {@link by.itbatia.psp.individualsapi.dto.KeycloakUserRepresentation KeycloakUserRepresentation}
     * <br><br>
     * <ul>
     *     Possible responses:
     *     <li>{@code 201} - Created</li>
     *     <li>{@code 400} - Bad Request</li>
     *     <li>{@code 403} - Forbidden</li>
     *     <li>{@code 409} - Conflict</li>
     *     <li>{@code 500} - Internal Server Error</li>
     * </ul>
     */
    public Mono<@NonNull Void> createUser(String email, String password) {
        KeycloakUserRepresentation kcUser = KeycloakUserRepresentation.build(email, password);

        return keycloakAdminTokenClient.getValidAdminToken()
            .flatMap(token -> create(kcUser, token));
    }

    private Mono<@NonNull Void> create(KeycloakUserRepresentation kcUser, String token) {
        return keycloakWebClient.post()
            .uri(URI_TO_CREATE_USER, properties.getRealm())
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + token)
            .body(BodyInserters.fromValue(kcUser))
            .retrieve()
            .onStatus(HttpStatusCode::isError, KeycloakUtil::handleKeycloakException)
            .toBodilessEntity()
            .then();
    }

    //-//-//-//-//-------------------------   Request token   -------------------------//-//-//-//-//

    public Mono<@NonNull TokenResponse> requestToken(String username, String password) {
        return keycloakWebClient.post()
            .uri(URI_TO_GET_TOKEN, properties.getRealm())
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

    //-//-//-//-//-------------------------   Refresh token   -------------------------//-//-//-//-//

    public Mono<@NonNull TokenResponse> refreshToken(String refreshToken) {
        return keycloakWebClient.post()
            .uri(URI_TO_GET_TOKEN, properties.getRealm())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData(GRANT_TYPE, GRANT_TYPE_REFRESH_TOKEN)
                .with(CLIENT_ID, properties.getClientId())
                .with(CLIENT_SECRET, properties.getClientSecret())
                .with(REFRESH_TOKEN, refreshToken))
            .retrieve()
            .onStatus(HttpStatusCode::isError, KeycloakUtil::handleKeycloakException)
            .bodyToMono(TokenResponse.class);
    }

    //-//-//-//-//---------------------------   Get user   ----------------------------//-//-//-//-//

    /**
     * Keycloak docs:<br>
     * <br>
     * Request:
     * <a href="https://www.keycloak.org/docs-api/latest/rest-api/index.html#_get_adminrealmsrealmusersuser_id">
     * <i>GET /admin/realms/{realm}/users/{user-id}</i></a><br>
     * Response:
     * <a href="https://www.keycloak.org/docs-api/latest/rest-api/index.html#UserRepresentation">
     * <i>UserRepresentation</i></a><br>
     * <br>
     * Keycloak {@code UserRepresentation} this is the custom {@link by.itbatia.psp.individualsapi.dto.KeycloakUserRepresentation KeycloakUserRepresentation}
     */
    public Mono<@NonNull UserInfoResponse> fetchUserInfo(String userId) {
        return keycloakAdminTokenClient.getValidAdminToken()
            .flatMap(token -> getUserById(userId, token));
    }

    private Mono<@NonNull UserInfoResponse> getUserById(String userId, String token) {
        return keycloakWebClient.get()
            .uri(URI_TO_GET_USER, properties.getRealm(), userId)
            .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + token)
            .retrieve()
            .onStatus(HttpStatusCode::isError, KeycloakUtil::handleKeycloakException)
            .bodyToMono(KeycloakUserRepresentation.class)
            .map(KeycloakUserRepresentation::toResponse);
    }
}
