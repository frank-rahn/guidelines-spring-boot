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
package de.rahn.guidelines.springboot.batch.job.userimport;

import de.rahn.guidelines.springboot.batch.report.support.ReportHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Frank Rahn
 */
@Component
@StepScope
@RequiredArgsConstructor
public class UserImportPersonProcessor implements ItemProcessor<Person, Person> {

  @Value("#{jobParameters['filter']?:'FILTER'}")
  String filterUser;

  @Value("#{jobParameters['skip']?:'SKIP'}")
  String skipUser;

  private final ReportHelper reportHelper;

  @Override
  public Person process(Person person) {
    if (filterUser.toUpperCase().equals(person.getLastName().toUpperCase())) {
      reportHelper.reportWarning("Filter Person:\n" + person);
      return null;
    } else if (skipUser.toUpperCase().equals(person.getLastName().toUpperCase())) {
      reportHelper.reportError("Skip Person:\n" + person);
      throw new RuntimeException("Skip " + skipUser);
    }

    var transformedPerson =
        new Person(person.getLastName().toUpperCase(), person.getFirstName().toUpperCase())
            .withEmailAddress(person.getEmailAddress())
            .withBirthday(person.getBirthday());

    reportHelper.reportInformation("Converting Persons:\n" + person + " into " + transformedPerson);

    return transformedPerson;
  }
}
