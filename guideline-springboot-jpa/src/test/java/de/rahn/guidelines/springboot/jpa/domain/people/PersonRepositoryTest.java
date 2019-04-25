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
package de.rahn.guidelines.springboot.jpa.domain.people;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
class PersonRepositoryTest {

  @Autowired
  PersonRepository personRepository;

  @Test
  void injectedComponentsAreNotNull() {
    assertThat(personRepository).isNotNull();
  }

  @Test
  @Sql(statements = {"DELETE FROM person"})
  void whenSavedThenFindByName() {
    final Person person = new Person("Rahn", LocalDate.of(1940, 2, 8));
    person.setFirstName("Gerd");

    personRepository.save(person);
    assertThat(person.getId()).isNotEmpty();

    final List<Person> result = personRepository.findByLastName("Rahn");
    assertThat(result).hasSize(1);
    assertThat(result).contains(person);
  }

  @Test
  void whenNothingThenFindOldData() {
    final List<Person> result = personRepository.findByLastName("Rahn");
    assertThat(result).hasSize(2);
  }
}
