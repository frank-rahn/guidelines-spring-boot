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
package de.rahn.guidelines.springboot.batch.customized;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.boot.autoconfigure.batch.JobExecutionEvent;

@ExtendWith({MockitoExtension.class})
class CustomizedJobExecutionExitCodeGeneratorTests {

  @Mock
  private JobExecution jobExecution;

  @Spy
  private CustomizedJobExecutionExitCodeGenerator classUnderTest =
      new CustomizedJobExecutionExitCodeGenerator();

  @Test
  void givenJobExecution_whenGetExitCode_thenExitCodeIs0() {
    // Given
    ExitStatus givenExitStatus = ExitStatus.COMPLETED;
    int expectedErrorCode = 0;

    // When & Then
    assertCustomizedJobExecutionExitCodeGenerator(givenExitStatus, expectedErrorCode);
  }

  @Test
  void givenJobExecution_whenGetExitCode_thenExitCodeIs1() {
    // Given
    ExitStatus givenExitStatus = CustomizedExitStatus.COMPLETED_WITH_ERRORS;
    int expectedErrorCode = 1;

    // When & Then
    assertCustomizedJobExecutionExitCodeGenerator(givenExitStatus, expectedErrorCode);
  }

  @Test
  void givenJobExecution_whenGetExitCode_thenExitCodeIs2() {
    // Given
    ExitStatus givenExitStatus = CustomizedExitStatus.COMPLETED_WITH_SKIPS;
    int expectedErrorCode = 2;

    // When & Then
    assertCustomizedJobExecutionExitCodeGenerator(givenExitStatus, expectedErrorCode);
  }

  @Test
  void givenJobExecution_whenGetExitCode_thenExitCodeIs3() {
    // Given
    ExitStatus givenExitStatus = ExitStatus.NOOP;
    int expectedErrorCode = 3;

    // When & Then
    assertCustomizedJobExecutionExitCodeGenerator(givenExitStatus, expectedErrorCode);
  }

  @Test
  void givenJobExecution_whenGetExitCode_thenExitCodeIs4() {
    // Given
    ExitStatus givenExitStatus = ExitStatus.FAILED;
    int expectedErrorCode = 4;

    // When & Then
    assertCustomizedJobExecutionExitCodeGenerator(givenExitStatus, expectedErrorCode);
  }

  @Test
  void givenJobExecution_whenGetExitCode_thenExitCodeIs5() {
    // Given
    ExitStatus givenExitStatus = ExitStatus.EXECUTING;
    int expectedErrorCode = 5;

    // When & Then
    assertCustomizedJobExecutionExitCodeGenerator(givenExitStatus, expectedErrorCode);
  }

  @Test
  void givenJobExecution_whenGetExitCode_thenExitCodeIs6() {
    // Given
    ExitStatus givenExitStatus = ExitStatus.STOPPED;
    int expectedErrorCode = 6;

    // When & Then
    assertCustomizedJobExecutionExitCodeGenerator(givenExitStatus, expectedErrorCode);
  }

  @Test
  void givenJobExecution_whenGetExitCode_thenExitCodeIs7() {
    // Given
    ExitStatus givenExitStatus = ExitStatus.UNKNOWN;
    int expectedErrorCode = 7;

    // When & Then
    assertCustomizedJobExecutionExitCodeGenerator(givenExitStatus, expectedErrorCode);
  }

  @Test
  void givenJobExecution_whenGetExitCode_thenExitCodeIs8() {
    // Given
    ExitStatus givenExitStatus = new ExitStatus("exitCode", "description");
    int expectedErrorCode = 8;

    // When & Then
    assertCustomizedJobExecutionExitCodeGenerator(givenExitStatus, expectedErrorCode);
  }

  private void assertCustomizedJobExecutionExitCodeGenerator(
      ExitStatus givenExitStatus, int expectedErrorCode) {
    // Given
    doReturn(givenExitStatus).when(jobExecution).getExitStatus();
    JobExecutionEvent jobExecutionEvent = spy(new JobExecutionEvent(jobExecution));

    // When
    classUnderTest.onApplicationEvent(jobExecutionEvent);
    int exitCode = classUnderTest.getExitCode();

    // Then
    verify(jobExecutionEvent, times(1)).getJobExecution();
    verify(jobExecution, times(1)).getExitStatus();
    verify(classUnderTest, times(1)).mapExitStatusToExitCode(givenExitStatus);

    assertThat(exitCode).isEqualTo(expectedErrorCode);
  }
}
