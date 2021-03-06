package ru.terra.jbrss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Import;
import ru.terra.jbrss.config.AccountsWebApplicationConfiguration;

@EnableAutoConfiguration
@EnableEurekaClient
@Import(AccountsWebApplicationConfiguration.class)
@SpringBootApplication
public class AccountApplication {

    public static void main(String[] args) {
        System.setProperty("spring.config.name", "accounts-server");
        SpringApplication.run(AccountApplication.class, args);
    }
}
