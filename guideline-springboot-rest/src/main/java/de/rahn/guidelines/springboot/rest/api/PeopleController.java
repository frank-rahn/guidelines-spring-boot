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

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

import de.rahn.guidelines.springboot.rest.domain.people.Person;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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

/** @author Frank Rahn */
@RestController
@RequestMapping(
    path = {"/api/people"},
    produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
@Tag(name = "People")
@ApiResponse(
    responseCode = "401",
    description = "Nicht autorisiert die Personen abzurufen (Unauthorized)",
    content = {@Content()})
@ApiResponse(
    responseCode = "403",
    description = "Zugriff auf die Personen ist verboten (Forbidden)",
    content = {@Content()})
@ApiResponse(
    responseCode = "500",
    description = "Unbekannter Fehler im Service (Internal Server Error)",
    content = {@Content()})
@Slf4j
class PeopleController {

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
  private Person setup() {
    Person person = new Person("Rahn", LocalDate.of(1967, 5, 5));
    person.setId(UUID.randomUUID().toString());
    person.setFirstName("Frank");

    return addPersonToPeople(person);
  }

  @GetMapping
  @Secured("ROLE_USER")
  @Operation(summary = "Liefert alle Personen")
  @ApiResponse(responseCode = "200", description = "Personen erfolgreich abgerufen (Ok)")
  public Collection<Person> getPeople() {
    LOGGER.info("GetAllPeople: Authentication={}", getContext().getAuthentication());

    return people.values();
  }

  @GetMapping(path = "/{id}")
  @Secured("ROLE_USER")
  @Operation(summary = "Suche die Person mit der Id")
  @ApiResponse(responseCode = "200", description = "Person erfolgreich abgerufen (Ok)")
  @ApiResponse(
      responseCode = "404",
      description = "Keine Person gefunden (Not Found)",
      content = {@Content()})
  public ResponseEntity<Person> getPersonById(
      @PathVariable @Parameter(description = "Die UUID der gesuchten Person") UUID id) {
    LOGGER.info("GetPersonById: Id={}, Authentication={}", id, getContext().getAuthentication());

    return ResponseEntity.of(Optional.ofNullable(people.get(id.toString())));
  }

  @DeleteMapping(path = "/{id}")
  @Secured("ROLE_USER")
  @Operation(summary = "Lösche die Person mit der Id")
  @ApiResponse(responseCode = "204", description = "Person erfolgreich gelöscht (No Content)")
  public ResponseEntity<Void> deletePersonById(
      @PathVariable @Parameter(description = "Die UUID der zu löschenden Person") UUID id) {
    LOGGER.info("DeletePersonById: Id={}, Authentication={}", id, getContext().getAuthentication());

    people.remove(id.toString());

    return ResponseEntity.noContent().build();
  }

  @PutMapping(path = "/{id}")
  @Secured("ROLE_USER")
  @Operation(summary = "Füge die Person mit der Id hinzu oder ändere sie")
  @ApiResponse(responseCode = "200", description = "Person erfolgreich geändert oder angelegt (Ok)")
  public Person putPersonById(
      @PathVariable @Parameter(description = "Die UUID der neuen oder zu ändernde Person") UUID id,
      @RequestBody
      @Parameter(description = "Die neue oder zu ändernde Person", required = true)
      @Valid
          Person person) {
    LOGGER.info(
        "PutPerson: Id={}, Person={}, Authentication={}",
        id,
        person,
        getContext().getAuthentication());

    return addPersonToPeople(id.toString(), person);
  }

  @PostMapping
  @Secured("ROLE_USER")
  @Operation(description = "Füge eine Person hinzu")
  @ApiResponse(
      responseCode = "201",
      description = "Person erfolgreich angelegt (CREATED)",
      headers = {
          @Header(
              name = "location",
              description = "URI der angelegten Person",
              required = true,
              schema = @Schema(name = "string", format = "uri"))
      })
  public ResponseEntity<Person> postPerson(
      @RequestBody @Parameter(description = "Die neue Person", required = true) @Valid
          Person person) {
    LOGGER.info(
        "PostPerson: Person={}, Authentication={}", person, getContext().getAuthentication());

    addPersonToPeople(null, person);
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").build(person.getId());
    LOGGER.info("PostPerson: Location={}", location);

    return ResponseEntity.created(location).body(person);
  }

  Person reset() {
    people.clear();

    return setup();
  }
}
