/*
 * Copyright (c) 2019-2020 the original author or authors.
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
package de.rahn.guidelines.springboot.app.jdbc.config;

import de.rahn.guidelines.springboot.app.jdbc.domain.people.Person;
import de.rahn.guidelines.springboot.app.jdbc.domain.people.PersonRepository;
import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.ExitCodeExceptionMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @author Frank Rahn
 */
@Configuration
@Slf4j
class AppJdbcConfiguration {

  @Bean
  @Order(1)
  ApplicationRunner insertPerson(PersonRepository repository) {
    return args -> {
      var person = repository.save(new Person("Rahn", LocalDate.of(1967, 5, 5)));
      LOGGER.info("Person erzeugt: {}", person);
    };
  }

  @Bean
  @Order(2)
  ApplicationRunner selectAllPerson(PersonRepository repository) {
    return args -> LOGGER.info("Gelesene Personen: {}", repository.findAll());
  }

  @Bean
  ExitCodeExceptionMapper exitCodeExceptionMapper() {
    return exception -> {
      var cause = exception.getCause();

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
