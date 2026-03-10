package by.itbatia.psp.individualsapi.config;

import static by.itbatia.psp.individualsapi.util.ConstantUtil.COLON;

import java.util.List;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Batsian_SV
 */
@Configuration
public class SwaggerConfig {

    private static final String SWAGGER_DESCRIPTION = """
        Individuals API - this is an authentication orchestrator.<br>
        Interacts with Keycloak via a REST API and provides clients with external endpoints for:
        1) registration;
        2) login;
        3) token refresh;
        4) retrieving information about the current user.
        """;

    @Bean
    public OpenAPI customOpenApi(@Value("${server.port}") String port,
                                 @Value("${info.application.name}") String name,
                                 @Value("${info.application.version}") String version,
                                 @Value("${individuals-api.swagger.local-server}") String localServer) {

        return new OpenAPI()
            .info(new Info()
                .title(name)
                .version(version)
                .description(SWAGGER_DESCRIPTION)
                .license(new License().name("Apache 2.0")
                    .url("https://springdoc.org/v2/")))
            .servers(List.of(
                new Server()
                    .url(localServer + COLON + port)
                    .description("Local server")));
    }
}
