package by.itbatia.psp.individualsapi.validator;

import by.itbatia.individualsapi.dto.TokenRefreshRequest;
import by.itbatia.individualsapi.dto.UserRegistrationRequest;
import by.itbatia.psp.individualsapi.exception.api.BadRequestApiException;
import lombok.experimental.UtilityClass;

/**
 * @author Batsian_SV
 */
@UtilityClass
public class RestValidator {

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    public static void validate(UserRegistrationRequest request) throws BadRequestApiException {
        checkForNull(request);
        checkPasswordsMatches(request);
        checkEmail(request);
    }

    public static void validate(TokenRefreshRequest request) throws BadRequestApiException {
        if (request == null) {
            throw new BadRequestApiException("Request is null");
        }
        if (isBlank(request.getRefreshToken())) {
            throw new BadRequestApiException("Refresh token is required");
        }
    }

    private static void checkForNull(UserRegistrationRequest request) throws BadRequestApiException {
        if (request == null) {
            throw new BadRequestApiException("Request is null");
        }
        if (isBlank(request.getEmail())) {
            throw new BadRequestApiException("Email is not set");
        }
        if (isBlank(request.getPassword())) {
            throw new BadRequestApiException("Password is not set");
        }
        if (isBlank(request.getConfirmPassword())) {
            throw new BadRequestApiException("ConfirmPassword is not set");
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.isEmpty();
    }

    private static void checkPasswordsMatches(UserRegistrationRequest request) throws BadRequestApiException {
        if (request.getPassword().equals(request.getConfirmPassword())) {
            return;
        }
        throw new BadRequestApiException("Password and confirmation do not match");
    }

    private static void checkEmail(UserRegistrationRequest request) throws BadRequestApiException {
        if (request.getEmail().matches(EMAIL_PATTERN)) {
            return;
        }
        throw new BadRequestApiException("Email is not valid");
    }
}
