package by.itbatia.psp.individualsapi.util;

import by.itbatia.individualsapi.dto.TokenRefreshRequest;
import lombok.experimental.UtilityClass;

/**
 * @author Batsian_SV
 */
@UtilityClass
public class TokenRefreshRequestUtil {

    public static TokenRefreshRequest build(String refreshToken) {
        return buildTokenRefreshRequest(refreshToken);
    }

    public static TokenRefreshRequest build() {
        return buildTokenRefreshRequest("fakeRefresh");
    }

    private static TokenRefreshRequest buildTokenRefreshRequest(String refreshToken) {
        TokenRefreshRequest tokenResponse = new TokenRefreshRequest();

        tokenResponse.setRefreshToken(refreshToken);

        return tokenResponse;
    }
}
