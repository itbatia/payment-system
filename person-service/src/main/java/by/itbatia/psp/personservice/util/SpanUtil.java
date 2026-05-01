package by.itbatia.psp.personservice.util;

import lombok.experimental.UtilityClass;

/**
 * @author Batsian_SV
 * @apiNote Custom tags for searching and filtering traces:<br>
 * Grafana →  Explore → Tempo → Search → Tags → Select tags
 */
@UtilityClass
public class SpanUtil {

    public static final String HTTP_STATUS_TAG = "http.status";
}
