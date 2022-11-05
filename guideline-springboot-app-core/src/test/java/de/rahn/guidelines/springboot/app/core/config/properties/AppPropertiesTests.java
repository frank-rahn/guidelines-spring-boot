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
package de.rahn.guidelines.springboot.app.core.config.properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;

import de.rahn.guidelines.springboot.app.core.config.properties.AppProperties.Person;
import java.time.LocalDate;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Frank Rahn
 */
@SpringBootTest(
    properties = {
      "app.people[0].id=4711",
      "app.people[0].firstName=Frank",
      "app.people[0].lastName=Rahn",
      "app.people[0].birthday=1967-05-05",
      "app.people[0].emailAddress=frank@frank-rahn.de",
      "app.people[1].id=4712",
      "app.people[1].firstName=Martin",
      "app.people[1].lastName=Rahn",
      "app.people[1].birthday=1979-03-25",
      "app.people[1].emailAddress=martin@frank-rahn.de"
    })
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AppPropertiesTests {

  @Test
  void given_Context_when_loads_then_populate_AppProperties(
      @Autowired(required = false) AppProperties properties) {
    // Given
    // When
    // Then
    assertThat(properties)
        .extracting(AppProperties::getPeople, InstanceOfAssertFactories.list(Person.class))
        .hasSize(2)
        .element(0)
        .satisfies(
            person ->
                assertThat(person)
                    .hasFieldOrPropertyWithValue("id", "4711")
                    .hasFieldOrPropertyWithValue("firstName", "Frank")
                    .hasFieldOrPropertyWithValue("lastName", "Rahn")
                    .hasFieldOrPropertyWithValue("emailAddress", "frank@frank-rahn.de")
                    .returns(LocalDate.of(1967, 5, 5), from(Person::getBirthday)));
    assertThat(properties)
        .extracting(AppProperties::getPeople, InstanceOfAssertFactories.list(Person.class))
        .hasSize(2)
        .element(1)
        .satisfies(
            person ->
                assertThat(person)
                    .hasFieldOrPropertyWithValue("id", "4712")
                    .hasFieldOrPropertyWithValue("firstName", "Martin")
                    .hasFieldOrPropertyWithValue("lastName", "Rahn")
                    .hasFieldOrPropertyWithValue("emailAddress", "martin@frank-rahn.de")
                    .returns(LocalDate.of(1979, 3, 25), from(Person::getBirthday)));
  }
}
