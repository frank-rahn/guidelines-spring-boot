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
package de.rahn.guidelines.springboot.app.jpa.domain.people;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

/**
 * @author Frank Rahn
 */
@DataJpaTest
class PersonRepositoryTest {

  @Autowired
  PersonRepository personRepository;

  @Test
  void givenContext_whenLoads_thenInjectedComponentsAreNotNull() {
    assertThat(personRepository).isNotNull();
  }

  @Test
  @Sql(statements = {"DELETE FROM person"})
  void givenPeople_whenSaved_thenFindByName() {
    // Given
    final Person person = new Person("Rahn", LocalDate.of(1940, 2, 8));
    person.setFirstName("Gerd");

    // When
    final Person result = personRepository.save(person);

    // Then
    assertThat(result.getId()).isNotEmpty();

    final List<Person> foundPerson = personRepository.findByLastName("Rahn");
    assertThat(foundPerson).hasSize(1);
    assertThat(foundPerson).contains(person);
  }

  @Test
  void givenNothing_whenFindByLastName_thenFindOldData() {
    // When
    final List<Person> result = personRepository.findByLastName("Rahn");

    // Then
    assertThat(result).hasSize(0);
  }
}
