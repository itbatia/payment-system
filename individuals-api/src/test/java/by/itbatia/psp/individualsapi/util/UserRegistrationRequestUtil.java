package by.itbatia.psp.individualsapi.util;

import by.itbatia.psp.individualsapi.dto.UserRegistrationRequest;
import lombok.experimental.UtilityClass;

import java.util.UUID;

/**
 * @author Batsian_SV
 */
@UtilityClass
public class UserRegistrationRequestUtil {

    public static UserRegistrationRequest build(String email, String password) {
        return buildUserRegistrationRequest(email, password, password);
    }

    private static UserRegistrationRequest buildUserRegistrationRequest(String email, String password, String confirmPassword) {
        return UserRegistrationRequest.builder()
            .userId(UUID.randomUUID())
            .email(email)
            .password(password)
            .confirmPassword(confirmPassword)
            .build();
    }
}
