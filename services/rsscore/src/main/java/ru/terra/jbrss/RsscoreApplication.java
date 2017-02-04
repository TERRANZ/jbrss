package ru.terra.jbrss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import ru.terra.jbrss.config.RssApplicationConfiguration;

@SpringBootApplication
@EnableAutoConfiguration
@EnableDiscoveryClient
@Import(RssApplicationConfiguration.class)
public class RsscoreApplication {

    public static void main(String[] args) {
        System.setProperty("spring.config.name", "rss-server");
        SpringApplication.run(RsscoreApplication.class, args);
    }
}
