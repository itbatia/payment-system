package by.itbatia.psp.individualsapi.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Batsian_SV
 */
@Data
@ConfigurationProperties(prefix = "individuals-api.person-service-client")
public class PersonServiceClientProperties {

    private String baseUrl;
    private int connectionTimeout;
}
