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

import de.rahn.guidelines.springboot.batch.report.ReportJobExecutionListener;
import java.sql.SQLException;
import java.util.Collection;
import java.util.stream.Collectors;
import org.apache.commons.lang3.builder.RecursiveToStringStyle;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

/**
 * @author Frank Rahn
 */
@Component
public class ReportHelper {

  private static final Logger LOGGER_REPORT =
      LoggerFactory.getLogger(ReportJobExecutionListener.REPORT);

  public void reportInformation(String message) {
    LOGGER_REPORT.info(message);
  }

  public void reportWarning(String message) {
    LOGGER_REPORT.warn(message);
  }

  public void reportError(String message) {
    LOGGER_REPORT.error(message);
  }

  public void reportThrowable(String message, Object item, Throwable throwable, Logger logger) {
    LOGGER_REPORT.error(
        message + toStringRepresentation(item, false) + ": " + throwable.getMessage());

    logThrowable(message, item, throwable, logger);
  }

  private void logThrowable(String message, Object item, Throwable throwable, Logger logger) {
    if (throwable instanceof DataAccessException && throwable.getCause() instanceof SQLException) {
      logThrowable(message, item, throwable.getCause(), logger);
    } else if (throwable instanceof SQLException) {
      final String tmp = message + toStringRepresentation(item, true);
      ((SQLException) throwable).forEach(t2 -> logger.error(tmp, t2));
    } else {
      logger.error(message + toStringRepresentation(item, true), throwable);
    }
  }

  private String toStringRepresentation(Object item, boolean full) {
    if (item == null) {
      return "";
    }

    if (item instanceof Collection) {
      @SuppressWarnings("unchecked")
      Collection<Object> items = (Collection<Object>) item;
      return toStringRepresentation(items, full);
    }

    if (full) {
      return ToStringBuilder.reflectionToString(item, new RecursiveToStringStyle());
    }

    return item.toString();
  }

  private String toStringRepresentation(Collection<Object> items, boolean full) {
    return items.stream()
        .map(item -> toStringRepresentation(item, full))
        .collect(Collectors.joining(", ", "[", "]"));
  }
}
