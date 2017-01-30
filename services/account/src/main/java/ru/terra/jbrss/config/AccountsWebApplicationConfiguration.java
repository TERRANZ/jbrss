package ru.terra.jbrss.config;

import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EntityScan("ru.terra.jbrss")
@EnableJpaRepositories("ru.terra.jbrss")
@PropertySource("classpath:db-config.properties")
public class AccountsWebApplicationConfiguration {
}
