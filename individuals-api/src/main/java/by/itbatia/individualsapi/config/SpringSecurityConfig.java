package by.itbatia.individualsapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * @author Batsian_SV
 */
@Configuration
@EnableWebFluxSecurity
public class SpringSecurityConfig {

    private static final String ENTRY_POINT = "/api/**";
    private static final String[] AUTH_WHITELIST = {
        "/v3/api-docs/**",
        "/swagger-ui/**",
        "/webjars/**"
    };

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
            .authorizeExchange(exchanges -> exchanges
                .pathMatchers(AUTH_WHITELIST).permitAll()
                .anyExchange().authenticated()
            )
//            .oauth2ResourceServer(oauth2 -> oauth2
//                .jwt(jwt -> jwt
//                    .jwtAuthenticationConverter(jwtAuthenticationConverter())
//                )
//            );
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
