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

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.JSON_STYLE;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author Frank Rahn
 */
@ApiModel(description = "Eine Person")
@XmlRootElement
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Person {

  @ApiModelProperty("Die UUID der Person")
  private String id;

  @ApiModelProperty("Der Vorname der Person")
  private String firstName;

  @NotNull
  @NotBlank
  @ApiModelProperty(value = "Der Nachname der Person", required = true)
  @NonNull
  private String lastName;

  @Email
  @ApiModelProperty("Die E-Mail der Person")
  private String emailAddress;

  @DateTimeFormat(iso = DATE)
  @NotNull
  @ApiModelProperty(value = "Das Geburtsdatum der Person", required = true)
  @NonNull
  private LocalDate birthday;

  @ApiModelProperty("Allgemeine Informationen über die Person")
  private List<String> infos;

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    return reflectionEquals(this, obj, false);
  }

  @Override
  public int hashCode() {
    return reflectionHashCode(this, false);
  }

  @Override
  public String toString() {
    return reflectionToString(this, JSON_STYLE);
  }
}
