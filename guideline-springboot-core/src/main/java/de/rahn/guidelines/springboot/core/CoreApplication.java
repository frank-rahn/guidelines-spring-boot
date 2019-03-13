package de.rahn.guidelines.springboot.core;

import de.rahn.guidelines.springboot.core.util.AppProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CoreApplication {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CoreApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(CoreApplication.class, args);
	}

	@Bean
	protected ApplicationRunner start(AppProperties app) {
		return args -> LOGGER.info(app.toString());
	}
}