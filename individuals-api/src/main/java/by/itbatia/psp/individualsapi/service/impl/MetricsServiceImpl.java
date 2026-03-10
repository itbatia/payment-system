package by.itbatia.psp.individualsapi.service.impl;

import by.itbatia.psp.individualsapi.enums.Meter;
import by.itbatia.psp.individualsapi.enums.MeterTags;
import by.itbatia.psp.individualsapi.service.MetricsService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Batsian_SV
 */
@Service
@RequiredArgsConstructor
public class MetricsServiceImpl implements MetricsService {

    private final MeterRegistry meterRegistry;

    @Override
    public void incrementSuccessfulLogin() {
        meterRegistry.counter(
            Meter.LOGIN_TOTAL.getName(),
            MeterTags.SUCCESS.getTags()
        ).increment();
    }

    @Override
    public void incrementFailedLogin() {
        meterRegistry.counter(
            Meter.LOGIN_TOTAL.getName(),
            MeterTags.FAIL.getTags()
        ).increment();
    }

    @Override
    public void incrementSuccessfulRegistration() {
        meterRegistry.counter(
            Meter.REGISTRATION_TOTAL.getName(),
            MeterTags.SUCCESS.getTags()
        ).increment();
    }

    @Override
    public void incrementFailedRegistration() {
        meterRegistry.counter(
            Meter.REGISTRATION_TOTAL.getName(),
            MeterTags.FAIL.getTags()
        ).increment();
    }

    @Override
    public Timer.Sample startTimer() {
        return Timer.start(meterRegistry);
    }

    @Override
    public void stopTimerOnSuccess(Timer.Sample timer, Meter meter) {
        timer.stop(registerTimer(meter, MeterTags.SUCCESS));
    }

    @Override
    public void stopTimerOnError(Timer.Sample timer, Meter meter) {
        timer.stop(registerTimer(meter, MeterTags.FAIL));
    }

    private Timer registerTimer(Meter meter, MeterTags tags) {
        return Timer.builder(meter.getName())
            .description(meter.getDescription())
            .tags(tags.getTags())
            .register(meterRegistry);
    }
}
