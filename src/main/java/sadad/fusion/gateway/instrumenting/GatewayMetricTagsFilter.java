package sadad.fusion.gateway.instrumenting;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.observation.ServerRequestObservationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class GatewayMetricTagsFilter implements GlobalFilter, Ordered {
    private final CustomMetrics customMetrics;

    public GatewayMetricTagsFilter(CustomMetrics customMetrics) {
        this.customMetrics = customMetrics;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            customMetrics.recordOnCommit(exchange);
            // populate uri tag
            /*
             * request-path may contain path-variable it can bloat the Prometheus with so many time-series
                ServerRequestObservationContext.findCurrent(exchange.getAttributes())
                        .ifPresent(context -> context.setPathPattern(exchange.getRequest().getPath().toString()));
             *
             */
        }));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE; // run early that no routing has happened, and the Exchange is still the original Exchange
    }
}
