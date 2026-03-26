package by.itbatia.psp.individualsapi.service;

import by.itbatia.psp.individualsapi.dto.TokenResponse;
import by.itbatia.psp.individualsapi.dto.UserInfoResponse;
import by.itbatia.psp.individualsapi.dto.UserRegistrationRequest;
import org.jspecify.annotations.NonNull;
import reactor.core.publisher.Mono;

/**
 * @author Batsian_SV
 */
public interface UserService {

    Mono<@NonNull TokenResponse> register(UserRegistrationRequest request);

    Mono<@NonNull UserInfoResponse> getCurrentUser(String userId);
}
