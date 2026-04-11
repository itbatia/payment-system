package by.itbatia.psp.individualsapi.rest;

import by.itbatia.psp.common.dto.IndividualResponse;
import by.itbatia.psp.common.dto.IndividualUpdateRequest;
import by.itbatia.psp.individualsapi.api.IndividualsApi;
import by.itbatia.psp.individualsapi.client.PersonServiceClient;
import by.itbatia.psp.individualsapi.util.RestUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * @author Batsian_SV
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class IndividualController implements IndividualsApi {

    private final PersonServiceClient personServiceClient;

    @Override
    @PreAuthorize("hasRole('USER')")
    public Mono<ResponseEntity<IndividualResponse>> getById(UUID id, ServerWebExchange exchange) {
        return personServiceClient.getIndividualById(id)
            .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<IndividualResponse>> getByEmail(ServerWebExchange exchange) {
        return RestUtil.getPrincipalUserEmail()
            .flatMap(personServiceClient::getIndividualByUserEmail)
            .map(ResponseEntity::ok);
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public Mono<ResponseEntity<IndividualResponse>> update(Mono<IndividualUpdateRequest> request, ServerWebExchange exchange) {
        return request
            .flatMap(personServiceClient::updateIndividual)
            .map(ResponseEntity::ok);
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public Mono<ResponseEntity<Void>> delete(UUID id, ServerWebExchange exchange) {
        return personServiceClient.deleteIndividual(id)
            .map(_ -> ResponseEntity.noContent().build());
    }
}
