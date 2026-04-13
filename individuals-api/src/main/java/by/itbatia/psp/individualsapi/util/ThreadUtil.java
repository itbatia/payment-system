package by.itbatia.psp.individualsapi.util;

import java.util.UUID;

import by.itbatia.psp.common.dto.IndividualUpdateRequest;
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

    public static void setThreadName(IndividualUpdateRequest request) {
        if (request.getId() != null) {
            Thread.currentThread().setName(request.getId().toString());
        }
    }
}
