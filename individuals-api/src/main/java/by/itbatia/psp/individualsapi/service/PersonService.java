package by.itbatia.psp.individualsapi.service;

import by.itbatia.psp.individualsapi.dto.TokenResponse;
import by.itbatia.psp.individualsapi.dto.UserRegistrationRequest;
import reactor.core.publisher.Mono;

/**
 * @author Batsian_SV
 */
public interface PersonService {

    Mono<TokenResponse> registerWithFallback(UserRegistrationRequest request);
}
