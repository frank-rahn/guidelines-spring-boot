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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.ISBN;

/**
 * @author Frank Rahn
 */
@ApiModel(description = "Ein Buch")
@XmlRootElement
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Book {

  @ApiModelProperty(value = "Die Artikelnummer", required = true, example = "4711")
  @NotBlank
  private final String id;

  @ApiModelProperty(value = "Die zehnstellige ISBN", required = true, example = "0123456789")
  @ISBN(type = ISBN_10)
  private final String isbn;

  @ApiModelProperty("Der Titel des Buches sollte mindestens 5 Zeichen umfassen")
  @Size(min = 5)
  private String title;
}
