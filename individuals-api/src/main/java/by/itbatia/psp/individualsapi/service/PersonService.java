package by.itbatia.psp.individualsapi.service;

import by.itbatia.psp.common.dto.IndividualCreateRequest;
import by.itbatia.psp.individualsapi.dto.TokenResponse;
import reactor.core.publisher.Mono;

/**
 * @author Batsian_SV
 */
public interface PersonService {

    Mono<TokenResponse> registerWithFallback(IndividualCreateRequest request);
}
