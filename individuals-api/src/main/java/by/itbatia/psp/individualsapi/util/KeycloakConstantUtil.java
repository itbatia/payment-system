package by.itbatia.psp.individualsapi.util;

import lombok.experimental.UtilityClass;

/**
 * @author Batsian_SV
 */
@UtilityClass
public class KeycloakConstantUtil {

    public static final String URI_TO_CREATE_USER = "/admin/realms/{realm}/users";
    public static final String URI_TO_GET_TOKEN = "/realms/{realm}/protocol/openid-connect/token";
    public static final String URI_TO_GET_USER = "/admin/realms/{realm}/users/{user-id}";

    public static final String GRANT_TYPE = "grant_type";
    public static final String GRANT_TYPE_PASSWORD = "password";
    public static final String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";
    public static final String GRANT_TYPE_CLIENT_CREDENTIALS = "client_credentials";

    public static final String CLIENT_SECRET = "client_secret";
    public static final String CLIENT_ID = "client_id";

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String BEARER_PREFIX = "Bearer ";

    public static final String USER_ID_KC_ATTRIBUTE = "personServiceUserId";
    public static final String EMAIL_CLAIM = "email";
}
