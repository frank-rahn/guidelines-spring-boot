package de.rahn.guidelines.springboot.jpa.domain.people;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, String> {
	
	List<Person> findByLastName(String lastname);
	
}