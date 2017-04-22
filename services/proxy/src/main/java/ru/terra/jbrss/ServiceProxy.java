package ru.terra.jbrss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
@EnableAutoConfiguration
@EnableEurekaClient
public class ServiceProxy {

    public static void main(String[] args) {
        System.setProperty("spring.config.name", "proxy-server");
        SpringApplication.run(ServiceProxy.class, args);
    }
}
