package ru.terra.jbrss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import ru.terra.jbrss.config.ImAppConfiguration;

@SpringBootApplication
@EnableAutoConfiguration
@EnableDiscoveryClient
@Import(ImAppConfiguration.class)
public class ImappApplication {

    public static void main(String[] args) {
        System.setProperty("spring.config.name", "im-server");
        SpringApplication.run(ImappApplication.class, args);
    }
}
