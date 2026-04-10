package by.itbatia.psp.personservice.exception;

import by.itbatia.psp.common.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.util.BindErrorUtils;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleException(MethodArgumentNotValidException exception) {
        log.error(exception.getMessage(), exception);
        String errorMsg = BindErrorUtils.resolveAndJoin(exception.getBindingResult().getFieldErrors());

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(buildErrorResponse(errorMsg));
    }

    private static ErrorResponse buildErrorResponse(ApiException exception) {
        return buildResponse(exception.getMessage(), exception.getHttpStatus());
    }

    private static ErrorResponse buildErrorResponse(String errorMsg) {
        return buildResponse(errorMsg, HttpStatus.BAD_REQUEST);
    }

    private static ErrorResponse buildResponse(String errorMsg, HttpStatus httpStatus) {
        ErrorResponse response = new ErrorResponse();

        response.setError(errorMsg);
        response.setStatus(httpStatus.value());

        return response;
    }
}
