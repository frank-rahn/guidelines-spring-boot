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
package de.rahn.guidelines.springboot.batch.report.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import de.rahn.guidelines.springboot.batch.report.ReportJobExecutionListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

@ExtendWith(MockitoExtension.class)
class ReportHelperTest {

  @Mock
  private Appender<ILoggingEvent> mockAppender;

  @Spy
  private ReportHelper classUnderTest = new ReportHelper();

  @BeforeEach
  void setUp() {
    Logger logger = (Logger) LoggerFactory.getLogger(ReportJobExecutionListener.REPORT);
    logger.addAppender(mockAppender);
  }

  @Test
  void givenMessage_whenReportInformation_thenLoggingMessageAsInfo() {
    // Given
    String message = "test";

    // When
    classUnderTest.reportInformation(message);

    // Then
    verify(mockAppender)
        .doAppend(
            argThat(
                argument -> {
                  assertThat(argument.getMessage()).isEqualTo(message);
                  assertThat(argument.getLevel()).isEqualTo(Level.INFO);
                  return true;
                }));
  }

  @Test
  void givenMessage_whenReportWarning_thenLoggingMessageAsWarn() {
    // Given
    String message = "test";

    // When
    classUnderTest.reportWarning(message);

    // Then
    verify(mockAppender)
        .doAppend(
            argThat(
                argument -> {
                  assertThat(argument.getMessage()).isEqualTo(message);
                  assertThat(argument.getLevel()).isEqualTo(Level.WARN);
                  return true;
                }));
  }

  @Test
  void givenMessage_whenReportError_thenLoggingMessageAsError() {
    // Given
    String message = "test";

    // When
    classUnderTest.reportError(message);

    // Then
    verify(mockAppender)
        .doAppend(
            argThat(
                argument -> {
                  assertThat(argument.getMessage()).isEqualTo(message);
                  assertThat(argument.getLevel()).isEqualTo(Level.ERROR);
                  return true;
                }));
  }
}
