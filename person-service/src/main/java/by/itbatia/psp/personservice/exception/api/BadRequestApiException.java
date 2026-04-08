package by.itbatia.psp.personservice.exception.api;

import by.itbatia.psp.personservice.exception.ApiException;
import org.springframework.http.HttpStatus;

/**
 * @author Batsian_SV
 */
public class BadRequestApiException extends ApiException {

    public BadRequestApiException(String errorMsg) {
        super(HttpStatus.BAD_REQUEST, errorMsg);
    }
}
