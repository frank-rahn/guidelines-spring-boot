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
import de.rahn.guidelines.springboot.rest.domain.people.Person;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author Frank Rahn
 */
@WebMvcTest(controllers = {PeopleController.class})
class PeopleControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private PeopleController classUnderTest;

  private Person person;

  @BeforeEach
  void resetPeopleController() {
    person = classUnderTest.reset();
  }

  @Test
  void givenPeopleAndNoAuth_whenGetPeople_thenReturnStatusUnauthorized() throws Exception {
    mockMvc
        .perform(get("/api/people").contentType(APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser
  void givenPeople_whenGetPeople_thenReturnJsonArrayWith1Element() throws Exception {
    mockMvc
        .perform(get("/api/people").contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].lastName", is(person.getLastName())))
        .andExpect(jsonPath("$[0].id", is(person.getId())));
  }

  @Test
  @WithMockUser
  void givenInvalidId_whenGetPeopleById_thenReturnHttpStatus404() throws Exception {
    mockMvc
        .perform(get("/api/people/" + UUID.randomUUID().toString()).contentType(APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser
  void givenValidId_whenGetPeopleById_thenReturnJsonElement() throws Exception {
    mockMvc
        .perform(get("/api/people/" + person.getId()).contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.lastName", is(person.getLastName())))
        .andExpect(jsonPath("$.id", is(person.getId())));
  }

  @Test
  @WithMockUser
  void givenInvalidId_whenDeletePeopleById_thenReturnHttpStatus204() throws Exception {
    mockMvc
        .perform(
            delete("/api/people/" + UUID.randomUUID().toString()).contentType(APPLICATION_JSON))
        .andExpect(status().isNoContent());
  }

  @Test
  @WithMockUser
  void givenValidId_whenDeletePeopleById_thenReturnHttpStatus204() throws Exception {
    mockMvc
        .perform(delete("/api/people/" + person.getId()).contentType(APPLICATION_JSON))
        .andExpect(status().isNoContent());
  }

  @Test
  @WithMockUser
  void givenPerson_whenPutPersonById_thenReturnPersonWithOk() throws Exception {
    String uuid = UUID.randomUUID().toString();

    Person person = new Person("Rahn", LocalDate.of(1979, 3, 25));
    person.setId(uuid);

    mockMvc
        .perform(
            put("/api/people/{id}", uuid)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.lastName", is("Rahn")))
        .andExpect(jsonPath("$.id", is(uuid)));
  }

  @Test
  @WithMockUser
  void givenPerson_whenPostPerson_thenReturnPersonWithIdAndOk() throws Exception {
    Person person = new Person("Rahn", LocalDate.of(1979, 3, 25));

    mockMvc
        .perform(
            post("/api/people")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person)))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.lastName", is("Rahn")))
        .andExpect(header().string("location", startsWith("http://localhost/api/people/")));
  }

  @Test
  @WithMockUser
  void givenPersonWithoutName_whenPostPerson_thenReturnValidationError() throws Exception {
    String personAsJson =
        "{\"id\": null, \"firstName\": null, \"lastName\": null, \"emailAddress\": null, "
            + "\"birthday\": null, \"infos\": null}";

    mockMvc
        .perform(post("/api/people").contentType(APPLICATION_JSON).content(personAsJson))
        .andExpect(status().isBadRequest());
  }
}
