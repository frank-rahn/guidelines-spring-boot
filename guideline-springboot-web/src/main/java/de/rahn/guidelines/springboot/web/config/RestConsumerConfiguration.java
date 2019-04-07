package de.rahn.guidelines.springboot.web.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConsumerConfiguration {

  @Bean
  RestTemplate restTemplate(RestTemplateBuilder builder) {
    return builder.rootUri("http://localhost:8081/api/people").build();
  }
}
