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
package de.rahn.guidelines.springboot.app.core.config.properties;

import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

/**
 * @author Frank Rahn
 */
@ConfigurationProperties(prefix = "app")
@ConstructorBinding
@AllArgsConstructor
@Getter
@ToString
@Validated
public class AppProperties {

  private final List<Person> people;

  @ConstructorBinding
  @AllArgsConstructor
  @Getter
  @ToString
  @Validated
  public static class Person {

    @ToString.Include(rank = 1)
    private String id;

    private String firstName;

    @ToString.Include(rank = 2)
    @NotNull
    @NotBlank
    private String lastName;

    @Email private String emailAddress;

    @NotNull private LocalDate birthday;
  }
}
