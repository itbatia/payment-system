package by.itbatia.psp.individualsapi.filter;

import by.itbatia.psp.individualsapi.util.RestUtil;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author Batsian_SV
 */
@Slf4j
@Component
public class LoggingWebFilter implements WebFilter {

    @Override
    public @NonNull Mono<Void> filter(@NonNull ServerWebExchange exchange,
                                      @NonNull WebFilterChain chain) {

        String url = exchange.getRequest().getPath().value();

        return RestUtil.getPrincipalUserId()
            .flatMap(userId -> {
                log.debug("Request received on [url={}] from [userId={}]", url, userId);
                return chain.filter(exchange);
            })
            .switchIfEmpty(Mono.defer(() -> {
                log.debug("Request received on [url={}]", url);
                return chain.filter(exchange);
            }));
    }
}
