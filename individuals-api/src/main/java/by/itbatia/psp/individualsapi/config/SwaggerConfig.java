package by.itbatia.psp.individualsapi.config;

import static by.itbatia.psp.individualsapi.util.ConstantUtil.COLON;

import java.util.List;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
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
@OpenAPIDefinition
@SecurityScheme(
    name = "oauth2",
    type = SecuritySchemeType.OAUTH2,
    flows = @OAuthFlows(
        authorizationCode = @OAuthFlow(
            authorizationUrl = "${keycloak.auth-server-url}/realms/psp/protocol/openid-connect/auth",
            tokenUrl = "${keycloak.auth-server-url}/realms/psp/protocol/openid-connect/token"
        )
    )
)
public class SwaggerConfig {

    private static final String SWAGGER_DESCRIPTION = """
        Individuals API - Authentication orchestrator.<br>
        Individuals API interacts with Keycloak via a REST API and provides clients with external endpoints for:
        1) registration;
        2) login;
        3) token refresh;
        4) retrieving information about the current user.
        """;

    @Bean
    public OpenAPI customOpenApi(@Value("${server.port}") String port,
                                 @Value("${info.application.name}") String name,
                                 @Value("${info.application.version}") String version,
                                 @Value("${application.individuals-api.swagger.dev-server}") String devServer,
                                 @Value("${application.individuals-api.swagger.local-server}") String localServer) {

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
                    .description("Local server"),
                new Server()
                    .url(devServer)
                    .description("Dev server")));
    }
}
