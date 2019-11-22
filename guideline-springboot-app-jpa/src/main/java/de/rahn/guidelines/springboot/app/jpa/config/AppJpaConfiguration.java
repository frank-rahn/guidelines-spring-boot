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
package de.rahn.guidelines.springboot.app.jpa.config;

import de.rahn.guidelines.springboot.app.jpa.domain.people.Person;
import de.rahn.guidelines.springboot.app.jpa.domain.people.PersonRepository;
import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Frank Rahn
 */
@Configuration
@Slf4j
public class AppJpaConfiguration {

  @Bean
  @Order(1)
  ApplicationRunner initDatabase(PersonRepository repository) {
    return new ApplicationRunner() {

      @Override
      @Transactional
      public void run(ApplicationArguments args) {
        Person person = new Person("Rahn", LocalDate.of(1967, 5, 5));
        person.setFirstName("Frank");
        repository.save(person);

        person = new Person("Rahn", LocalDate.of(1979, 3, 25));
        person.setFirstName("Martin");
        repository.save(person);
      }
    };
  }

  @Bean
  @Order(2)
  ApplicationRunner usingDatabase(PersonRepository repository) {
    return args ->
        repository.findByLastName("Rahn").forEach(person -> LOGGER.info(person.toString()));
  }
}
