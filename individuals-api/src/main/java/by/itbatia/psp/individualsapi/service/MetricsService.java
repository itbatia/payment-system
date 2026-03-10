package by.itbatia.psp.individualsapi.service;

import by.itbatia.psp.individualsapi.enums.Meter;
import io.micrometer.core.instrument.Timer;

/**
 * @author Batsian_SV
 */
public interface MetricsService {

    void incrementSuccessfulLogin();

    void incrementFailedLogin();

    void incrementSuccessfulRegistration();

    void incrementFailedRegistration();

    Timer.Sample startTimer();

    void stopTimerOnSuccess(Timer.Sample timer, Meter meter);

    void stopTimerOnError(Timer.Sample timer, Meter meter);
}
