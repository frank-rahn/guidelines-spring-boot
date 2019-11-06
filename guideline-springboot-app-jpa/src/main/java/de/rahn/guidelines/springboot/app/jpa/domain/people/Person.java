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
package de.rahn.guidelines.springboot.app.jpa.domain.people;

import static javax.persistence.AccessType.FIELD;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

import java.time.LocalDate;
import java.util.Optional;
import javax.persistence.Access;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

@Access(FIELD)
@Entity
public class Person extends AbstractAuditing {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(generator = "uuid")
  private String id;

  private String firstName;

  @Basic(optional = false)
  @NotNull
  @NotBlank
  private String lastName;

  @Email
  private String emailAddress;

  @Basic(optional = false)
  @NotNull
  @DateTimeFormat(iso = DATE)
  private LocalDate birthday;

  protected Person() {
    // default bzw. no-arg constructor
  }

  public Person(String lastName, LocalDate birthday) {
    // required-args constructor
    this();

    this.lastName = lastName;
    this.birthday = birthday;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Optional<String> getFirstName() {
    return Optional.ofNullable(firstName);
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

  public Optional<String> getEmailAddress() {
    return Optional.ofNullable(emailAddress);
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
