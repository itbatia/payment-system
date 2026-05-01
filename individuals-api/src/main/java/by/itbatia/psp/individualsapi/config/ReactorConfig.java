package by.itbatia.psp.individualsapi.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Hooks;

/**
 * @author Batsian_SV
 */
@Configuration
public class ReactorConfig {

    /**
     * Enable MDC propagation for logs: [traceId,spanId]<br>
     * Note: in Spring WebMvc uses logback-spring.xml -> include MDC Key
     */
    @PostConstruct
    public void init() {
        Hooks.enableAutomaticContextPropagation();
    }
}
