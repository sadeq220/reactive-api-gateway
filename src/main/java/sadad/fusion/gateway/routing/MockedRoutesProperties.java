package sadad.fusion.gateway.routing;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.config.GatewayProperties;

@ConfigurationProperties(prefix = "gateway.mocked")
public class MockedRoutesProperties extends GatewayProperties {
}
