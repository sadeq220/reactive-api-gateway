package sadad.fusion.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import sadad.fusion.gateway.routing.YamlRegisterer;

@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(GatewayApplication.class);
		springApplication.addInitializers(new YamlRegisterer());
		springApplication.run(args);
	}

}
