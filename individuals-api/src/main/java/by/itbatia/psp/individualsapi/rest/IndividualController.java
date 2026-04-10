package by.itbatia.psp.individualsapi.rest;

import java.util.UUID;

import by.itbatia.psp.common.dto.IndividualResponse;
import by.itbatia.psp.common.dto.IndividualUpdateRequest;
import by.itbatia.psp.individualsapi.api.IndividualsApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author Batsian_SV
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class IndividualController implements IndividualsApi {

    @Override
    public Mono<ResponseEntity<Void>> delete(UUID id, ServerWebExchange exchange) {
        return null;
    }

    @Override
    public Mono<ResponseEntity<IndividualResponse>> getByEmail(String email, ServerWebExchange exchange) {
        return null;
    }

    @Override
    public Mono<ResponseEntity<IndividualResponse>> getById(UUID id, ServerWebExchange exchange) {
        return null;
    }

    @Override
    public Mono<ResponseEntity<IndividualResponse>> update(Mono<IndividualUpdateRequest> individualUpdateRequest, ServerWebExchange exchange) {
        return null;
    }

    //    private final UserService userService;
//    private final TokenService tokenService;
//    private final PersonServiceClient personServiceClient;
//    private final RestValidationService restValidationService;
//
//    @Override
//    public Mono<@NonNull ResponseEntity<@NonNull TokenResponse>> registerUser(Mono<IndividualCreateRequest> request, ServerWebExchange exchange) {
//        return request
//            .doOnNext(restValidationService::validate)
//            .flatMap(personServiceClient::createIndividual)
//            .flatMap(userService::register)
//            .map(tokenResponse -> ResponseEntity.status(HttpStatus.CREATED).body(tokenResponse));
//    }
//
//    @Override
//    public Mono<@NonNull ResponseEntity<@NonNull TokenResponse>> login(Mono<UserLoginRequest> request, ServerWebExchange exchange) {
//        return request
//            .doOnNext(restValidationService::validate)
//            .flatMap(req -> tokenService.login(req.getEmail(), req.getPassword()))
//            .map(tokenResponse -> ResponseEntity.status(HttpStatus.OK).body(tokenResponse));
//    }
//
//    @Override
//    public Mono<@NonNull ResponseEntity<@NonNull TokenResponse>> refreshToken(Mono<TokenRefreshRequest> request, ServerWebExchange exchange) {
//        return request
//            .doOnNext(restValidationService::validate)
//            .flatMap(req -> tokenService.refresh(req.getRefreshToken()))
//            .map(tokenResponse -> ResponseEntity.status(HttpStatus.OK).body(tokenResponse));
//    }
//
//    @Override
//    @PreAuthorize("hasRole('USER')")
//    public Mono<@NonNull ResponseEntity<@NonNull UserInfoResponse>> getCurrentUser(ServerWebExchange exchange) {
//        return RestUtil.getPrincipal()
//            .flatMap(principal -> {
//                String userId = principal.getSubject();
//                return userService.getCurrentUser(userId)
//                    .doOnNext(user -> RestUtil.enrichWithRoles(user, principal))
//                    .map(ResponseEntity::ok);
//            });
//    }
}
