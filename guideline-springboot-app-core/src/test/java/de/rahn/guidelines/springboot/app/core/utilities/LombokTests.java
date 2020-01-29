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
package de.rahn.guidelines.springboot.app.core.utilities;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author Frank Rahn
 */
@Slf4j
class LombokTests {

  @Test
  void givenObjects_whenToString_thenOutput() {
    // Given
    Whole whole = newWhole();

    // When
    String result = whole.toString();

    // Then
    assertThat(result).isNotBlank();
    LOGGER.info(result);
  }

  @Test
  void givenObjects_whenHasCode_thenOk() {
    // Given
    Whole whole = newWhole();

    // When
    int result = whole.hashCode();

    // Then
    assertThat(result).isGreaterThan(0);
    LOGGER.info(Integer.toString(result));
  }

  @Test
  void givenObjects_whenEquals_thenTrue() {
    // Given
    Whole whole1 = newWhole();
    Whole whole2 = newWhole();

    // When
    boolean result = whole1.equals(whole2);

    // Then
    assertThat(result).isTrue();
  }

  private static Whole newWhole() {
    Whole whole = new Whole();
    whole.setId("4711");
    whole.getParts().add(new Part("1", whole));
    whole.getParts().add(new Part("2", whole));
    whole.getParts().add(new Part("3", whole));
    whole.getParts().add(new Part("4", whole));
    return whole;
  }

  @Getter
  @Setter
  @ToString
  @EqualsAndHashCode
  static class Whole {

    private String id;

    private List<Part> parts = new ArrayList<>();
  }

  @Getter
  @Setter
  @AllArgsConstructor
  @ToString
  @EqualsAndHashCode
  static class Part {

    private String id;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Whole whole;
  }
}
