package by.itbatia.psp.individualsapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {

    private final HttpStatus httpStatus;

    public ApiException(HttpStatus httpStatus, String errorMsg) {
        super(errorMsg);
        this.httpStatus = httpStatus;
    }
}
