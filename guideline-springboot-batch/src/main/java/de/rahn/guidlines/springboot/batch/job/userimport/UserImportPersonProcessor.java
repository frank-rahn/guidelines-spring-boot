/*
 * Copyright (c) 2019-2019 the original author or authors.
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
package de.rahn.guidlines.springboot.batch.job.userimport;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Frank Rahn
 */
@Component
@StepScope
@Slf4j
public class UserImportPersonProcessor implements ItemProcessor<Person, Person> {

  @Value("#{jobParameters['filter']?:'FILTER'}")
  private String filterUser;

  @Value("#{jobParameters['skip']?:'SKIP'}")
  private String skipUser;

  @Override
  public Person process(Person person) {
    if (filterUser.toUpperCase().equals(person.getLastName().toUpperCase())) {
      // Filter this record
      return null;
    } else if (skipUser.toUpperCase().equals(person.getLastName().toUpperCase())) {
      // Skip this record
      throw new RuntimeException("Skip " + skipUser);
    }

    Person transformedPerson =
        new Person(person.getFirstName().toUpperCase(), person.getLastName().toUpperCase());

    LOGGER.info("Converting (" + person + ") into (" + transformedPerson + ")");

    return transformedPerson;
  }
}
