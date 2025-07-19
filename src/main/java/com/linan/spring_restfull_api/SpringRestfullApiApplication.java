package com.linan.spring_restfull_api;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringRestfullApiApplication {

	public static void main(String[] args) {
		try {
			Dotenv dotenv = Dotenv.load();
			dotenv.entries().forEach(
					entry -> System.setProperty(entry.getKey(), entry.getValue())
			);
		} catch (Exception ignored) {
			// Ignore error if .env or dotenv-java is not available (e.g., in Docker)
		}

		SpringApplication.run(SpringRestfullApiApplication.class, args);
	}


}
