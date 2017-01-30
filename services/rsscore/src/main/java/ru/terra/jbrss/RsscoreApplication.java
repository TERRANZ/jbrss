package ru.terra.jbrss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RsscoreApplication {

	public static void main(String[] args) {
		System.setProperty("spring.config.name", "rss-server");
		SpringApplication.run(RsscoreApplication.class, args);
	}
}
