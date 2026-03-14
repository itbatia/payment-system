package by.itbatia.psp.individualsapi.config;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * @author Batsian_SV
 */
@Testcontainers
public abstract class KeycloakTestcontainerConfig {

    protected static final KeycloakContainer KEYCLOAK_CONTAINER;

    static {
        KEYCLOAK_CONTAINER = new KeycloakContainer("quay.io/keycloak/keycloak:26.5")
            .withRealmImportFile("test-realm-config.json");

        KEYCLOAK_CONTAINER.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("individuals-api.keycloak.base-url", KEYCLOAK_CONTAINER::getAuthServerUrl);
        registry.add("individuals-api.keycloak.realm", () -> "individuals");
        registry.add("individuals-api.keycloak.client-id", () -> "individuals-api");
        registry.add("individuals-api.keycloak.client-secret", () -> "test-secret");
    }
}
