package by.itbatia.psp.individualsapi.config;

import by.itbatia.psp.individualsapi.enums.Meter;
import by.itbatia.psp.individualsapi.enums.MeterTags;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Batsian_SV
 */
@Configuration
public class MetricsConfig {

    @Bean
    public MeterBinder meterBinder() {
        return meterRegistry -> {

            Counter.builder(Meter.LOGIN_TOTAL.getName())
                .description(Meter.LOGIN_TOTAL.getDescription())
                .tags(MeterTags.SUCCESS.getTags())
                .tags(MeterTags.FAIL.getTags())
                .register(meterRegistry);

            Counter.builder(Meter.REGISTRATION_TOTAL.getName())
                .description(Meter.REGISTRATION_TOTAL.getDescription())
                .tags(MeterTags.SUCCESS.getTags())
                .tags(MeterTags.FAIL.getTags())
                .register(meterRegistry);
        };
    }
}
