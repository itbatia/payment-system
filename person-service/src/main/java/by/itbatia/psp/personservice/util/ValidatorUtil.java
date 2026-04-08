package by.itbatia.psp.personservice.util;

import java.util.Objects;

import lombok.experimental.UtilityClass;

/**
 * @author Batsian_SV
 */
@UtilityClass
public class ValidatorUtil {

    public static boolean nonNull(Object... fields) {
        for (Object field : fields) {
            if (Objects.isNull(field)) {
                return false;
            }
        }
        return true;
    }
}
