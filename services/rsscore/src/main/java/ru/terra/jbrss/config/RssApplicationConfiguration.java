package ru.terra.jbrss.config;


import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@EntityScan("ru.terra.jbrss")
//@EnableJpaRepositories("ru.terra.jbrss")
@PropertySource("classpath:db-config.properties")
@EnableResourceServer
@EnableEurekaClient
public class RssApplicationConfiguration {
}
