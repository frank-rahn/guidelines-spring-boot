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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class UserImportPersonProcessor implements ItemProcessor<Person, Person> {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserImportPersonProcessor.class);

  @Override
  public Person process(Person person) {
    Person transformedPerson =
        new Person(person.getFirstName().toUpperCase(), person.getLastName().toUpperCase());

    LOGGER.info("Converting (" + person + ") into (" + transformedPerson + ")");

    return transformedPerson;
  }
}
