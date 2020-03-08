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
package de.rahn.guidelines.springboot.app.jdbc;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ExitCodeExceptionMapper;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Frank Rahn
 */
@SpringBootTest
class AppJdbcApplicationTests {

  @Autowired
  private ExitCodeExceptionMapper exitCodeExceptionMapper;

  @Test
  void givenContext_whenLoads_thenOk() {
    // Empty
  }

  @Test
  void givenContext_whenLoads_thenExitCodeExceptionMapper() {
    // Given
    Throwable throwable = new RuntimeException("Unknown Exception");

    // When
    int exitCode2 = exitCodeExceptionMapper.getExitCode(throwable);

    //noinspection UnnecessaryInitCause
    throwable.initCause(new IllegalArgumentException("Known Exception"));
    int exitCode3 = exitCodeExceptionMapper.getExitCode(throwable);

    // Then
    assertThat(exitCode2).isEqualTo(2);
    assertThat(exitCode3).isEqualTo(3);
  }
}
