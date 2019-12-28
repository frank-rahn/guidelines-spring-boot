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
package de.rahn.guidelines.springboot.rest.domain.people;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author Frank Rahn
 */
@Schema(description = "Eine Person")
@XmlRootElement
@Getter
@Setter
@EqualsAndHashCode
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Person {

  @Schema(description = "Die UUID der Person", example = "7b6a3a11-0873-46b5-9d62-05fe6a436ea9")
  private String id;

  @Schema(description = "Der Vorname der Person", example = "Martin")
  private String firstName;

  @NotBlank
  @Schema(description = "Der Nachname der Person", example = "Rahn")
  @NonNull
  private String lastName;

  @Email
  @Schema(description = "Die E-Mail der Person", example = "martin@frank-rahn.de")
  private String emailAddress;

  @DateTimeFormat(iso = DATE)
  @NotNull
  @Schema(description = "Das Geburtsdatum der Person", example = "1979-03-25")
  @NonNull
  private LocalDate birthday;

  @Schema(description = "Allgemeine Informationen über die Person", example = "[\"Tester\"]")
  private List<String> infos;
}
