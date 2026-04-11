package by.itbatia.psp.individualsapi.client;

import java.util.UUID;

import by.itbatia.psp.common.dto.ErrorResponse;
import by.itbatia.psp.common.dto.IndividualCreateRequest;
import by.itbatia.psp.common.dto.IndividualResponse;
import by.itbatia.psp.common.dto.IndividualUpdateRequest;
import by.itbatia.psp.individualsapi.dto.UserRegistrationRequest;
import by.itbatia.psp.individualsapi.exception.api.PersonServiceApiException;
import by.itbatia.psp.individualsapi.mapper.IndividualApiMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @author Batsian_SV
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PersonServiceClient {

    private final WebClient personServiceWebClient;
    private final IndividualApiMapper individualApiMapper;

    public Mono<UserRegistrationRequest> createIndividual(IndividualCreateRequest request) {
        return personServiceWebClient
            .post()
            .uri("/api/v1/individuals")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, this::handlePersonServiceException)
            .onStatus(HttpStatusCode::is5xxServerError, this::handleException)
            .bodyToMono(IndividualResponse.class)
            .map(response -> individualApiMapper.toUserRegistrationRequest(request, response));
    }

    public Mono<IndividualResponse> getIndividualById(UUID id) {
        return personServiceWebClient
            .get()
            .uri("/api/v1/individuals/{id}", id)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, this::handlePersonServiceException)
            .onStatus(HttpStatusCode::is5xxServerError, this::handleException)
            .bodyToMono(IndividualResponse.class);
    }

    public Mono<IndividualResponse> getIndividualByUserEmail(String email) {
        return personServiceWebClient
            .get()
            .uri("/api/v1/individuals/email/{email}", email)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, this::handlePersonServiceException)
            .onStatus(HttpStatusCode::is5xxServerError, this::handleException)
            .bodyToMono(IndividualResponse.class);
    }

    public Mono<IndividualResponse> updateIndividual(IndividualUpdateRequest request) {
        return personServiceWebClient
            .put()
            .uri("/api/v1/individuals")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, this::handlePersonServiceException)
            .onStatus(HttpStatusCode::is5xxServerError, this::handleException)
            .bodyToMono(IndividualResponse.class);
    }

    public Mono<Void> deleteIndividual(UUID id) {
        return personServiceWebClient
            .delete()
            .uri("/api/v1/individuals/{id}", id)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, this::handlePersonServiceException)
            .onStatus(HttpStatusCode::is5xxServerError, this::handleException)
            .bodyToMono(Void.class);
    }

    private Mono<PersonServiceApiException> handlePersonServiceException(ClientResponse clientResponse) {
        return clientResponse.bodyToMono(ErrorResponse.class)
            .map(response -> {
                log.error("Request to PersonService failed: [code={}, error={}]", response.getStatus(), response.getError());

                String errorMessage = response.getError();
                HttpStatus httpStatus = HttpStatus.valueOf(response.getStatus());

                return new PersonServiceApiException(httpStatus, errorMessage);
            });
    }

    private Mono<PersonServiceApiException> handleException(ClientResponse clientResponse) {
        return clientResponse.bodyToMono(String.class)
            .map(response -> {
                log.error("Request to Person service failed: {}", response);
                return new PersonServiceApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Person service unavailable");
            });
    }
}
