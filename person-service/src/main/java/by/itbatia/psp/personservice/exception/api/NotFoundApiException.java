package by.itbatia.psp.personservice.exception.api;

import by.itbatia.psp.personservice.exception.ApiException;
import org.springframework.http.HttpStatus;

/**
 * @author Batsian_SV
 */
public class NotFoundApiException extends ApiException {

    public NotFoundApiException(String errorMsg) {
        super(HttpStatus.NOT_FOUND, errorMsg);
    }
}
