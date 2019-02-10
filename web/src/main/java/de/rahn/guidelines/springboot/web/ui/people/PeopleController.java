package de.rahn.guidelines.springboot.web.ui.people;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Controller
@RequestMapping(path = "/people")
public class PeopleController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PeopleController.class);
	
	private RestTemplate template;
	
	public PeopleController(RestTemplate template) {
		this.template = template;
	}
	
	@GetMapping
	public String getAllPeople(Model model) {
		LOGGER.info("GetAllPeople: Authentication={}", getContext().getAuthentication());
		
		ResponseEntity<List<Person>> people = template.exchange(
				"/",
				GET,
				null,
				new ParameterizedTypeReference<List<Person>>() {
				});
		
		model.addAttribute("people", people.getBody());
		
		return "people";
	}
	
	@GetMapping(path = "/{id}")
	public String getPersonById(@PathVariable("id") String id, Model model) {
		LOGGER.info("GetPersonById: Id={}, Authentication={}", id, getContext().getAuthentication());
		
		Person person = template.getForObject("/" + id, Person.class);
		
		model.addAttribute("person", person);
		
		return "person";
	}
	
}