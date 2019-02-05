package de.rahn.guidelines.springboot.rest.api;

import de.rahn.guidelines.springboot.rest.domain.people.Person;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping(path = "/api/people")
public class PeopleController {
	
	private List<Person> people = new ArrayList<>();
	
	@PostConstruct
	protected void setup() {
		Person person=new Person();
		person.setId(UUID.randomUUID().toString());
		person.setFirstName("Frank");
		person.setLastName("Rahn");
		person.setBirthday(LocalDate.of(1967, 5,5));
		people.add(person);
	}
	
	@GetMapping
	public List<Person> getAllPeople() {
		return people;
	}
	
	@GetMapping(path = "/{id}")
	public ResponseEntity<Person> getPersonById(@PathVariable("id") String id) {
		
		return ResponseEntity.of(
				people.stream().filter(person -> person.getId().equals(id)).findFirst()
		);
	}
	
	@PostMapping
	public Person newPerson(@RequestBody @Valid Person person) {
		person.setId(UUID.randomUUID().toString());
		people.add(person);
		
		return person;
	}
	
}