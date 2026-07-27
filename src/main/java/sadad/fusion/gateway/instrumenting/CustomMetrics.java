package sadad.fusion.gateway.instrumenting;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class CustomMetrics {
    private final MeterRegistry meterRegistry;


    public CustomMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void recordOnCommit(ServerWebExchange serverWebExchange){
        if (serverWebExchange.getResponse().isCommitted()){
         this.recordErrorIfApplicable(serverWebExchange);
        } else {
            serverWebExchange.getResponse().beforeCommit(() -> {
                this.recordErrorIfApplicable(serverWebExchange);
                return Mono.empty();
            });
        }
    }
    private void recordErrorIfApplicable(ServerWebExchange exchange){
        HttpStatusCode statusCode = exchange.getResponse().getStatusCode();
        if (statusCode == null) {
            return;
        }
        boolean isError = statusCode.is4xxClientError() || statusCode.is5xxServerError();
        if (!isError) {
            return;
        }
        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        /*
         * Micrometer deduplicates by name+tags internally,
         * so calling this on every request is safe and cheap; it won't create duplicate counters for the same tag combination.
         * The lookup cost is just a single hash lookup, so it is acceptable for most use cases.
         */
        Counter.builder("api.gateway.routed.error")
                .description("counts all routed requests with 4xx or 5xx response")
                .tag("method", exchange.getRequest().getMethod().name())
                .tag("status", String.valueOf(statusCode.value()))
                .tag("route", route != null ? route.getId() : "unknown")
                .register(meterRegistry)
                .increment();

    }

}
