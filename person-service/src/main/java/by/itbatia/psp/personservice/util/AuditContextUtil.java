package by.itbatia.psp.personservice.util;

import lombok.experimental.UtilityClass;

/**
 * @author Batsian_SV
 */
@UtilityClass
public class AuditContextUtil {

    private static final ThreadLocal<String> INITIATOR = new ThreadLocal<>();

    public static void set(String initiator) {
        INITIATOR.set(initiator);
    }

    public static String get() {
        return INITIATOR.get();
    }

    public static void clear() {
        INITIATOR.remove();
    }
}
