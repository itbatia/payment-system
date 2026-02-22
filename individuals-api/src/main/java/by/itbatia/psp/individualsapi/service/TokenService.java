package by.itbatia.psp.individualsapi.service;

import by.itbatia.individualsapi.dto.TokenResponse;
import org.jspecify.annotations.NonNull;
import reactor.core.publisher.Mono;

/**
 * @author Batsian_SV
 */
public interface TokenService {

    Mono<@NonNull TokenResponse> login(String username, String password);

    Mono<@NonNull TokenResponse> refresh(String refreshToken);
}
