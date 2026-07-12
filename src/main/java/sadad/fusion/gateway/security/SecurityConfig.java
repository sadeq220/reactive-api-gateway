package sadad.fusion.gateway.security;

import com.nimbusds.jose.crypto.MACVerifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.DefaultServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;


@EnableWebFluxSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain createSecurityFilterChain(ServerHttpSecurity httpSecurity){
        return httpSecurity.csrf(csrf-> csrf.disable())
                .oauth2Login(oAuth2LoginSpec -> {
                    oAuth2LoginSpec.loginPage("/sample-login");
                    oAuth2LoginSpec.authenticationMatcher(new PathPatternParserServerWebExchangeMatcher("/login/oauth2/code/"));
                    oAuth2LoginSpec.authorizationRequestResolver(authorizationRequestResolver());
                })
                .build();
    }
    @Bean
    public ReactiveClientRegistrationRepository clientRegistrationRepository(){
        return new InMemoryReactiveClientRegistrationRepository(keycloakClientRegistration());
    }
    public ClientRegistration keycloakClientRegistration(){
        return ClientRegistration.withRegistrationId("keycloak")//client code
                .clientId("sadad_api-gateway")
                .clientSecret("1zYDtZsB1EFeR6FTXTfRZBMHECCq6jgE")
                .redirectUri("{baseUrl}/login/oauth2/code/")
                .scope("openid","profile") // improve to OIDC1, which provides id_token in addition to access_token
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)//Authorization code flow grant type
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                // fetching all necessary uri(authorization_uri, token_uri, userinfo_uri, jwks_uri) through OIDC spec
                // from http(s)://<your-keycloak-host>/realms/<your-realm-name>/.well-known/openid-configuration
                .issuerUri("http://localhost:8080/realms/sadad")// OpenID Connect Discovery standard

                // Explicit endpoints - no discovery needed
                .authorizationUri("http://localhost:8080/realms/sadad/protocol/openid-connect/auth")
                .tokenUri("http://localhost:8080/realms/sadad/protocol/openid-connect/token")
                .userInfoUri("http://localhost:8080/realms/sadad/protocol/openid-connect/userinfo")
                .jwkSetUri("http://localhost:8080/realms/sadad/protocol/openid-connect/certs")

                // userInfo endpoint response field name
                .userNameAttributeName("preferred_username")
                .build();
    }
    @Bean
    public ServerOAuth2AuthorizationRequestResolver authorizationRequestResolver(){
        DefaultServerOAuth2AuthorizationRequestResolver defaultServerOAuth2AuthorizationRequestResolver =
                new DefaultServerOAuth2AuthorizationRequestResolver(clientRegistrationRepository(),
                        new PathPatternParserServerWebExchangeMatcher("/oauth2/authorization/{registrationId}"));
        defaultServerOAuth2AuthorizationRequestResolver.setAuthorizationRequestCustomizer(builder -> {

        });
        return defaultServerOAuth2AuthorizationRequestResolver;
    }
}
