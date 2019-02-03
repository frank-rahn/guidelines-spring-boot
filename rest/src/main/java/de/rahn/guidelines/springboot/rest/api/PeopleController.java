package de.rahn.guidelines.springboot.rest.api;

import de.rahn.guidelines.springboot.rest.domain.people.Person;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/people")
public class PeopleController {
	
	private List<Person> people = new ArrayList<>();
	
	@GetMapping
	public List<Person> people() {
		return people;
	}
	
	@GetMapping(path = "/{id}")
	public ResponseEntity<Person> person(@PathVariable("id") String id) {
		
		return ResponseEntity.of(
				people.stream().filter(person -> person.getId().equals(id)).findFirst()
		);
	}
	
	@PostMapping
	public Person person(@RequestBody @Valid Person person) {
		person.setId(UUID.randomUUID().toString());
		people.add(person);
		
		return person;
	}
	
}