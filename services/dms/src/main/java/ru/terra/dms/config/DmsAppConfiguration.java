package ru.terra.dms.config;


import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@EntityScan("ru.terra.dms")
//@EnableJpaRepositories("ru.terra.dms")
@PropertySource("classpath:db-config.properties")
@EnableResourceServer
@EnableEurekaClient
public class DmsAppConfiguration {
}
