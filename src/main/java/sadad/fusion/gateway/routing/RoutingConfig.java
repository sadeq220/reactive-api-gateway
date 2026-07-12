package sadad.fusion.gateway.routing;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.config.PropertiesRouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * customization of GatewayAutoConfiguration
 */
@Configuration
@ConfigurationPropertiesScan(basePackageClasses = MockedRoutesProperties.class)
public class RoutingConfig {
    /**
     * many RouteDefinitionLocator bean can be defined, e.g. RedisRouteDefinitionLocator
     * all RouteDefinitionLocator beans are aggregated into one RouteDefinitionLocator bean
     * @return RouteDefinitionLocator
     */
    @Bean
    public PropertiesRouteDefinitionLocator propertySourceRouteDefinitionLocator(MockedRoutesProperties gatewayProperties){
        return new PropertiesRouteDefinitionLocator(gatewayProperties);
    }

    /**
     * include DefaultFilters and RouteDefinitions and PredicateDefinitions
     */
    @Bean
    @ConfigurationProperties(prefix = "gateway")
    @Primary
    public GatewayProperties customeGatewayProperties(){
        return new GatewayProperties(){};
    }
}
