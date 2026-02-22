package by.itbatia.psp.individualsapi.exception.api;

import by.itbatia.psp.individualsapi.exception.ApiException;
import org.springframework.http.HttpStatus;

public class KeycloakException extends ApiException {

    public KeycloakException(HttpStatus httpStatus, String errorMsg) {
        super(httpStatus, errorMsg);
    }
}
