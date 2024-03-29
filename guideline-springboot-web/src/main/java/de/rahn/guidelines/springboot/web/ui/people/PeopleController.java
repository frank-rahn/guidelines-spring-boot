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

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

/**
 * @author Frank Rahn
 */
@Controller
@RequestMapping(path = "/people")
@Secured("ROLE_USER")
@Slf4j
@RequiredArgsConstructor
class PeopleController {

  private final RestTemplate template;

  @GetMapping
  public String getAllPeople(Model model) {
    LOGGER.info("GetAllPeople: Authentication={}", getContext().getAuthentication());

    var people =
        template.exchange("/", GET, null, new ParameterizedTypeReference<List<Person>>() {});

    model.addAttribute("people", people.getBody());

    return "people";
  }

  @GetMapping(path = "/{id}")
  public String getPersonById(@PathVariable UUID id, Model model) {
    LOGGER.info("GetPersonById: Id={}, Authentication={}", id, getContext().getAuthentication());

    var person = template.getForObject("/" + id.toString(), Person.class);

    model.addAttribute("person", person);

    return "person";
  }
}
