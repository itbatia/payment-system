package by.itbatia.psp.personservice.filter;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author Batsian_SV
 */
@Component
@RequiredArgsConstructor
public class TraceIdResponseFilter extends OncePerRequestFilter {

    private final Tracer tracer;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        Span currentSpan = tracer.currentSpan();

        if (currentSpan != null) {
            String traceId = currentSpan.context().traceId();
            response.setHeader("X-Trace-Id", traceId);
        }
        filterChain.doFilter(request, response);
    }
}
