package by.itbatia.psp.individualsapi.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Batsian_SV
 */
@Data
@ConfigurationProperties(prefix = "keycloak")
public class KeycloakProperties {

    private String realm;
    private String baseUrl;
    private String clientId;
    private String clientSecret;
    private int connectionTimeout;
}
