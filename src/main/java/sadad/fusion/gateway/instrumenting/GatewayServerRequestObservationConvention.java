package sadad.fusion.gateway.instrumenting;

import io.micrometer.common.KeyValue;
import io.micrometer.common.KeyValues;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.observation.DefaultServerRequestObservationConvention;
import org.springframework.http.server.reactive.observation.ServerRequestObservationContext;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * To add custom Tags to Metrics
 */
//@Component
public class GatewayServerRequestObservationConvention
        extends DefaultServerRequestObservationConvention {

    @Override
    public KeyValues getLowCardinalityKeyValues(ServerRequestObservationContext context) {
        KeyValues keyValues = super.getLowCardinalityKeyValues(context);
        ServerHttpRequest exchange = context.getCarrier();

        if (exchange != null) {
            String requestGroup = exchange.getHeaders().getFirst("X-Request-Group") ;
            keyValues = keyValues.and(KeyValue.of("group", String.valueOf(requestGroup)));

        }
        return keyValues;
    }
}