package by.itbatia.psp.individualsapi.util;

import by.itbatia.individualsapi.dto.UserRegistrationRequest;
import lombok.experimental.UtilityClass;

/**
 * @author Batsian_SV
 */
@UtilityClass
public class UserRegistrationRequestUtil {

    public static UserRegistrationRequest build(String email, String password) {
        return buildUserRegistrationRequest(email, password, password);
    }

    public static UserRegistrationRequest build(String email, String password, String confirmPassword) {
        return buildUserRegistrationRequest(email, password, confirmPassword);
    }

    private static UserRegistrationRequest buildUserRegistrationRequest(String email, String password, String confirmPassword) {
        UserRegistrationRequest request = new UserRegistrationRequest();

        request.setEmail(email);
        request.setPassword(password);
        request.setConfirmPassword(confirmPassword);

        return request;
    }
}
