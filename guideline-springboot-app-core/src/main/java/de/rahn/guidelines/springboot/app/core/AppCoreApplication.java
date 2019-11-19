/*
 * Copyright (c) 2019-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.rahn.guidelines.springboot.app.core;

import de.rahn.guidelines.springboot.app.core.config.properties.AppProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.ExitCodeExceptionMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

/**
 * @author Frank Rahn
 */
@SpringBootApplication
@EnableConfigurationProperties({AppProperties.class})
@Slf4j
public class AppCoreApplication {

  public static void main(String[] args) {
    ApplicationContext applicationContext = SpringApplication.run(AppCoreApplication.class, args);

    int exitCode = SpringApplication.exit(applicationContext);
    LOGGER.info("App finished with exit code {}", exitCode);
    System.exit(exitCode);
  }

  @Bean
  @Order(1)
  ApplicationRunner start(AppProperties app) {
    return args -> LOGGER.info(app.toString());
  }

  @Bean
  @Order(2)
  ApplicationRunner fail() {
    return args -> {
      if (args.containsOption("go-wrong")) {
        LOGGER.warn("Option go-wrong received");
        throw new IllegalArgumentException("Option go-wrong received");
      }

      LOGGER.info("No option received");
    };
  }

  @Bean
  ExitCodeExceptionMapper exitCodeExceptionMapper() {
    return exception -> {
      Throwable cause = exception.getCause();

      if (cause == null) {
        cause = exception;
      }

      if (cause instanceof IllegalArgumentException) {
        return 3;
      }

      // Unknown Exception
      return 2;
    };
  }
}
