package ru.terra.jbrss.config;


import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@EntityScan("ru.terra.jbrss")
@EnableJpaRepositories("ru.terra.jbrss")
@PropertySource("classpath:db-config.properties")
@EnableResourceServer
public class RssApplicationConfiguration {
}
