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
package de.rahn.guidelines.springboot.app.jdbc.domain.people;

import static org.assertj.core.api.Assertions.assertThat;

import de.rahn.guidelines.springboot.app.jdbc.config.AggregateConfiguration;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.ContextConfiguration;

@DataJdbcTest
@ContextConfiguration(classes = AggregateConfiguration.class)
class PersonTests {

  @Autowired
  private PersonRepository repository;

  @Test
  void givenPerson_whenSave_thenInserted() {
    // Given
    String name = "Rahn";
    Person person = new Person(name, LocalDate.of(1967, 5, 5));

    // When
    Person savedPerson = repository.save(person);

    // Then
    assertThat(savedPerson).isNotNull();
    assertThat(savedPerson.getId()).isNotNull();
    assertThat(savedPerson.getLastName()).isEqualTo(name);
  }
}
