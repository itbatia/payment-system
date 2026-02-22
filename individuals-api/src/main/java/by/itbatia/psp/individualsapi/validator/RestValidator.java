package by.itbatia.psp.individualsapi.validator;

import by.itbatia.individualsapi.dto.TokenRefreshRequest;
import by.itbatia.individualsapi.dto.UserLoginRequest;
import by.itbatia.individualsapi.dto.UserRegistrationRequest;
import by.itbatia.psp.individualsapi.exception.api.BadRequestApiException;
import lombok.experimental.UtilityClass;

/**
 * @author Batsian_SV
 */
@UtilityClass
public class RestValidator {

    private static final String ERROR_MSG = "%s field is required";
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    public static void validate(UserRegistrationRequest request) throws BadRequestApiException {
        checkForNull(request);
        checkForNull(request.getEmail(), "Email");
        checkForNull(request.getEmail(), "Password");
        checkForNull(request.getEmail(), "ConfirmPassword");
        checkPasswordsMatches(request);
        checkEmail(request.getEmail());
    }

    public static void validate(UserLoginRequest request) throws BadRequestApiException {
        checkForNull(request);
        checkForNull(request.getEmail(), "Email");
        checkForNull(request.getEmail(), "Password");
        checkEmail(request.getEmail());
    }

    public static void validate(TokenRefreshRequest request) throws BadRequestApiException {
        checkForNull(request);
        checkForNull(request.getRefreshToken(), "RefreshToken");
    }

    private static void checkForNull(Object request) throws BadRequestApiException {
        if (request == null) {
            throw new BadRequestApiException("Request is null");
        }
    }

    private static void checkForNull(String value, String fieldName) throws BadRequestApiException {
        if (isBlank(value)) {
            throw new BadRequestApiException(String.format(ERROR_MSG, fieldName));
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

    private static void checkEmail(String email) throws BadRequestApiException {
        if (email.matches(EMAIL_PATTERN)) {
            return;
        }
        throw new BadRequestApiException("Email must be a valid email address");
    }
}
