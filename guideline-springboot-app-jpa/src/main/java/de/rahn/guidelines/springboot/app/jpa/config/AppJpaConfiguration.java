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
package de.rahn.guidelines.springboot.app.jpa.config;

import de.rahn.guidelines.springboot.app.jpa.domain.people.Person;
import de.rahn.guidelines.springboot.app.jpa.domain.people.PersonRepository;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.ExitCodeExceptionMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Frank Rahn
 */
@Configuration
@Slf4j
class AppJpaConfiguration {

  @Bean
  @Order(1)
  ApplicationRunner initDatabase(PersonRepository repository) {
    return args -> {
      var person = new Person("Rahn", LocalDate.of(1967, 5, 5));
      person.setFirstName("Frank");
      repository.save(person);

      person.setEmailAddress("frank@frank-rahn.de");
      repository.save(person);

      person = new Person("Rahn", LocalDate.of(1979, 3, 25));
      person.setFirstName("Martin");
      repository.save(person);
    };
  }

  @Bean
  @Order(2)
  ApplicationRunner usingDatabase(PersonRepository repository) {
    return args ->
        repository.findByLastName("Rahn").stream()
            .map(Person::getId)
            .flatMap(id -> repository.findRevisions(id).stream())
            .forEach(revision -> LOGGER.info(revision.toString()));
  }

  @Bean
  @Order(3)
  @PersistenceContext
  ApplicationRunner usingEnvers(final EntityManager entityManager) {
    return new ApplicationRunner() {
      @Override
      @Transactional
      public void run(ApplicationArguments args) {
        var auditReader = AuditReaderFactory.get(entityManager);
        var auditQuery =
            auditReader
                .createQuery()
                .forRevisionsOfEntity(Person.class, false, true)
                .add(AuditEntity.property("lastName").eq("Rahn"));

        List<?> results = auditQuery.getResultList();

        results.stream()
            .filter(result -> result instanceof Object[])
            .map(result -> (Object[]) result)
            .filter(revisionArray -> revisionArray.length == 3)
            .forEach(
                revisionArray -> {
                  LOGGER.info("Entity: {}", revisionArray[0]);
                  LOGGER.info("Revision: {}", revisionArray[1]);
                  LOGGER.info("RevisionType: {}", revisionArray[2]);
                });
      }
    };
  }

  @Bean
  ExitCodeExceptionMapper exitCodeExceptionMapper() {
    return exception -> {
      var cause = exception.getCause();

      if (cause == null) {
        cause = exception;
      }

      if (cause instanceof IllegalArgumentException) {
        return 3;
      }

      // Unknown Exception
      return 2;
    };
  }
}
