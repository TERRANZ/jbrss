package ru.terra.jbrss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import ru.terra.jbrss.config.RssApplicationConfiguration;
import ru.terra.jbrss.rss.RssCore;

@SpringBootApplication
@EnableAutoConfiguration
@EnableDiscoveryClient
@Import(RssApplicationConfiguration.class)
public class RsscoreApplication {

    public static void main(String[] args) {
        System.setProperty("spring.config.name", "rss-server");
        ConfigurableApplicationContext context = SpringApplication.run(RsscoreApplication.class, args);
        context.getBean(RssCore.class).start();
    }
}
