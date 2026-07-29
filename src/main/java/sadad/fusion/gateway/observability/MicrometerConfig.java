package sadad.fusion.gateway.observability;

import io.micrometer.core.instrument.Tags;
import org.springframework.cloud.gateway.support.tagsprovider.GatewayTagsProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;

@Configuration
public class MicrometerConfig {
    /**
     * to provide custom tags for routed requests metrics at spring_cloud_gateway_requests_seconds
     */
    //@Bean
    public GatewayTagsProvider gatewayTagsProvider() {
        return serverWebExchange ->  {
            ServerHttpRequest request = serverWebExchange.getRequest();
            return Tags.of("path", request.getURI().getPath());
        };
    }

}
