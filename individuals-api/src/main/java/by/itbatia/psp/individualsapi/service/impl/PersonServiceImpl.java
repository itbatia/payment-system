package by.itbatia.psp.individualsapi.service.impl;

import java.util.UUID;

import by.itbatia.psp.individualsapi.client.PersonServiceClient;
import by.itbatia.psp.individualsapi.dto.TokenResponse;
import by.itbatia.psp.individualsapi.dto.UserRegistrationRequest;
import by.itbatia.psp.individualsapi.service.PersonService;
import by.itbatia.psp.individualsapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author Batsian_SV
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final UserService userService;
    private final PersonServiceClient personServiceClient;

    /**
     * If creating a new user in Keycloak is failed, we delete that user in the person-service and return the original error.
     */
    @Override
    public Mono<TokenResponse> registerWithFallback(UserRegistrationRequest request) {
        UUID individualId = request.getIndividualId();
        System.out.println("1individualId = " + individualId);

        return userService.register(request)
            .doOnError(throwable -> log.warn("Rollback user in 'person-service', because registration error in KC: {}", throwable.getMessage()))
            .onErrorResume(throwable -> personServiceClient.deleteIndividual(individualId)
                .then(Mono.error(throwable)));
    }
}
