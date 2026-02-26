package by.itbatia.psp.individualsapi.util;

import by.itbatia.individualsapi.dto.UserLoginRequest;
import lombok.experimental.UtilityClass;

/**
 * @author Batsian_SV
 */
@UtilityClass
public class UserLoginRequestUtil {

    public static UserLoginRequest build(String email, String password) {
        UserLoginRequest request = new UserLoginRequest();

        request.setEmail(email);
        request.setPassword(password);

        return request;
    }
}
