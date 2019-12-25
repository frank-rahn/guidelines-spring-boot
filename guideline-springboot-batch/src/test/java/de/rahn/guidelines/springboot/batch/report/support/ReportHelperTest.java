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

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import de.rahn.guidelines.springboot.batch.report.ReportJobExecutionListener;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

@ExtendWith(MockitoExtension.class)
class ReportHelperTest {

  @Mock
  private Appender<ILoggingEvent> mockAppender;

  @Mock
  private org.slf4j.Logger mockLogger;

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

  @Test
  void givenMessageAndRuntimeExceptionWithoutItem_whenReportThrowable_thenLoggingMessageAsError() {
    // Given
    String message = "test";
    Throwable exception = new RuntimeException(message);

    doReturn(false).when(mockLogger).isErrorEnabled();

    // When
    classUnderTest.reportThrowable(message, null, exception, mockLogger);

    // Then
    verify(mockAppender)
        .doAppend(
            argThat(
                argument -> {
                  String msg = argument.getFormattedMessage();
                  assertThat(msg).startsWith(message);
                  assertThat(msg).endsWith(message);
                  assertThat(argument.getLevel()).isEqualTo(Level.ERROR);
                  return true;
                }));
    verify(mockLogger, times(1)).isErrorEnabled();
  }

  @Test
  void
  givenMessageAndRuntimeExceptionAndItem_whenReportThrowable_thenLoggingMessageAsErrorAndException() {
    // Given
    String message = "test";
    Throwable exception = new RuntimeException(message);
    Object item = asList("test-a", "test-b");

    doReturn(true).when(mockLogger).isErrorEnabled();

    // When
    classUnderTest.reportThrowable(message, item, exception, mockLogger);

    // Then
    verify(mockAppender)
        .doAppend(
            argThat(
                argument -> {
                  String msg = argument.getFormattedMessage();
                  assertThat(msg).startsWith(message);
                  assertThat(msg).endsWith(message);
                  assertThat(argument.getLevel()).isEqualTo(Level.ERROR);
                  return true;
                }));
    verify(mockLogger, times(1)).isErrorEnabled();
  }

  @Test
  void
  givenMessageAndDataAccessExceptionAndItemAndSQLException_whenReportThrowable_thenLoggingMessageAsErrorAndException() {
    // Given
    String message = "test";
    Throwable exception = new DataAccessException(message) {
    };
    Object item = asList("test-a", "test-b");

    doReturn(true).when(mockLogger).isErrorEnabled();

    // When
    classUnderTest.reportThrowable(message, item, exception, mockLogger);

    // Then
    verify(mockAppender)
        .doAppend(
            argThat(
                argument -> {
                  String msg = argument.getFormattedMessage();
                  assertThat(msg).startsWith(message);
                  assertThat(msg).endsWith(message);
                  assertThat(argument.getLevel()).isEqualTo(Level.ERROR);
                  return true;
                }));
    verify(mockLogger, times(1)).isErrorEnabled();
    verify(mockLogger, times(1)).error(anyString(), any(Throwable.class));
  }

  @Test
  void
  givenMessageAndDataAccessExceptionAndSQLExceptionAndItem_whenReportThrowable_thenLoggingMessageAsErrorAndException() {
    // Given
    String message = "test";
    Throwable exception =
        new DataAccessException(message, new SQLException("reason", "SQLState", 4711)) {
        };
    Object item = asList("test-a", "test-b");

    // When
    classUnderTest.reportThrowable(message, item, exception, mockLogger);

    // Then
    verify(mockAppender)
        .doAppend(
            argThat(
                argument -> {
                  String msg = argument.getFormattedMessage();
                  assertThat(msg).startsWith(message);
                  assertThat(argument.getLevel()).isEqualTo(Level.ERROR);
                  return true;
                }));
    verify(mockLogger, times(0)).isErrorEnabled();
    verify(mockLogger, times(1)).error(anyString(), any(Throwable.class));
  }
}
