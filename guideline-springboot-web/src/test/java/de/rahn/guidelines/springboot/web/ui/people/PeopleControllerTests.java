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

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.isNull;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;

import java.util.List;
import java.util.UUID;
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
class PeopleControllerTests {

  @Mock
  private RestTemplate template;

  @InjectMocks
  private PeopleController classUnderTest;

  @Test
  void givenPeople_WhenGetAllPeople_ThenOk() {
    // Given
    List<Person> personList = List.of(new Person());
    ResponseEntity<List<Person>> people = new ResponseEntity<>(personList, OK);

    doReturn(people)
        .when(template)
        .exchange(
            eq("/"), eq(GET), isNull(), Mockito.<ParameterizedTypeReference<List<Person>>>any());
    ExtendedModelMap model = new ExtendedModelMap();

    // When
    String result = classUnderTest.getAllPeople(model);

    // Then
    assertThat(result).isEqualTo("people");
    assertThat(model).hasSize(1);
    assertThat(model.getAttribute("people")).isEqualTo(personList);
  }

  @Test
  void givenNothing_WhenGetAllPeople_ThenOk() {
    // Given
    List<Person> personList = emptyList();
    ResponseEntity<List<Person>> people = new ResponseEntity<>(personList, OK);

    doReturn(people)
        .when(template)
        .exchange(
            eq("/"), eq(GET), isNull(), Mockito.<ParameterizedTypeReference<List<Person>>>any());
    ExtendedModelMap model = new ExtendedModelMap();

    // When
    String result = classUnderTest.getAllPeople(model);

    // Then
    assertThat(result).isEqualTo("people");
    assertThat(model).hasSize(1);
    assertThat(model.getAttribute("people")).isEqualTo(personList);
  }

  @Test
  void givenPerson_whenGetPersonById_thenOk() {
    // Given
    ExtendedModelMap model = new ExtendedModelMap();
    UUID id = UUID.randomUUID();
    Person person = new Person();
    doReturn(person).when(template).getForObject(eq("/" + id.toString()), eq(Person.class));

    // When
    String result = classUnderTest.getPersonById(id, model);

    // Then
    assertThat(result).isEqualTo("person");
    assertThat(model).hasSize(1);
    assertThat(model.getAttribute("person")).isEqualTo(person);
  }
}
