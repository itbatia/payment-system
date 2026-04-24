package by.itbatia.psp.personservice.filter;

import by.itbatia.psp.personservice.util.SpanUtil;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author Batsian_SV
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OtelStatusWebFilter extends OncePerRequestFilter {

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
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) throws ServletException, IOException {

        log.info("Request on [url={}]", request.getRequestURI());

        try {
            chain.doFilter(request, response);

            if (response.getStatus() >= 400) {
                Span span = tracer.currentSpan();
                if (span != null) {
                    span.tag(SpanUtil.HTTP_STATUS_TAG, response.getStatus());
                }
            }
        } catch (Exception exception) {
            Span span = tracer.currentSpan();
            if (span != null) {
                span.error(exception);
            }
            throw exception;
        }
    }
}
