package by.itbatia.psp.individualsapi.exception;

import by.itbatia.psp.common.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientRequestException;

/**
 * @author Batsian_SV
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleException(ApiException exception) {
        log.error(exception.getMessage(), exception);

        ErrorResponse response = buildErrorResponse(exception);
        return ResponseEntity.status(exception.getHttpStatus()).body(response);
    }

    @ExceptionHandler(WebClientRequestException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleException(WebClientRequestException exception) {
        log.error(exception.getMessage(), exception);

        ErrorResponse response = buildErrorResponse(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    private static ErrorResponse buildErrorResponse(ApiException exception) {
        return buildErrorResponse(exception.getMessage(), exception.getHttpStatus());
    }

    private static ErrorResponse buildErrorResponse(String errorMsg, HttpStatus httpStatus) {
        ErrorResponse response = new ErrorResponse();

        response.setError(errorMsg);
        response.setStatus(httpStatus.value());

        return response;
    }
}
