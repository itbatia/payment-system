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
}
