package ru.terra.jbrss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ImappApplication {

    public static void main(String[] args) {
        System.setProperty("spring.config.name", "im-server");
        SpringApplication.run(ImappApplication.class, args);
    }
}
