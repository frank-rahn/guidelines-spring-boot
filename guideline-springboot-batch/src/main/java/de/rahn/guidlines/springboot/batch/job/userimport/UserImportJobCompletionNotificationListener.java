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
package de.rahn.guidlines.springboot.batch.job.userimport;

import static org.springframework.batch.core.BatchStatus.COMPLETED;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserImportJobCompletionNotificationListener extends JobExecutionListenerSupport {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(UserImportJobCompletionNotificationListener.class);

  private final JdbcTemplate jdbcTemplate;

  public UserImportJobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    LOGGER.info(jobExecution.toString());

    if (jobExecution.getStatus() == COMPLETED) {
      LOGGER.info("Time to verify the results:");

      jdbcTemplate
          .query(
              "SELECT first_name, last_name FROM people",
              (rs, row) -> new Person(rs.getString(1), rs.getString(2)))
          .forEach(person -> LOGGER.info("Found <" + person + "> in the database."));
    }
  }
}