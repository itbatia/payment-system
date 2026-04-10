package by.itbatia.psp.individualsapi.util;

import java.util.List;

import by.itbatia.psp.individualsapi.dto.UserInfoResponse;
import by.itbatia.psp.individualsapi.security.JwtGrantedAuthoritiesConverter;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import reactor.core.publisher.Mono;

/**
 * @author Batsian_SV
 */
@UtilityClass
public class RestUtil {

    /**
     * Example of an incoming access token:
     * <pre>{@code
     * {
     *   "exp": 1773741162,
     *   "iat": 1773740862,
     *   "jti": "onrtro:8306be2e-16f3-86c4-80fc-80c9ee31ca35",
     *   "iss": "http://localhost:8080/realms/individuals",
     *   "sub": "01170678-8ea8-41e3-80ad-341d957235fa",
     *   "typ": "Bearer",
     *   "azp": "individuals-api",
     *   "sid": "BoYMCliWpmfVIByrvKMO3gO_",
     *   "realm_access": {
     *     "roles": [
     *       "default-roles-individuals",
     *       "user"
     *     ]
     *   },
     *   "scope": "profile email",
     *   "email_verified": true,
     *   "preferred_username": "test@mail.ru",
     *   "email": "test@mail.ru"
     * }
     * }</pre>
     */
    public static void enrichWithRoles(UserInfoResponse userInfoResponse, Jwt principal) {
        List<String> roles = JwtGrantedAuthoritiesConverter.extractRoles(principal);
        userInfoResponse.setRoles(roles);
    }

    public static Mono<Jwt> getPrincipal() {
        return ReactiveSecurityContextHolder.getContext()
            .mapNotNull(SecurityContext::getAuthentication)
            .cast(JwtAuthenticationToken.class)
            .map(JwtAuthenticationToken::getToken);
    }
}
