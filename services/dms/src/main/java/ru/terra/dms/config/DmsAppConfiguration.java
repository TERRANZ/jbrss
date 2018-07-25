package ru.terra.dms.config;


import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan("ru.terra.dms")
@EnableJpaRepositories("ru.terra.dms")
@PropertySource("classpath:db-config.properties")
public class DmsAppConfiguration {
}
