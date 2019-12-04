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

import static org.assertj.core.api.Assertions.assertThat;

import de.rahn.guidelines.springboot.batch.report.support.ReportHelper;
import java.time.LocalDate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserImportPersonProcessorTest {

  @Mock
  private ReportHelper reportHelper;

  @InjectMocks
  private UserImportPersonProcessor classUnderTest;

  @Test
  void givenPersonAndNotFilterUserAndNotSkipUser_whenProcess_thenReturnPersonIsOk() {
    // Given
    final Person person = new Person("lastName", "firstName");
    person.setEmailAddress("emailAddress");
    person.setBirthday(LocalDate.now());
    classUnderTest.filterUser = "FILTER";
    classUnderTest.skipUser = "SKIP";

    // When
    Person result = classUnderTest.process(person);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getFirstName()).isEqualTo(person.getFirstName().toUpperCase());
    assertThat(result.getLastName()).isEqualTo(person.getLastName().toUpperCase());
    assertThat(result.getEmailAddress()).isEqualTo(person.getEmailAddress());
    assertThat(result.getBirthday()).isToday();
  }

  @Test
  void givenPersonAndFilterUserAndNotSkipUser_whenProcessAndFilter_thenReturnNull() {
    // Given
    final Person person = new Person("lastName", "firstName");
    person.setEmailAddress("emailAddress");
    person.setBirthday(LocalDate.now());
    classUnderTest.filterUser = "LastNaMe";
    classUnderTest.skipUser = "SKIP";

    // When
    Person result = classUnderTest.process(person);

    // Then
    assertThat(result).isNull();
  }

  @Test
  void givenPersonAndNotFilterUserAndSkipUser_whenProcessAndFilter_thenReturnPersonIsOk() {
    // Given
    final Person person = new Person("lastName", "firstName");
    person.setEmailAddress("emailAddress");
    person.setBirthday(LocalDate.now());
    classUnderTest.filterUser = "FILTER";
    classUnderTest.skipUser = "LastNaMe";

    // When and Then
    Assertions.assertThatExceptionOfType(RuntimeException.class)
        .isThrownBy(() -> classUnderTest.process(person))
        .withMessage("Skip LastNaMe");
  }
}
