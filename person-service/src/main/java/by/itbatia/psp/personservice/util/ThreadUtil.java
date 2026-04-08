package by.itbatia.psp.personservice.util;

import java.util.UUID;

import lombok.experimental.UtilityClass;

/**
 * @author Batsian_SV
 */
@UtilityClass
public class ThreadUtil {

    public static void setThreadName(String threadName) {
        if (threadName != null) {
            Thread.currentThread().setName(threadName);
        }
    }

    public static void setThreadName(UUID uid) {
        if (uid != null) {
            Thread.currentThread().setName(uid.toString());
        }
    }
}
