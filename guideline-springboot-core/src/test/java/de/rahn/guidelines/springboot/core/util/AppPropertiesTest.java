package de.rahn.guidelines.springboot.core.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
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
class AppPropertiesTest {

  @Autowired(required = false)
  private AppProperties properties;

  @Test
  void shouldPopulateAppProperties() {
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
