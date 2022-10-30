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
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
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
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PersonTests {

  @Autowired private PersonRepository repository;

  @Test
  @WithMockUser(username = "user")
  void given_Person_when_save_then_Person_has_Uuid_and_AuditorInformation() {
    // Given
    var creator = "user";
    var auditor = "user";
    var name = "Rahn";
    var birthday = LocalDate.of(1967, 5, 5);
    var person = new Person(name, birthday);

    // When
    var result = repository.save(person);

    // Then
    assertThat(result).extracting(Person::getId).isNotNull();
    assertThat(result).extracting(Person::getFirstName).isNull();
    assertThat(result).extracting(Person::getLastName).isEqualTo(name);
    assertThat(result).extracting(Person::getEmailAddress).isNull();
    assertThat(result).extracting(Person::getBirthday).isEqualTo(birthday);
    assertThat(result).extracting(Person::getCreatedBy).isEqualTo(creator);
    assertThat(result).extracting(Person::getCreatedDate).isNotNull();
    assertThat(result).extracting(Person::getLastModifiedBy).isEqualTo(auditor);
    assertThat(result).extracting(Person::getLastModifiedDate).isNotNull();
  }

  @Test
  @WithMockUser(username = "user")
  void given_Person_with_Address_when_save_then_Person_has_Uuid_and_AuditorInformation() {
    // Given
    var creator = "user";
    var auditor = "user";
    var name = "Rahn";
    var birthday = LocalDate.of(1967, 5, 5);
    var person = new Person(name, birthday);
    var address = new Address("Neusser Str. 594", "50737 Köln");
    person.getAddresses().add(address);

    // When
    var result = repository.save(person);

    // Then
    assertThat(result).extracting(Person::getId).isNotNull();
    assertThat(result).extracting(Person::getFirstName).isNull();
    assertThat(result).extracting(Person::getLastName).isEqualTo(name);
    assertThat(result).extracting(Person::getEmailAddress).isNull();
    assertThat(result).extracting(Person::getBirthday).isEqualTo(birthday);
    assertThat(result).extracting(Person::getCreatedBy).isEqualTo(creator);
    assertThat(result).extracting(Person::getCreatedDate).isNotNull();
    assertThat(result).extracting(Person::getLastModifiedBy).isEqualTo(auditor);
    assertThat(result).extracting(Person::getLastModifiedDate).isNotNull();
  }

  @Test
  @WithMockUser(username = "gast")
  @Sql(scripts = "classpath:saved.sql")
  void given_Person_when_load_and_update_then_changed_FirstName_and_Version() {
    // Given
    var creator = "user";
    var auditor = "gast";
    var name = "Rahn";
    var firstName = "Frank";
    var birthday = LocalDate.of(1967, 5, 5);

    // When
    var result = repository.findByLastName(name);

    assertThat(result).hasSize(1);
    Person savedPerson = result.get(0);
    savedPerson.setFirstName(firstName);

    Person updatedPerson = repository.save(savedPerson);

    // Then
    assertThat(updatedPerson).extracting(Person::getId).isNotNull();
    assertThat(updatedPerson).extracting(Person::getFirstName).isEqualTo(firstName);
    assertThat(updatedPerson).extracting(Person::getLastName).isEqualTo(name);
    assertThat(updatedPerson).extracting(Person::getEmailAddress).isNull();
    assertThat(updatedPerson).extracting(Person::getBirthday).isEqualTo(birthday);
    assertThat(updatedPerson).extracting(Person::getCreatedBy).isEqualTo(creator);
    assertThat(updatedPerson).extracting(Person::getCreatedDate).isNotNull();
    assertThat(updatedPerson).extracting(Person::getLastModifiedBy).isEqualTo(auditor);
    assertThat(updatedPerson).extracting(Person::getLastModifiedDate).isNotNull();
  }
}
