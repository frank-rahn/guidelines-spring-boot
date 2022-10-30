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
package de.rahn.guidelines.springboot.batch.job.userimport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.InstanceOfAssertFactories.LOCAL_DATE;

import de.rahn.guidelines.springboot.batch.report.support.ReportHelper;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserImportPersonProcessorTests {

  @Mock
  @SuppressWarnings("unused")
  private ReportHelper reportHelper;

  @InjectMocks private UserImportPersonProcessor classUnderTest;

  @Test
  void given_Person_and_not_filter_user_and_not_skip_user_when_process_then_returns_Person_is_ok() {
    // Given
    var person = new Person("firstName", "lastName");
    person.setEmailAddress("emailAddress");
    person.setBirthday(LocalDate.now());
    classUnderTest.filterUser = "FILTER";
    classUnderTest.skipUser = "SKIP";

    // When
    var result = classUnderTest.process(person);

    // Then
    assertThat(result)
        .extracting(Person::getFirstName)
        .isEqualTo(person.getFirstName().toUpperCase());
    assertThat(result)
        .extracting(Person::getLastName)
        .isEqualTo(person.getLastName().toUpperCase());
    assertThat(result).extracting(Person::getEmailAddress).isEqualTo(person.getEmailAddress());
    assertThat(result).extracting(Person::getBirthday, LOCAL_DATE).isToday();
  }

  @Test
  void given_Person_and_filter_user_and_not_skip_user_when_process_and_filter_then_returns_null() {
    // Given
    var person = new Person("firstName", "lastName");
    person.setEmailAddress("emailAddress");
    person.setBirthday(LocalDate.now());
    classUnderTest.filterUser = "LastNaMe";
    classUnderTest.skipUser = "SKIP";

    // When
    var result = classUnderTest.process(person);

    // Then
    assertThat(result).isNull();
  }

  @Test
  void
      given_Person_and_not_filter_user_and_skip_user_when_process_and_filter_then_throws_RuntimeException() {
    // Given
    var person = new Person("firstName", "lastName");
    person.setEmailAddress("emailAddress");
    person.setBirthday(LocalDate.now());
    classUnderTest.filterUser = "FILTER";
    classUnderTest.skipUser = "LastNaMe";

    // When and Then
    assertThatExceptionOfType(RuntimeException.class)
        .isThrownBy(() -> classUnderTest.process(person))
        .withMessage("Skip LastNaMe");
  }
}
