/***************************************************************************************************
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
 **************************************************************************************************/

package de.rahn.guidelines.springboot.rest.api;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.rahn.guidelines.springboot.rest.api.PeopleControllerTest.PeopleControllerTestConfiguration;
import de.rahn.guidelines.springboot.rest.domain.people.Person;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {PeopleController.class})
@ContextConfiguration(classes = {PeopleControllerTestConfiguration.class})
class PeopleControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void givenPeople_whenGetPeople_andNoAuth_thenReturnStatusUnauthorized() throws Exception {
    mockMvc
        .perform(get("/api/people").contentType(APPLICATION_JSON_UTF8))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser
  void givenPeople_whenGetPeople_thenReturnJsonArrayWith1Element() throws Exception {
    mockMvc
        .perform(get("/api/people").contentType(APPLICATION_JSON_UTF8))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].lastName", is("Rahn")));
  }

  @Test
  @WithMockUser
  void givenPerson_whenPutPerson_thenReturnPersonWithOk() throws Exception {
    String uuid = UUID.randomUUID().toString();

    Person person = new Person("Rahn", LocalDate.of(1979, 3, 25));
    person.setId(uuid);

    mockMvc
        .perform(
            put("/api/people/{id}", uuid)
                .contentType(APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(person)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.lastName", is("Rahn")));
  }

  @Test
  @WithMockUser
  void givenPerson_whenPostPerson_thenReturnPersonWithIdAndOk() throws Exception {
    Person person = new Person("Rahn", LocalDate.of(1979, 3, 25));

    mockMvc
        .perform(
            post("/api/people")
                .contentType(APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(person)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.lastName", is("Rahn")));
  }

  @Test
  @WithMockUser
  void givenPersonWithoutName_whenPostPerson_thenReturnValidationError() throws Exception {
    String personAsJson =
        "{\"id\": null, \"firstName\": null, \"lastName\": null, \"emailAddress\": null, "
            + "\"birthday\": null, \"infos\": null}";

    mockMvc
        .perform(post("/api/people").contentType(APPLICATION_JSON_UTF8).content(personAsJson))
        .andExpect(status().isBadRequest());
  }

  @TestConfiguration
  static class PeopleControllerTestConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.antMatcher("/api/*").authorizeRequests().anyRequest().hasRole("USER");
      http.httpBasic();
      http.csrf().disable();
    }
  }
}
