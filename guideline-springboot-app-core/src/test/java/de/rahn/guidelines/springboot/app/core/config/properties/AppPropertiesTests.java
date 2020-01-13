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
package de.rahn.guidelines.springboot.app.core.config.properties;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author Frank Rahn
 */
@ExtendWith({SpringExtension.class})
@SpringBootTest(
    properties = {
        "app.people[0].id=4711",
        "app.people[0].firstName=Frank",
        "app.people[0].lastName=Rahn",
        "app.people[0].birthday=1967-05-05",
        "app.people[0].emailAddress=frank@frank-rahn.de",
        "app.people[1].id=4712",
        "app.people[1].firstName=Martin",
        "app.people[1].lastName=Rahn",
        "app.people[1].birthday=1979-03-25",
        "app.people[1].emailAddress=martin@frank-rahn.de"
    })
class AppPropertiesTests {

  @Autowired(required = false)
  private AppProperties properties;

  @Test
  void givenContext_whenLoads_thenShouldPopulateAppProperties() {
    // Then
    assertThat(properties).isNotNull();
    assertThat(properties.getPeople()).hasSize(2);
    assertThat(properties.getPeople().get(0)).isNotNull();
    assertThat(properties.getPeople().get(0).getId()).isEqualTo("4711");
    assertThat(properties.getPeople().get(0).getFirstName()).isEqualTo("Frank");
    assertThat(properties.getPeople().get(0).getLastName()).isEqualTo("Rahn");
    assertThat(properties.getPeople().get(0).getBirthday()).isEqualTo(LocalDate.of(1967, 5, 5));
    assertThat(properties.getPeople().get(0).getEmailAddress()).isEqualTo("frank@frank-rahn.de");
    assertThat(properties.getPeople().get(1)).isNotNull();
    assertThat(properties.getPeople().get(1).getId()).isEqualTo("4712");
    assertThat(properties.getPeople().get(1).getFirstName()).isEqualTo("Martin");
    assertThat(properties.getPeople().get(1).getLastName()).isEqualTo("Rahn");
    assertThat(properties.getPeople().get(1).getBirthday()).isEqualTo(LocalDate.of(1979, 3, 25));
    assertThat(properties.getPeople().get(1).getEmailAddress()).isEqualTo("martin@frank-rahn.de");
  }
}
