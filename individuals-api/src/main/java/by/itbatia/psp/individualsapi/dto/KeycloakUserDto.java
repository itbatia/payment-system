package by.itbatia.psp.individualsapi.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

/**
 * See docs:<br>
 * <a href="https://www.keycloak.org/docs/latest/server_admin/index.html">
 * <i>Keycloak docs - Version 26.5.4</i></a><br>
 * <a href="https://www.keycloak.org/docs-api/latest/rest-api/index.html">
 * <i>Keycloak Admin REST API</i></a><br>
 * <a href="https://www.keycloak.org/docs-api/latest/rest-api/openapi.json">
 * <i>Keycloak Open API '3.0.3' json</i></a><br>
 * 1. UserRepresentation - is the {@code KeycloakUserDto}<br>
 * 2. CredentialRepresentation - is the {@code Credential}
 *
 * @author Batsian_SV
 */
@Data
@Builder
public class KeycloakUserDto {

    private String email;
    private String username;
    private boolean enabled;
    private boolean emailVerified;
    private List<Credential> credentials;

    /**
     * 1. By default {@code username} = incoming {@code email}<br>
     * 2. {@code username} - has unique constraint in Keycloak<br>
     * <br>
     * {@code enabled} - required field<br>
     * {@code emailVerified} - to skip warning 'Not verified'
     */
    public static KeycloakUserDto build(String email, String password) {
        return KeycloakUserDto.builder()
            .email(email)
            .username(email)
            .enabled(true)
            .emailVerified(true)
            .credentials(List.of(new Credential(password)))
            .build();
    }

    /**
     * {@code temporary} - if true, the user must change the password upon first login.<br>
     * {@code value} - is a user password.
     */
    @Data
    private static class Credential {
        private String type = "password";
        private String value;
        private boolean temporary = false;

        public Credential(String password) {
            this.value = password;
        }
    }
}
