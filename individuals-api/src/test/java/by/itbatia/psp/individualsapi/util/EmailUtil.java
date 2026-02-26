package by.itbatia.psp.individualsapi.util;

import java.util.UUID;

import lombok.experimental.UtilityClass;

/**
 * @author Batsian_SV
 */
@UtilityClass
public class EmailUtil {

    public static String generateUniqueEmail() {
        return UUID.randomUUID() + "@test.com";
    }
}
