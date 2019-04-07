package de.rahn.guidelines.springboot.rest.api;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

import de.rahn.guidelines.springboot.rest.domain.people.Person;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

@RestController
@RequestMapping(path = "/api/people", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = {"People"})
public class PeopleController {

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
  protected void setup() {
    Person person = new Person();
    person.setId(UUID.randomUUID().toString());
    person.setFirstName("Frank");
    person.setLastName("Rahn");
    person.setBirthday(LocalDate.of(1967, 5, 5));

    addPersonToPeople(person);
  }

  @GetMapping
  @ApiOperation("Liefert alle Personen")
  public Collection<Person> getAllPeople() {
    LOGGER.info("GetAllPeople: Authentication={}", getContext().getAuthentication());

    return people.values();
  }

  @GetMapping(path = "/{id}")
  @ApiOperation("Suche die Person mit der Id")
  public ResponseEntity<Person> getPersonById(
      @PathVariable("id") @ApiParam(value = "Die UUID der gesuchten Person", required = true)
          String id) {
    LOGGER.info("GetPersonById: Id={}, Authentication={}", id, getContext().getAuthentication());

    return ResponseEntity.of(Optional.ofNullable(people.get(id)));
  }

  @DeleteMapping(path = "/{id}")
  @ApiOperation("Lösche die Person mit der Id")
  public ResponseEntity<Person> deletePersonById(
      @PathVariable("id") @ApiParam(value = "Die UUID der zu löschenden Person", required = true)
          String id) {
    LOGGER.info("DeletePersonById: Id={}, Authentication={}", id, getContext().getAuthentication());

    return ResponseEntity.of(Optional.ofNullable(people.remove(id)));
  }

  @PostMapping
  @ApiOperation("Füge eine Person hinzu")
  public Person postPerson(
      @RequestBody @Valid @ApiParam(value = "Die neue Person", required = true) Person person) {
    LOGGER.info(
        "PostPerson: Person={}, Authentication={}", person, getContext().getAuthentication());

    return addPersonToPeople(null, person);
  }

  @PutMapping(path = "/{id}")
  @ApiOperation("Füge die Person mit der Id hinzu")
  public Person putPerson(
      @PathVariable("id") @ApiParam(value = "Die UUID der neuen Person", required = true) String id,
      @RequestBody @Valid @ApiParam(value = "Die neue Person", required = true) Person person) {
    LOGGER.info(
        "PutPerson: Id={}, Person={}, Authentication={}",
        id,
        person,
        getContext().getAuthentication());

    return addPersonToPeople(id, person);
  }
}
