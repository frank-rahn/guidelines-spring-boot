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
package de.rahn.guidelines.springboot.app.jpa.domain.people;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;

import de.rahn.guidelines.springboot.app.jpa.config.JpaConfiguration;
import java.time.LocalDate;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;

/**
 * @author Frank Rahn
 */
@DataJpaTest(
    includeFilters = {
      @Filter(
          type = ASSIGNABLE_TYPE,
          classes = {JpaConfiguration.class})
    })
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PersonRepositoryTests {

  @Autowired PersonRepository personRepository;

  @Test
  void given_Context_when_loads_then_injected_components_are_not_null() {
    assertThat(personRepository).isNotNull();
  }

  @Test
  @SuppressWarnings({"SqlResolve", "SqlWithoutWhere"})
  @Sql(statements = {"DELETE FROM person"})
  void given_People_without_authentication_when_saved_then_FindByName() {
    givenPeople_whenSaved_thenFindByName();
  }

  @Test
  @SuppressWarnings({"SqlResolve", "SqlWithoutWhere"})
  @Sql(statements = {"DELETE FROM person"})
  @WithMockUser
  void given_People_with_authentication_when_saved_then_FindByName() {
    givenPeople_whenSaved_thenFindByName();
  }

  private void givenPeople_whenSaved_thenFindByName() {
    // Given
    var person = new Person("Rahn", LocalDate.of(1940, 2, 8));
    person.setFirstName("Gerd");

    // When
    var result = personRepository.save(person);

    // Then
    assertThat(result).extracting(Person::getId, InstanceOfAssertFactories.STRING).isNotEmpty();

    var foundPerson = personRepository.findByLastName("Rahn");
    assertThat(foundPerson).hasSize(1).contains(person);
  }

  @Test
  void given_nothing_when_FindByLastName_then_find_old_data() {
    // Given
    // When
    var result = personRepository.findByLastName("Rahn");

    // Then
    assertThat(result).hasSize(0);
  }
}
