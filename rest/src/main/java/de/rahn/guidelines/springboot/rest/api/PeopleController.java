package de.rahn.guidelines.springboot.rest.api;

import de.rahn.guidelines.springboot.rest.domain.people.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@RestController
@RequestMapping(path = "/api/people")
public class PeopleController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PeopleController.class);
	
	private Map<String, Person> people = new HashMap<>();
	
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
	public Collection<Person> getAllPeople() {
		LOGGER.info("Authentication: {}", getContext().getAuthentication());
		return people.values();
	}
	
	@GetMapping(path = "/{id}")
	public ResponseEntity<Person> getPersonById(@PathVariable("id") String id) {
		LOGGER.info("Id: {}, Authentication: {}", id, getContext().getAuthentication());
		return ResponseEntity.of(Optional.ofNullable(people.get(id)));
	}
	
	@DeleteMapping(path = "/{id}")
	public ResponseEntity<Person> removePersonById(@PathVariable("id") String id) {
		LOGGER.info("Id: {}, Authentication: {}", id, getContext().getAuthentication());
		return ResponseEntity.of(Optional.ofNullable(people.remove(id)));
	}
	
	@PostMapping
	public Person newPerson(@RequestBody @Valid Person person) {
		LOGGER.info("Person: {}, Authentication: {}", person, getContext().getAuthentication());
		return addPersonToPeople(null, person);
	}
	
	@PutMapping(path = "/{id}")
	public Person newPerson(@PathVariable("id") String id, @RequestBody @Valid Person person) {
		LOGGER.info("Id: {}, Person: {}, Authentication: {}", id, person, getContext().getAuthentication());
		return addPersonToPeople(id, person);
	}
	
}