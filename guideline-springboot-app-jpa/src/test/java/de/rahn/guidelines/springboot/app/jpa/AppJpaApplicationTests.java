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
package de.rahn.guidelines.springboot.app.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import de.rahn.guidelines.springboot.app.jpa.domain.people.Person;
import de.rahn.guidelines.springboot.app.jpa.domain.people.PersonRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author Frank Rahn
 */
@ExtendWith({SpringExtension.class})
@SpringBootTest
class AppJpaApplicationTests {

  @Autowired
  private PersonRepository repository;

  @Test
  @WithMockUser
  void givenContextWithUser_whenLoads_thenOk() {
    Optional<Person> personOpt = repository.findByLastName("Rahn").stream().findFirst();

    assertThat(personOpt).isPresent();

    Person person = personOpt.get();
    person.setEmailAddress("rahn@koeln.de");

    repository.save(person);
  }
}
