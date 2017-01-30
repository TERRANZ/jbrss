package ru.terra.jbrss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import ru.terra.jbrss.config.AccountsWebApplicationConfiguration;

@EnableAutoConfiguration
@EnableDiscoveryClient
@Import(AccountsWebApplicationConfiguration.class)
public class AccountApplication {

    public static void main(String[] args) {
        System.setProperty("spring.config.name", "accounts-server");
        SpringApplication.run(AccountApplication.class, args);
    }
}
