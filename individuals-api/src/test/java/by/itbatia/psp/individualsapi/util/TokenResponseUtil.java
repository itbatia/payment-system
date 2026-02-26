package by.itbatia.psp.individualsapi.util;

import by.itbatia.individualsapi.dto.TokenResponse;
import lombok.experimental.UtilityClass;

/**
 * @author Batsian_SV
 */
@UtilityClass
public class TokenResponseUtil {

    public static TokenResponse build() {
        TokenResponse tokenResponse = new TokenResponse();

        tokenResponse.setAccessToken("fakeAccess");
        tokenResponse.setRefreshToken("fakeRefresh");
        tokenResponse.setExpiresIn(3600);
        tokenResponse.setTokenType("Bearer");

        return tokenResponse;
    }
}
