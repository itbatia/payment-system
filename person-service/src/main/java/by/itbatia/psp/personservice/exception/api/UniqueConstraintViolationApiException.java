package by.itbatia.psp.personservice.exception.api;

import by.itbatia.psp.personservice.exception.ApiException;
import org.springframework.http.HttpStatus;

/**
 * @author Batsian_SV
 */
public class UniqueConstraintViolationApiException extends ApiException {

    public UniqueConstraintViolationApiException(String errorMsg, Exception exception) {
        super(HttpStatus.CONFLICT, errorMsg, exception);
    }
}
