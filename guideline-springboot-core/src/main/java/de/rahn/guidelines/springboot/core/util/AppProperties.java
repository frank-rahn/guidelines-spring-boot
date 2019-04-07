package de.rahn.guidelines.springboot.core.util;

import static org.apache.commons.lang3.builder.ToStringStyle.JSON_STYLE;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "app")
@Validated
public class AppProperties {

  private final List<Person> people = new ArrayList<>();

  public List<Person> getPeople() {
    return people;
  }

  public void setPeople(List<Person> people) {
    this.people.clear();

    if (people != null) {
      this.people.addAll(people);
    }
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, JSON_STYLE);
  }

  @Validated
  public static class Person {

    private String id;

    private String firstName;

    @NotNull
    @NotBlank
    private String lastName;

    @Email
    private String emailAddress;

    @NotNull
    private LocalDate birthday;

    @Override
    public String toString() {
      return ToStringBuilder.reflectionToString(this, JSON_STYLE);
    }

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public String getFirstName() {
      return firstName;
    }

    public void setFirstName(String firstName) {
      this.firstName = firstName;
    }

    public String getLastName() {
      return lastName;
    }

    public void setLastName(String lastName) {
      this.lastName = lastName;
    }

    public String getEmailAddress() {
      return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
      this.emailAddress = emailAddress;
    }

    public LocalDate getBirthday() {
      return birthday;
    }

    public void setBirthday(LocalDate birthday) {
      this.birthday = birthday;
    }
  }
}
