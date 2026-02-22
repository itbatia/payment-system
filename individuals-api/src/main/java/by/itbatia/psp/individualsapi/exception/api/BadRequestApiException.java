package by.itbatia.psp.individualsapi.exception.api;

import by.itbatia.psp.individualsapi.exception.ApiException;
import org.springframework.http.HttpStatus;

/**
 * @author Batsian_SV
 */
public class BadRequestApiException extends ApiException {

    public BadRequestApiException(String errorMsg) {
        super(HttpStatus.BAD_REQUEST, errorMsg);
    }
}
