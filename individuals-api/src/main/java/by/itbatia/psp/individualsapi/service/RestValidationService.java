package by.itbatia.psp.individualsapi.service;

import by.itbatia.individualsapi.dto.TokenRefreshRequest;
import by.itbatia.individualsapi.dto.UserLoginRequest;
import by.itbatia.individualsapi.dto.UserRegistrationRequest;
import by.itbatia.psp.individualsapi.exception.api.BadRequestApiException;

/**
 * @author Batsian_SV
 */
public interface RestValidationService {

    void validate(UserRegistrationRequest request) throws BadRequestApiException;

    void validate(UserLoginRequest request) throws BadRequestApiException;

    void validate(TokenRefreshRequest request) throws BadRequestApiException;
}
