/*
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
 */
package de.rahn.guidelines.springboot.rest.api;

import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_NO_CONTENT;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

import de.rahn.guidelines.springboot.rest.domain.people.Person;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.net.URI;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(
    path = {"/api/people"},
    produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
@Api(tags = {"People"})
class PeopleController {

  private static final Logger LOGGER = LoggerFactory.getLogger(PeopleController.class);

  private final Map<String, Person> people = new HashMap<>();

  private Person addPersonToPeople(Person person) {
    Assert.notNull(person.getId(), "Id darf nicht null sein!");

    people.put(person.getId(), person);

    return person;
  }

  private Person addPersonToPeople(String id, Person person) {
    if (id != null) {
      person.setId(id);
    } else {
      person.setId(UUID.randomUUID().toString());
    }

    return addPersonToPeople(person);
  }

  @PostConstruct
  void setup() {
    Person person = new Person("Rahn", LocalDate.of(1967, 5, 5));
    person.setId(UUID.randomUUID().toString());
    person.setFirstName("Frank");

    addPersonToPeople(person);
  }

  @GetMapping
  @ApiOperation("Liefert alle Personen")
  @ApiResponses({
      @ApiResponse(code = HTTP_OK, message = "Personen erfolgreich abgerufen"),
      @ApiResponse(code = HTTP_UNAUTHORIZED, message = "Nicht autorisiert die Personen abzurufen"),
      @ApiResponse(code = HTTP_FORBIDDEN, message = "Zugriff auf die Personen ist verboten")
  })
  Collection<Person> getAllPeople() {
    LOGGER.info("GetAllPeople: Authentication={}", getContext().getAuthentication());

    return people.values();
  }

  @GetMapping(path = "/{id}")
  @ApiOperation("Suche die Person mit der Id")
  @ApiResponses({
      @ApiResponse(code = HTTP_OK, message = "Person erfolgreich abgerufen"),
      @ApiResponse(code = HTTP_UNAUTHORIZED, message = "Nicht autorisiert die Person abzurufen"),
      @ApiResponse(code = HTTP_FORBIDDEN, message = "Zugriff auf die Person ist verboten"),
      @ApiResponse(code = HTTP_NOT_FOUND, message = "Keine Person gefunden")
  })
  public ResponseEntity<Person> getPersonById(
      @PathVariable("id") @ApiParam(value = "Die UUID der gesuchten Person", required = true)
          String id) {
    LOGGER.info("GetPersonById: Id={}, Authentication={}", id, getContext().getAuthentication());

    return ResponseEntity.of(Optional.ofNullable(people.get(id)));
  }

  @DeleteMapping(path = "/{id}")
  @ApiOperation("Lösche die Person mit der Id")
  @ApiResponses({
      @ApiResponse(code = HTTP_NO_CONTENT, message = "Person erfolgreich gelöscht"),
      @ApiResponse(code = HTTP_UNAUTHORIZED, message = "Nicht autorisiert die Person zu löschen"),
      @ApiResponse(code = HTTP_FORBIDDEN, message = "Löschen der Person ist verboten")
  })
  ResponseEntity<Void> deletePersonById(
      @ApiParam(value = "Die UUID der zu löschenden Person", required = true) @PathVariable("id")
          String id) {
    LOGGER.info("DeletePersonById: Id={}, Authentication={}", id, getContext().getAuthentication());

    people.remove(id);

    return ResponseEntity.noContent().build();
  }

  @PostMapping
  @ApiOperation("Füge eine Person hinzu")
  @ApiResponses({
      @ApiResponse(code = HTTP_CREATED, message = "Person erfolgreich angelegt"),
      @ApiResponse(code = HTTP_UNAUTHORIZED, message = "Nicht autorisiert Personen zu bearbeiten"),
      @ApiResponse(code = HTTP_FORBIDDEN, message = "Zugriff auf die Person ist verboten")
  })
  ResponseEntity<Person> postPerson(
      @ApiParam(value = "Die neue Person", required = true) @RequestBody @Valid Person person) {
    LOGGER.info(
        "PostPerson: Person={}, Authentication={}", person, getContext().getAuthentication());

    person = addPersonToPeople(null, person);
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").build(person.getId());
    LOGGER.info("PostPerson: Location={}", location);

    return ResponseEntity.created(location).body(person);
  }

  @PutMapping(path = "/{id}")
  @ApiOperation("Füge die Person mit der Id hinzu oder ändere sie")
  @ApiResponses({
      @ApiResponse(code = HTTP_OK, message = "Person erfolgreich geändert oder angelegt"),
      @ApiResponse(code = HTTP_UNAUTHORIZED, message = "Nicht autorisiert Personen zu bearbeiten"),
      @ApiResponse(code = HTTP_FORBIDDEN, message = "Zugriff auf die Person ist verboten")
  })
  Person putPerson(
      @ApiParam(value = "Die UUID der neuen oder zu ändernde Person", required = true)
      @PathVariable("id")
          String id,
      @ApiParam(value = "Die neue oder zu ändernde Person", required = true) @RequestBody @Valid
          Person person) {
    LOGGER.info(
        "PutPerson: Id={}, Person={}, Authentication={}",
        id,
        person,
        getContext().getAuthentication());

    return addPersonToPeople(id, person);
  }
}
