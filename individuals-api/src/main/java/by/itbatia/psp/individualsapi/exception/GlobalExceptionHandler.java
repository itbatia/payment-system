package by.itbatia.psp.individualsapi.exception;

import by.itbatia.individualsapi.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Batsian_SV
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleException(ApiException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity
            .status(exception.getHttpStatus())
            .body(buildErrorResponse(exception));
    }

    private static ErrorResponse buildErrorResponse(ApiException exception) {
        ErrorResponse response = new ErrorResponse();

        response.setError(exception.getMessage());
        response.setStatus(exception.getHttpStatus().value());

        return response;
    }
}
