package de.rahn.guidelines.springboot.jpa.domain.people;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, String> {

  List<Person> findByLastName(String lastname);
}
