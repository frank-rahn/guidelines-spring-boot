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
package de.rahn.guidelines.springboot.web.ui.people;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.isNull;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.client.RestTemplate;

@ExtendWith({MockitoExtension.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PeopleControllerTests {

  @Mock private RestTemplate template;

  @InjectMocks private PeopleController classUnderTest;

  @Test
  void given_People_when_getAllPeople_then_ok(@Mock Person person) {
    // Given
    var personList = List.of(person);
    var people = new ResponseEntity<>(personList, OK);
    doReturn(people)
        .when(template)
        .exchange(
            eq("/"), eq(GET), isNull(), Mockito.<ParameterizedTypeReference<List<Person>>>any());
    var model = new ExtendedModelMap();

    // When
    var result = classUnderTest.getAllPeople(model);

    // Then
    assertThat(result).isEqualTo("people");
    assertThat(model).hasSize(1);
    assertThat(model.getAttribute("people")).isEqualTo(personList);
  }

  @Test
  void given_nothing_when_getAllPeople_then_ok(@Mock List<Person> personList) {
    // Given
    var people = new ResponseEntity<>(personList, OK);
    doReturn(people)
        .when(template)
        .exchange(
            eq("/"), eq(GET), isNull(), Mockito.<ParameterizedTypeReference<List<Person>>>any());
    var model = new ExtendedModelMap();

    // When
    String result = classUnderTest.getAllPeople(model);

    // Then
    assertThat(result).isEqualTo("people");
    assertThat(model).hasSize(1);
    assertThat(model.getAttribute("people")).isEqualTo(personList);
  }

  @Test
  void given_Person_when_getPersonById_then_ok(@Mock Person person) {
    // Given
    var uuid = UUID.randomUUID();
    var url = "/" + uuid;
    doReturn(person).when(template).getForObject(eq(url), eq(Person.class));
    var model = new ExtendedModelMap();

    // When
    var result = classUnderTest.getPersonById(uuid, model);

    // Then
    assertThat(result).isEqualTo("person");
    assertThat(model).hasSize(1);
    assertThat(model.getAttribute("person")).isEqualTo(person);
  }
}
