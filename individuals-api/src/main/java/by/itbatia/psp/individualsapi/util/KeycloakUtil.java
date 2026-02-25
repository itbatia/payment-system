package by.itbatia.psp.individualsapi.util;

import by.itbatia.psp.individualsapi.exception.api.KeycloakException;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.JsonNodeFactory;
import tools.jackson.databind.node.ObjectNode;

/**
 * @author Batsian_SV
 */
@UtilityClass
public class KeycloakUtil {

    public static final String URI_TO_CREATE_USER = "/admin/realms/{realm}/users";
    public static final String URI_TO_GET_TOKEN = "/realms/{realm}/protocol/openid-connect/token";
    public static final String URI_TO_GET_USER = "/admin/realms/{realm}/users/{user-id}";
    public static final String GRANT_TYPE = "grant_type";
    public static final String GRANT_TYPE_PASSWORD = "password";
    public static final String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";
    public static final String GRANT_TYPE_CLIENT_CREDENTIALS = "client_credentials";
    public static final String CLIENT_SECRET = "client_secret";
    public static final String CLIENT_ID = "client_id";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String BEARER_PREFIX = "Bearer ";

    private static final String UNKNOWN_ERROR = "Unknown error";
    private static final String POSSIBLE_FIELD_NAME_1 = "errorMessage";
    private static final String POSSIBLE_FIELD_NAME_2 = "error_description";
    private static final String POSSIBLE_FIELD_NAME_3 = "error";
    private static final String FALLBACK_ERROR_MSG = "Failed to parse Keycloak error: %s";
    private static final String AUTH_ERROR_MSG = "Authentication error: %s";

    public static Mono<@NonNull KeycloakException> handleKeycloakException(ClientResponse clientResponse) {
        return clientResponse.bodyToMono(JsonNode.class)
            .onErrorResume(throwable -> {
                ObjectNode fallback = JsonNodeFactory.instance.objectNode();
                String errorMsg = String.format(FALLBACK_ERROR_MSG, throwable.getMessage());
                fallback.put(POSSIBLE_FIELD_NAME_2, errorMsg);
                return Mono.just(fallback);
            })
            .map(errorNode -> {
                String errorMessage = extractErrorMessage(errorNode);
                return new KeycloakException((HttpStatus) clientResponse.statusCode(), errorMessage);
            });
    }

    private static String extractErrorMessage(JsonNode errorNode) {
        if (errorNode == null) {
            return UNKNOWN_ERROR;
        }
        if (errorNode.has(POSSIBLE_FIELD_NAME_1)) {
            return errorNode.get(POSSIBLE_FIELD_NAME_1).asString();
        }
        if (errorNode.has(POSSIBLE_FIELD_NAME_2)) {
            return errorNode.get(POSSIBLE_FIELD_NAME_2).asString();
        }
        if (errorNode.has(POSSIBLE_FIELD_NAME_3)) {
            return String.format(AUTH_ERROR_MSG, errorNode.get(POSSIBLE_FIELD_NAME_3).asString());
        }
        return UNKNOWN_ERROR;
    }
}
