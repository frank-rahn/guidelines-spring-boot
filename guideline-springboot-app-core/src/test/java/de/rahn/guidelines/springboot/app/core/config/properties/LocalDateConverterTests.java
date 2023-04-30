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

import static java.time.format.DateTimeFormatter.ISO_DATE;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class LocalDateConverterTests {

  private final LocalDateConverter classUnderTests = new LocalDateConverter();

  @Test
  void given_LocalDate_as_String_when_convert_then_is_LocalDate() {
    // Given
    var localDate = LocalDate.now();
    var dateString = localDate.format(ISO_DATE);

    // When
    var result = classUnderTests.convert(dateString);

    // Then
    assertThat(result).isEqualTo(localDate);
  }

  @SuppressWarnings("DataFlowIssue")
  @Test
  void given_null_when_convert_then_is_null() {
    // Given
    // When
    // Then
    assertThatIllegalArgumentException()
        .isThrownBy(() -> classUnderTests.convert(null))
        .withMessage("Argument source ist null");
  }
}
