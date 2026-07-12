### Spring Cloud Gateway Server WebFlux
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


### References
- [Spring Cloud Gateway doc](https://docs.spring.io/spring-cloud-gateway/reference/spring-cloud-gateway-server-webflux/)