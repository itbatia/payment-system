package by.itbatia.psp.individualsapi.config;

import java.util.Collection;

import by.itbatia.psp.individualsapi.security.JwtGrantedAuthoritiesConverter;
import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author Batsian_SV
 */
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SpringSecurityConfig {

    private static final String[] AUTH_BLACKLIST = {
        "/api/v1/auth/me"
    };

    private static final String[] AUTH_WHITELIST = {
        "/api/v1/auth/registration",
        "/api/v1/auth/login",
        "/api/v1/auth/refresh-token"
    };

    private static final String[] SWAGGER = {
        "/v3/api-docs/**",
        "/swagger-ui/**",
        "/webjars/**"
    };

    private static final String[] METRICS = {
        "/actuator/**"
    };

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, JwtGrantedAuthoritiesConverter authoritiesConverter) {
        http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(exchanges -> exchanges
                .pathMatchers(AUTH_BLACKLIST).authenticated()
                .pathMatchers(AUTH_WHITELIST).permitAll()
                .pathMatchers(SWAGGER).permitAll()
                .pathMatchers(METRICS).permitAll()
                .anyExchange().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter(authoritiesConverter)))
            )
            .exceptionHandling(exceptionHandling ->
                exceptionHandling
                    .authenticationEntryPoint(authenticationEntryPoint())
                    .accessDeniedHandler(accessDeniedHandler())
            );

        return http.build();
    }

    @Bean
    public Converter<@NonNull Jwt, Mono<@NonNull AbstractAuthenticationToken>> jwtAuthenticationConverter(Converter<@NonNull Jwt, Collection<GrantedAuthority>> authoritiesConverter) {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return new ReactiveJwtAuthenticationConverterAdapter(converter);
    }

    @Bean
    public ServerAuthenticationEntryPoint authenticationEntryPoint() {
        return (exchange, ex) -> enrichResponseWithStatus(exchange, HttpStatus.UNAUTHORIZED);
    }

    @Bean
    public ServerAccessDeniedHandler accessDeniedHandler() {
        return (exchange, ex) -> enrichResponseWithStatus(exchange, HttpStatus.FORBIDDEN);
    }

    private Mono<@NonNull Void> enrichResponseWithStatus(ServerWebExchange exchange, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }
}
