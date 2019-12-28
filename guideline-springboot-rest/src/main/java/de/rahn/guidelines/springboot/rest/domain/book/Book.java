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
package de.rahn.guidelines.springboot.rest.domain.book;

import static org.hibernate.validator.constraints.ISBN.Type.ISBN_10;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.ISBN;

/**
 * @author Frank Rahn
 */
@Schema(description = "Ein Buch")
@XmlRootElement
@Getter
@Setter
@EqualsAndHashCode
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Book {

  @Schema(description = "Die Artikelnummer", example = "7b6a3a11-0873-46b5-9d62-05fe6a436ea9")
  @NotBlank
  @NonNull
  private String id;

  @Schema(description = "Die zehnstellige ISBN", example = "0123456789")
  @NotNull
  @ISBN(type = ISBN_10)
  @NonNull
  private String isbn;

  @Schema(
      description = "Der Titel des Buches sollte mindestens 5 Zeichen umfassen",
      example = "Mein Buch")
  @NotNull
  @Size(min = 5)
  private String title;
}
