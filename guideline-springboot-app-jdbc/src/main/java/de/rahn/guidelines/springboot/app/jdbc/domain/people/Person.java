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
package de.rahn.guidelines.springboot.app.jdbc.domain.people;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

import de.rahn.guidelines.springboot.app.jdbc.domain.WithUUIDPersistable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author Frank Rahn
 */
@AllArgsConstructor(access = PRIVATE, onConstructor = @__(@PersistenceConstructor))
@RequiredArgsConstructor
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Person extends WithUUIDPersistable {

  @Setter
  private String firstName;

  @NotNull
  @NotBlank
  @NonNull
  private String lastName;

  @Email
  @Setter
  private String emailAddress;

  @DateTimeFormat(iso = DATE)
  @NotNull
  @NonNull
  private LocalDate birthday;

  @EqualsAndHashCode.Exclude
  private Set<Address> addresses = new HashSet<>();

  @CreatedBy
  private String createdBy;

  @CreatedDate
  private LocalDateTime createdDate;

  @LastModifiedBy
  private String lastModifiedBy;

  @LastModifiedDate
  private LocalDateTime lastModifiedDate;
}
