package by.itbatia.psp.individualsapi.client;

import by.itbatia.individualsapi.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

//    private final WebClient webClient;
//
//    public KeycloakClient(@Value("${keycloak.url}") String keycloakUrl) {
//        this.webClient = WebClient.builder().baseUrl(keycloakUrl).build();
//    }
//
//    public Mono<TokenResponse> requestToken(String username, String password) {
//        return webClient.post()
//            .uri("/realms/{realm}/protocol/openid-connect/token", REALM_NAME)
//            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//            .body(BodyInserters.fromFormData("grant_type", "password")
//                .with("client_id", CLIENT_ID)
//                .with("username", username)
//                .with("password", password))
//            .retrieve()
//            .bodyToMono(TokenResponse.class);
//    }
}
