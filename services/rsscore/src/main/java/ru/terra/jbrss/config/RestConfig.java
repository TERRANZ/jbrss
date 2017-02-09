package ru.terra.jbrss.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

import java.util.ArrayList;
import java.util.List;

@EnableOAuth2Client
@Configuration
public class RestConfig {
    @Value("${oauth.resource:http://localhost:2222}")
    private String baseUrl;
    @Value("${oauth.authorize:http://localhost:2222/acc/oauth/authorize}")
    private String authorizeUrl;
    @Value("${oauth.token:http://localhost:2222/acc/oauth/token}")
    private String tokenUrl;

    @Bean
    protected OAuth2ProtectedResourceDetails resource() {
        ClientCredentialsResourceDetails resource = new ClientCredentialsResourceDetails();
        List<String> scopes = new ArrayList<>(2);
        scopes.add("rest");
        resource.setAccessTokenUri(tokenUrl);
        resource.setClientId("restapp");
        resource.setClientSecret("restapp");
        resource.setGrantType("client_credentials");
        resource.setScope(scopes);
        return resource;
    }

    @Bean
    public OAuth2RestOperations restTemplate() {
        return new OAuth2RestTemplate(resource(), new DefaultOAuth2ClientContext(new DefaultAccessTokenRequest()));
    }
}
