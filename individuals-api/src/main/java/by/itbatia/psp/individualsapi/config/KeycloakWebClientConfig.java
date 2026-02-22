package by.itbatia.psp.individualsapi.config;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

import by.itbatia.psp.individualsapi.property.KeycloakProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

/**
 * @author Batsian_SV
 */
@Configuration
public class KeycloakWebClientConfig {

    @Bean(name = "keycloakWebClient")
    public WebClient keycloakWebClient(KeycloakProperties properties) {

        HttpClient httpClient = HttpClient.create()
            .responseTimeout(Duration.ofMillis(properties.getConnectionTimeout()));

        return WebClient.builder()
            .baseUrl(properties.getBaseUrl())
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.ACCEPT_CHARSET, StandardCharsets.UTF_8.name())
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();
    }

    //TODO fix
//    @Bean
//    @Qualifier("keycloakAdminClient")
//    public WebClient keycloakAdminWebClient(@Value("${keycloak.auth-server-url}") String authServerUrl,
//                                            @Value("${keycloak.realm}") String realm,
//                                            @Value("${keycloak.admin.client-id}") String clientId,
//                                            @Value("${keycloak.admin.client-secret}") String clientSecret) {
//        // Получение токена администратора и создание WebClient
//        // ...
//    }
//
//    @Bean
//    @Qualifier("keycloakPublicClient")
//    public WebClient keycloakPublicWebClient(@Value("${keycloak.auth-server-url}") String authServerUrl,
//                                             @Value("${keycloak.realm}") String realm,
//                                             @Value("${keycloak.public.client-id}") String clientId) {
//        return WebClient.builder()
//            .baseUrl(authServerUrl + "/realms/" + realm + "/protocol/openid-connect/token")
//            .build();
//    }
}
