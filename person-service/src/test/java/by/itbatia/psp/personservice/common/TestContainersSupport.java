package by.itbatia.psp.personservice.common;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * @author Batsian_SV
 */
public class TestContainersSupport {

    @SuppressWarnings("resource")
    public static PostgreSQLContainer<?> createPostgresContainer() {
        return new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");
    }

    public static void configurePostgresProperties(DynamicPropertyRegistry registry, PostgreSQLContainer<?> container) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }
}
