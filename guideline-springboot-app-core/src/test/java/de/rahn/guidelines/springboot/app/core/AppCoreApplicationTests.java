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
package de.rahn.guidelines.springboot.app.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.ExitCodeExceptionMapper;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Frank Rahn
 */
@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AppCoreApplicationTests {

  @Autowired private List<ApplicationRunner> applicationRunnerList;

  @Autowired private ExitCodeExceptionMapper exitCodeExceptionMapper;

  @Test
  void given_Context_when_loads_then_is_ok() {
    // Empty
  }

  @Test
  void given_Context_when_loads_then_wrong_ApplicationRunner() {
    // Given
    var applicationArguments = new DefaultApplicationArguments("--go-wrong");

    // When
    assertThat(applicationRunnerList).hasSize(2);

    // Then
    assertThatIllegalArgumentException()
        .isThrownBy(() -> applicationRunnerList.get(1).run(applicationArguments))
        .withMessageContaining("go-wrong");
  }

  @Test
  @SuppressWarnings("UnnecessaryInitCause")
  void given_Context_when_loads_then_correct_Exitcodes() {
    // Given
    var throwable = new RuntimeException("Unknown Exception");

    // When
    var exitCode2 = exitCodeExceptionMapper.getExitCode(throwable);

    throwable.initCause(new IllegalArgumentException("Known Exception"));
    var exitCode3 = exitCodeExceptionMapper.getExitCode(throwable);

    // Then
    assertThat(exitCode2).isEqualTo(2);
    assertThat(exitCode3).isEqualTo(3);
  }
}
