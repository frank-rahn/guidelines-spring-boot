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
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

/**
 * @author Frank Rahn
 */
@DataJdbcTest
@ContextConfiguration(classes = AggregateConfiguration.class)
class PersonTests {

  @Autowired
  private PersonRepository repository;

  @Test
  @WithMockUser(username = "user")
  void givenPerson_whenSave_thenPersonHasUuidAndAuditorInformation() {
    // Given
    final String creator = "user";
    final String auditor = "user";
    final String name = "Rahn";
    final LocalDate birthday = LocalDate.of(1967, 5, 5);
    Person person = new Person(name, birthday);

    // When
    Person savedPerson = repository.save(person);

    // Then
    assertThat(savedPerson).isNotNull();
    assertThat(savedPerson.getId()).isNotNull();
    assertThat(savedPerson.getFirstName()).isNull();
    assertThat(savedPerson.getLastName()).isEqualTo(name);
    assertThat(savedPerson.getEmailAddress()).isNull();
    assertThat(savedPerson.getBirthday()).isEqualTo(birthday);
    assertThat(savedPerson.getCreatedBy()).isEqualTo(creator);
    assertThat(savedPerson.getCreatedDate()).isNotNull();
    assertThat(savedPerson.getLastModifiedBy()).isEqualTo(auditor);
    assertThat(savedPerson.getLastModifiedDate()).isNotNull();
  }

  @Test
  @WithMockUser(username = "user")
  void givenPersonWithAddress_whenSave_thenPersonHasUuidAndAuditorInformation() {
    // Given
    final String creator = "user";
    final String auditor = "user";
    final String name = "Rahn";
    final LocalDate birthday = LocalDate.of(1967, 5, 5);
    Person person = new Person(name, birthday);
    Address address = new Address("Neusser Str. 594", "50737 Köln");
    person.getAddresses().add(address);

    // When
    Person savedPerson = repository.save(person);

    // Then
    assertThat(savedPerson).isNotNull();
    assertThat(savedPerson.getId()).isNotNull();
    assertThat(savedPerson.getFirstName()).isNull();
    assertThat(savedPerson.getLastName()).isEqualTo(name);
    assertThat(savedPerson.getEmailAddress()).isNull();
    assertThat(savedPerson.getBirthday()).isEqualTo(birthday);
    assertThat(savedPerson.getCreatedBy()).isEqualTo(creator);
    assertThat(savedPerson.getCreatedDate()).isNotNull();
    assertThat(savedPerson.getLastModifiedBy()).isEqualTo(auditor);
    assertThat(savedPerson.getLastModifiedDate()).isNotNull();
  }

  @Test
  @WithMockUser(username = "gast")
  @Sql(scripts = "classpath:saved.sql")
  void givenPerson_whenLoadAndUpdate_thenChangedFirstNameAndVersion() {
    // Given
    final String creator = "user";
    final String auditor = "gast";
    final String name = "Rahn";
    final String firstName = "Frank";
    final LocalDate birthday = LocalDate.of(1967, 5, 5);

    // When
    List<Person> persons = repository.findByLastName(name);

    assertThat(persons).hasSize(1);
    Person savedPerson = persons.get(0);
    savedPerson.setFirstName(firstName);

    Person updatedPerson = repository.save(savedPerson);

    // Then
    assertThat(updatedPerson).isNotNull();
    assertThat(updatedPerson.getId()).isNotNull();
    assertThat(updatedPerson.getFirstName()).isEqualTo(firstName);
    assertThat(updatedPerson.getLastName()).isEqualTo(name);
    assertThat(updatedPerson.getEmailAddress()).isNull();
    assertThat(updatedPerson.getBirthday()).isEqualTo(birthday);
    assertThat(updatedPerson.getCreatedBy()).isEqualTo(creator);
    assertThat(updatedPerson.getCreatedDate()).isNotNull();
    assertThat(updatedPerson.getLastModifiedBy()).isEqualTo(auditor);
    assertThat(updatedPerson.getLastModifiedDate()).isNotNull();
  }
}
