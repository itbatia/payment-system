package by.itbatia.psp.individualsapi.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Batsian_SV
 */
@Getter
@RequiredArgsConstructor
public enum Meter {

    LOGIN_TOTAL("login_total", "Total number of login attempts"),
    REGISTRATION_TOTAL("registration_total", "Total number of registration attempts"),
    KC_LOGIN_LATENCY("kc_login_latency_seconds", "Duration of user authentication via Keycloak"),
    KC_REGISTRATION_LATENCY("kc_registration_latency_seconds", "Duration of user registration via Keycloak");

    private final String name;
    private final String description;
}
