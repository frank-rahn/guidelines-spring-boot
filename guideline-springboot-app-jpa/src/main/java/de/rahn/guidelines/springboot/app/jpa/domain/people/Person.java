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
package de.rahn.guidelines.springboot.app.jpa.domain.people;

import static javax.persistence.AccessType.FIELD;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Access;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author Frank Rahn
 */
@Access(FIELD)
@Entity
@Audited
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class Person extends AbstractAuditing {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(generator = "uuid")
  @ToString.Include(rank = 1)
  private String id;

  private String firstName;

  @Basic(optional = false)
  @NotNull
  @NotBlank
  @NonNull
  @ToString.Include(rank = 2)
  private String lastName;

  @Email private String emailAddress;

  @Basic(optional = false)
  @DateTimeFormat(iso = DATE)
  @NotNull
  @NonNull
  private LocalDate birthday;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    var person = (Person) o;
    return id != null && Objects.equals(id, person.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}
