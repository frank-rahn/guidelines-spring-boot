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
package de.rahn.guidelines.springboot.rest.api;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.rahn.guidelines.springboot.rest.config.WebSecurityConfiguration;
import de.rahn.guidelines.springboot.rest.domain.people.Person;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author Frank Rahn
 */
@WebMvcTest(controllers = {PeopleController.class})
@Import({WebSecurityConfiguration.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PeopleControllerTests {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private PeopleController classUnderTest;

  /** Given */
  private Person person;

  @BeforeEach
  void resetPeopleController() {
    person = classUnderTest.reset();
  }

  @Test
  void given_People_and_no_Auth_when_getAllPeople_then_return_status_unauthorized()
      throws Exception {
    // Given
    // When
    var result = mockMvc.perform(get("/api/people").contentType(APPLICATION_JSON));

    // Then
    result.andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(roles = {"GAST"})
  void given_People_and_no_Auth_when_getAllPeople_then_return_status_forbidden() throws Exception {
    // Given
    // When
    var result = mockMvc.perform(get("/api/people").contentType(APPLICATION_JSON));

    // Then
    result.andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser
  void given_People_when_getAllPeople_then_return_json_array_with_1_element() throws Exception {
    // Given
    // When
    var result = mockMvc.perform(get("/api/people").contentType(APPLICATION_JSON));

    // Then
    result
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].lastName", is(person.getLastName())))
        .andExpect(jsonPath("$[0].id", is(person.getId())));
  }

  @Test
  @WithMockUser
  void given_invalid_Id_when_getPeopleById_then_return_status_not_found() throws Exception {
    // Given
    var id = UUID.randomUUID().toString();

    // When
    var result = mockMvc.perform(get("/api/people/{id}", id).contentType(APPLICATION_JSON));

    // Then
    result.andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser
  void given_valid_Id_when_getPeopleById_then_return_json_Element() throws Exception {
    // Given
    var id = person.getId();

    // When
    var result = mockMvc.perform(get("/api/people/{id}", id).contentType(APPLICATION_JSON));

    // Then
    result
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.lastName", is(person.getLastName())))
        .andExpect(jsonPath("$.id", is(id)));
  }

  @Test
  @WithMockUser
  void given_invalid_Id_when_deletePeopleById_then_return_status_no_content() throws Exception {
    // Given
    var id = UUID.randomUUID().toString();

    // When
    var result = mockMvc.perform(delete("/api/people/{id}", id).contentType(APPLICATION_JSON));

    // Then
    result.andExpect(status().isNoContent());
  }

  @Test
  @WithMockUser
  void given_valid_Id_when_deletePeopleById_then_return_status_no_content() throws Exception {
    // Given
    var id = person.getId();

    // When
    var result = mockMvc.perform(delete("/api/people/{id}", id).contentType(APPLICATION_JSON));

    // Then
    result.andExpect(status().isNoContent());
  }

  @Test
  @WithMockUser
  void given_Person_when_putPersonById_then_return_Person_with_status_ok() throws Exception {
    // Given
    var id = UUID.randomUUID().toString();
    var person = new Person("Rahn", LocalDate.of(1979, 3, 25));
    person.setId(id);

    // When
    var result =
        mockMvc.perform(
            put("/api/people/{id}", id)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person)));

    // Then
    result
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.lastName", is(person.getLastName())))
        .andExpect(jsonPath("$.id", is(id)));
  }

  @Test
  @WithMockUser
  void given_Person_when_postPerson_then_return_Person_with_id_and_status_ok() throws Exception {
    // Given
    var person = new Person("Rahn", LocalDate.of(1979, 3, 25));

    // When
    var result =
        mockMvc.perform(
            post("/api/people")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person)));

    // Then
    result
        .andExpect(status().isCreated())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.lastName", is(person.getLastName())))
        .andExpect(header().string("location", startsWith("http://localhost/api/people/")));
  }

  @Test
  @WithMockUser
  void given_Person_without_Name_when_postPerson_then_return_ValidationError() throws Exception {
    // Given
    String personAsJson =
        "{\"id\": null, \"firstName\": null, \"lastName\": null, \"emailAddress\": null, "
            + "\"birthday\": null, \"infos\": null}";

    // When
    var result =
        mockMvc.perform(post("/api/people").contentType(APPLICATION_JSON).content(personAsJson));

    // Then
    result.andExpect(status().isBadRequest());
  }
}
