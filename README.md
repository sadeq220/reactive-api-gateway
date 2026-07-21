### Spring Cloud Gateway Server WebFlux
Spring Cloud Gateway matches routes as part of the Spring WebFlux `HandlerMapping` infrastructure.    
Clients make requests to Spring Cloud Gateway. If the `Gateway Handler Mapping` determines that a request matches a route,    
it is sent to the `Gateway Web Handler`.    
This handler runs the request through a filter chain that is specific to the request.    


Configuration for Spring Cloud Gateway is driven by a **collection** of `RouteDefinitionLocator` instances.   
The following listing shows the definition of the RouteDefinitionLocator interface:
```Java
public interface RouteDefinitionLocator {
	Flux<RouteDefinition> getRouteDefinitions();
}
```

### Metrics
Spring Webflux register observation beans in `WebFluxObservationAutoConfiguration` class and register bean 
`DefaultServerRequestObservationConvention` that registers the **http_server_requests_seconds_count** metrics for incoming HTTP requests,    
*DefaultServerRequestObservationConvention* works with **ServerRequestObservationContext** object that is in the Exchange attributes.   

Spring Cloud Gateway registers **spring_cloud_gateway_requests_seconds_sum** metrics.     
**org.springframework.cloud.gateway.filter.GatewayMetricsFilter** register the above summary metric using micrometer Timer meter.    
set *spring.cloud.gateway.server.webflux.metrics.path-tags.enabled* property to add **path** tag to the above metrics.     

`Low cardinality values`: are for data with a small, bounded set of possible values. They are added to both metrics and traces.
`High cardinality values`: are for data with a large or unbounded set of possible values.    
They are added only to traces, logs, and other systems designed to handle high-cardinality data, but crucially, not to metrics.    

### References
- [Spring Cloud Gateway doc](https://docs.spring.io/spring-cloud-gateway/reference/spring-cloud-gateway-server-webflux/)