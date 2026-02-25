package by.itbatia.psp.individualsapi.dto;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import by.itbatia.individualsapi.dto.UserInfoResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author Batsian_SV
 * @apiNote Keycloak object:
 * <a href="https://www.keycloak.org/docs-api/latest/rest-api/index.html#UserRepresentation">
 * <i>UserRepresentation</i></a><br>
 * this is the custom object {@code KeycloakUserRepresentation}
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class KeycloakUserRepresentation {

    private String email;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String username;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean enabled;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean emailVerified;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Credential> credentials;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String id;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long createdTimestamp;

    /**
     * Keycloak object:
     * <a href="https://www.keycloak.org/docs-api/latest/rest-api/index.html#CredentialRepresentation">
     * <i>CredentialRepresentation</i></a>
     * this is the custom object {@code Credential}.<br>
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

    //-//-//-//-//------------------------   Build KC object   ------------------------//-//-//-//-//

    /**
     * Description:<br>
     * 1. By default {@code username} = incoming {@code email};<br>
     * 2. {@code username} - has unique constraint in Keycloak;<br>
     * 3. By default, user is {@code enabled}. Required field;<br>
     * 4. {@code emailVerified} - to skip warning 'Not verified'.
     */
    public static KeycloakUserRepresentation build(String email, String password) {
        KeycloakUserRepresentation kcUser = new KeycloakUserRepresentation();

        kcUser.setEmail(email);
        kcUser.setUsername(email);
        kcUser.setEnabled(true);
        kcUser.setEmailVerified(true);
        kcUser.setCredentials(List.of(new Credential(password)));

        return kcUser;
    }

    //-//-//-//-//---------------------   Build response object   ---------------------//-//-//-//-//

    public UserInfoResponse toResponse() {
        UserInfoResponse response = new UserInfoResponse();

        response.setId(this.id);
        response.setEmail(this.email);
        response.setCreatedAt(this.toOffsetDateTime());

        return response;
    }

    private OffsetDateTime toOffsetDateTime() {
        if (this.createdTimestamp == null) {
            return null;
        }
        return Instant.ofEpochMilli(this.createdTimestamp).atOffset(ZoneOffset.UTC);
    }
}
