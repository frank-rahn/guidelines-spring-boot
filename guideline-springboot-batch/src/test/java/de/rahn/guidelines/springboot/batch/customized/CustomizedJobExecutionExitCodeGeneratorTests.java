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

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.boot.autoconfigure.batch.JobExecutionEvent;

@ExtendWith({MockitoExtension.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CustomizedJobExecutionExitCodeGeneratorTests {

  @Spy
  private CustomizedJobExecutionExitCodeGenerator classUnderTest =
      new CustomizedJobExecutionExitCodeGenerator();

  @Test
  void given_JobExecution_when_get_ExitCode_then_ExitCode_is_0(@Mock JobExecution jobExecution) {
    // Given
    var givenExitStatus = ExitStatus.COMPLETED;
    var expectedErrorCode = 0;

    // When & Then
    assertCustomizedJobExecutionExitCodeGenerator(givenExitStatus, expectedErrorCode, jobExecution);
  }

  @Test
  void given_JobExecution_when_get_ExitCode_then_ExitCode_is_1(@Mock JobExecution jobExecution) {
    // Given
    var givenExitStatus = CustomizedExitStatus.COMPLETED_WITH_ERRORS;
    var expectedErrorCode = 1;

    // When & Then
    assertCustomizedJobExecutionExitCodeGenerator(givenExitStatus, expectedErrorCode, jobExecution);
  }

  @Test
  void given_JobExecution_when_get_ExitCode_then_ExitCode_is_2(@Mock JobExecution jobExecution) {
    // Given
    var givenExitStatus = CustomizedExitStatus.COMPLETED_WITH_SKIPS;
    var expectedErrorCode = 2;

    // When & Then
    assertCustomizedJobExecutionExitCodeGenerator(givenExitStatus, expectedErrorCode, jobExecution);
  }

  @Test
  void given_JobExecution_when_get_ExitCode_then_ExitCode_is_3(@Mock JobExecution jobExecution) {
    // Given
    var givenExitStatus = ExitStatus.NOOP;
    var expectedErrorCode = 3;

    // When & Then
    assertCustomizedJobExecutionExitCodeGenerator(givenExitStatus, expectedErrorCode, jobExecution);
  }

  @Test
  void given_JobExecution_when_get_ExitCode_then_ExitCode_is_4(@Mock JobExecution jobExecution) {
    // Given
    var givenExitStatus = ExitStatus.FAILED;
    var expectedErrorCode = 4;

    // When & Then
    assertCustomizedJobExecutionExitCodeGenerator(givenExitStatus, expectedErrorCode, jobExecution);
  }

  @Test
  void given_JobExecution_when_get_ExitCode_then_ExitCode_is_5(@Mock JobExecution jobExecution) {
    // Given
    var givenExitStatus = ExitStatus.EXECUTING;
    var expectedErrorCode = 5;

    // When & Then
    assertCustomizedJobExecutionExitCodeGenerator(givenExitStatus, expectedErrorCode, jobExecution);
  }

  @Test
  void given_JobExecution_when_get_ExitCode_then_ExitCode_is_6(@Mock JobExecution jobExecution) {
    // Given
    var givenExitStatus = ExitStatus.STOPPED;
    var expectedErrorCode = 6;

    // When & Then
    assertCustomizedJobExecutionExitCodeGenerator(givenExitStatus, expectedErrorCode, jobExecution);
  }

  @Test
  void given_JobExecution_when_get_ExitCode_then_ExitCode_is_7(@Mock JobExecution jobExecution) {
    // Given
    var givenExitStatus = ExitStatus.UNKNOWN;
    var expectedErrorCode = 7;

    // When & Then
    assertCustomizedJobExecutionExitCodeGenerator(givenExitStatus, expectedErrorCode, jobExecution);
  }

  @Test
  void given_JobExecution_when_get_ExitCode_then_ExitCode_is_8(@Mock JobExecution jobExecution) {
    // Given
    var givenExitStatus = new ExitStatus("exitCode", "description");
    var expectedErrorCode = 8;

    // When & Then
    assertCustomizedJobExecutionExitCodeGenerator(givenExitStatus, expectedErrorCode, jobExecution);
  }

  private void assertCustomizedJobExecutionExitCodeGenerator(
      ExitStatus givenExitStatus, int expectedErrorCode, JobExecution jobExecution) {
    // Given
    doReturn(givenExitStatus).when(jobExecution).getExitStatus();
    var jobExecutionEvent = spy(new JobExecutionEvent(jobExecution));

    // When
    classUnderTest.onApplicationEvent(jobExecutionEvent);
    var exitCode = classUnderTest.getExitCode();

    // Then
    verify(jobExecutionEvent, times(1)).getJobExecution();
    verify(jobExecution, times(1)).getExitStatus();
    verify(classUnderTest, times(1)).mapExitStatusToExitCode(givenExitStatus);

    assertThat(exitCode).isEqualTo(expectedErrorCode);
  }
}
