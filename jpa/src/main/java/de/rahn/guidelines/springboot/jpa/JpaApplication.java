package de.rahn.guidelines.springboot.jpa;

import de.rahn.guidelines.springboot.jpa.domain.people.Person;
import de.rahn.guidelines.springboot.jpa.domain.people.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@SpringBootApplication
public class JpaApplication {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JpaApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(JpaApplication.class, args);
	}
	
	@Bean
	@Order(1)
	protected ApplicationRunner initDatabase(PersonRepository repository) {
		return new ApplicationRunner() {
			
			@Override
			@Transactional
			public void run(ApplicationArguments args) {
				Person person = new Person();
				person.setFirstName("Frank");
				person.setLastName("Rahn");
				person.setBirthday(LocalDate.of(1967, 5, 5));
				repository.save(person);
				
				person = new Person();
				person.setFirstName("Martin");
				person.setLastName("Rahn");
				person.setBirthday(LocalDate.of(1979, 3, 25));
				repository.save(person);
			}
		};
	}
	
	@Bean
	@Order(2)
	protected ApplicationRunner usingDatabase(PersonRepository repository) {
		return args -> repository.findByLastName("Rahn").forEach(person -> LOGGER.info(person.toString()));
	}
	
}