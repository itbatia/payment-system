package by.itbatia.psp.individualsapi.config;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
public class WebClientConfig {

    @Value("${application.individuals-api.rest.connection-timeout}")
    private int connectionTimeout;

    @Bean
    public WebClient keycloakWebClient() {

        HttpClient httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeout)                       //Время ожидания соединения
            .responseTimeout(Duration.ofMillis(connectionTimeout))                                 //Время ожидания ответа - основная
            .doOnConnected(conn -> conn
                .addHandlerLast(new ReadTimeoutHandler(connectionTimeout, TimeUnit.MILLISECONDS))  //Время ожидания чтения
                .addHandlerLast(new WriteTimeoutHandler(connectionTimeout, TimeUnit.MILLISECONDS)) //Время ожидания записи
            );

        return WebClient.builder()
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
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
