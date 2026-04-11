package by.itbatia.psp.individualsapi;

import by.itbatia.psp.individualsapi.property.KeycloakProperties;
import by.itbatia.psp.individualsapi.property.PersonServiceClientProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties( {
    KeycloakProperties.class,
    PersonServiceClientProperties.class
})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
