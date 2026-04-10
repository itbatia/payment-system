package by.itbatia.psp.individualsapi.service.impl;

import by.itbatia.psp.common.dto.IndividualCreateRequest;
import by.itbatia.psp.common.dto.UserCreateRequest;
import by.itbatia.psp.individualsapi.dto.TokenRefreshRequest;
import by.itbatia.psp.individualsapi.dto.UserLoginRequest;
import by.itbatia.psp.individualsapi.exception.api.BadRequestApiException;
import by.itbatia.psp.individualsapi.service.MetricsService;
import by.itbatia.psp.individualsapi.service.RestValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Batsian_SV
 */
@Service
@RequiredArgsConstructor
public class RestValidationServiceImpl implements RestValidationService {

    private static final String ERROR_MSG = "%s field is required";
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private final MetricsService metricsService;

    @Override
    public void validate(IndividualCreateRequest request) throws BadRequestApiException {
        try {
            checkForNull(request);
            UserCreateRequest user = request.getUser();
            checkForNull(user);
            checkForNull(user.getEmail(), "Email");
            checkForNull(user.getPassword(), "Password");
            checkForNull(user.getConfirmPassword(), "ConfirmPassword");
            checkPasswordsMatches(user);
            checkEmail(user.getEmail());

        } catch (Exception exception) {
            metricsService.incrementFailedRegistration();
            throw exception;
        }
    }

    @Override
    public void validate(UserLoginRequest request) throws BadRequestApiException {
        try {
            checkForNull(request);
            checkForNull(request.getEmail(), "Email");
            checkForNull(request.getPassword(), "Password");
            checkEmail(request.getEmail());

        } catch (Exception exception) {
            metricsService.incrementFailedLogin();
            throw exception;
        }
    }

    @Override
    public void validate(TokenRefreshRequest request) throws BadRequestApiException {
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

    private static void checkPasswordsMatches(UserCreateRequest user) throws BadRequestApiException {
        if (user.getPassword().equals(user.getConfirmPassword())) {
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
