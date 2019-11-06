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
package de.rahn.guidelines.springboot.app.core.util;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.JSON_STYLE;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    return reflectionToString(this, JSON_STYLE);
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
      return reflectionToString(this, JSON_STYLE);
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
