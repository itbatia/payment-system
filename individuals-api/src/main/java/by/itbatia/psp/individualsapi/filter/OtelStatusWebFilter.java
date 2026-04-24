package by.itbatia.psp.individualsapi.filter;

import by.itbatia.psp.individualsapi.util.SpanUtil;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author Batsian_SV
 */
@Component
@RequiredArgsConstructor
public class OtelStatusWebFilter implements WebFilter {

    private final Tracer tracer;

    /**
     * Устанавливает официальный статус OpenTelemetry-спана на основе HTTP-статуса.
     * <p>
     * Важно различать:
     * <ul>
     *   <li><b>Атрибут {@code http.status}</b> — кастомный тег спана (например: 409),</li>
     *   <li><b>Официальный статус спана ({@code io.opentelemetry.api.trace.StatusCode})</b> — это поле OpenTelemetry объекта
     *   ({@code io.opentelemetry.api.trace.Span}) со значениями {@code OK}, {@code ERROR} или {@code UNSET}.</li>
     * </ul>
     * Grafana/Tempo отображают именно официальный статус спана в водопаде трассировок.
     */
    @Override
    public @NonNull Mono<Void> filter(@NonNull ServerWebExchange exchange, WebFilterChain chain) {
        return chain.filter(exchange)
            .onErrorResume(throwable -> {
                Span span = tracer.currentSpan();
                if (span != null) {
                    span.error(throwable);
                }
                return Mono.error(throwable);
            })
            .doFinally(signalType -> {
                HttpStatusCode statusCode = exchange.getResponse().getStatusCode();

                if (statusCode != null && statusCode.isError()) {
                    Span span = tracer.currentSpan();

                    if (span != null) {
                        span.tag(SpanUtil.HTTP_STATUS_TAG, statusCode.value());
                    }
                }
            });
    }
}
