package ru.terra.dms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import ru.terra.dms.config.DmsAppConfiguration;

@SpringBootApplication
@EnableAutoConfiguration
@EnableDiscoveryClient
@Import(DmsAppConfiguration.class)
public class DmsApplication {

    public static void main(String[] args) {
        System.setProperty("spring.config.name", "dms-server");
        ConfigurableApplicationContext context = SpringApplication.run(DmsApplication.class, args);
    }
}
