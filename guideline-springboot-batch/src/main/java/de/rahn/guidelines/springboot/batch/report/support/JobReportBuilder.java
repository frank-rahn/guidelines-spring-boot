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

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;
import static org.apache.commons.lang3.StringUtils.leftPad;
import static org.apache.commons.lang3.StringUtils.rightPad;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;

public class JobReportBuilder {

  @SuppressWarnings("WeakerAccess")
  public static final String NULL = "<null>";

  private StringBuilder report = new StringBuilder();

  public static JobReportBuilder of() {
    return new JobReportBuilder();
  }

  private static ZonedDateTime computeZonedDateTime(Date dateTime) {
    return dateTime.toInstant().atZone(ZoneId.systemDefault());
  }

  private static ZonedDateTime computeZonedDateTime(Date dateTime, Date defaultDateTime) {
    if (dateTime != null) {
      return computeZonedDateTime(dateTime);
    } else {
      return computeZonedDateTime(defaultDateTime);
    }
  }

  public JobReportBuilder writeHeader(JobExecution jobExecution) {
    ZonedDateTime zonedStartDateTime =
        computeZonedDateTime(jobExecution.getStartTime(), jobExecution.getCreateTime());
    ZonedDateTime zonedEndDateTime =
        computeZonedDateTime(jobExecution.getEndTime(), jobExecution.getCreateTime());

    return writeNewLine()
        .writeFilledLine('*')
        .writeFilled('*', 5)
        .writeText(' ')
        .writeText(jobExecution.getJobInstance().getJobName())
        .writeNewLine()
        .writeFilledLine('*')
        .writeText("Start am ")
        .writeText(zonedStartDateTime.toLocalDate())
        .writeText(" um ")
        .writeText(zonedStartDateTime.toLocalTime())
        .writeNewLine()
        .writeText("Ende am ")
        .writeText(zonedEndDateTime.toLocalDate())
        .writeText(" um ")
        .writeText(zonedEndDateTime.toLocalTime())
        .writeNewLine()
        .writeText("ID des Batchlaufes ist ")
        .writeText(jobExecution.getId())
        .writeNewLine()
        .writeText("ID der Instanz des Batchlaufes ist ")
        .writeText(jobExecution.getJobInstance().getInstanceId())
        .writeText(" (Für Wiederanlauf, ...)")
        .writeNewLine()
        .writeText("Parameters  : ")
        .writeText(jobExecution.getJobParameters())
        .writeNewLine()
        .writeText("Exit Code   : ")
        .writeText(jobExecution.getExitStatus().getExitCode())
        .writeNewLine()
        .writeText("Exit Meldung: ")
        .writeText(jobExecution.getExitStatus().getExitDescription())
        .writeNewLine();
  }

  public JobReportBuilder writeFooter(@SuppressWarnings("unused") JobExecution jobExecution) {
    return writeFilledLine('*');
  }

  public String build() {
    try {
      return report.toString();
    } finally {
      report.setLength(0);
    }
  }

  @SuppressWarnings("UnusedReturnValue")
  public JobReportBuilder writeStepExecution(StepExecution stepExecution) {
    writeFilledLine('*');

    if (stepExecution != null) {
      ZonedDateTime zonedStartDateTime = computeZonedDateTime(stepExecution.getStartTime());
      ZonedDateTime zonedEndDateTime =
          computeZonedDateTime(stepExecution.getEndTime(), stepExecution.getStartTime());

      return writeText(stepExecution.getStepName())
          .writeText(':')
          .writeNewLine()
          .writeTitle("Start")
          .writeText(zonedStartDateTime.toLocalTime())
          .writeNewLine()
          .writeTitle("Ende")
          .writeText(zonedEndDateTime.toLocalTime())
          .writeNewLine()
          .writeTitle("Exit Code")
          .writeText(stepExecution.getExitStatus().getExitCode())
          .writeNewLine()
          .writeTitle("Exit Meldung")
          .writeText(stepExecution.getExitStatus().getExitDescription())
          .writeNewLine()
          .writeText('\t')
          .writeFilled('*', 5)
          .writeNewLine()
          .writeCount("Read", stepExecution.getReadCount())
          .writeCount("Incorrect", stepExecution.getSkipCount())
          .writeCount("Filtered", stepExecution.getFilterCount())
          .writeCount("Processed", stepExecution.getWriteCount())
          .writeCount("Rollbacks", stepExecution.getRollbackCount())
          .writeCount("Commits", stepExecution.getCommitCount());
    }

    return writeNull().writeNewLine();
  }

  @SuppressWarnings("WeakerAccess")
  public JobReportBuilder writeCount(String title, Integer counter) {
    writeTitle(title);

    if (counter != null) {
      writeText(counter);
    } else {
      writeNull();
    }

    return writeNewLine();
  }

  @SuppressWarnings("WeakerAccess")
  public JobReportBuilder writeTitle(String title) {
    if (title == null) {
      title = NULL;
    }
    return writeText('\t').writeText(rightPad(title, 12)).writeText(": ");
  }

  @SuppressWarnings("WeakerAccess")
  public JobReportBuilder writeText(JobParameters jobParameters) {
    if (jobParameters != null) {
      jobParameters
          .getParameters()
          .forEach(
              (key, value) ->
                  writeNewLine()
                      .writeText('\t')
                      .writeText(key)
                      .writeText(" -> ")
                      .writeText(value.getValue()));
      return this;
    }

    return writeNull();
  }

  @SuppressWarnings("WeakerAccess")
  public JobReportBuilder writeText(LocalTime localTime) {
    if (localTime != null) {
      return writeText(localTime.format(ISO_LOCAL_TIME));
    }

    return writeNull();
  }

  @SuppressWarnings("WeakerAccess")
  public JobReportBuilder writeText(LocalDate localDate) {
    if (localDate != null) {
      return writeText(localDate.format(ISO_LOCAL_DATE));
    }

    return writeNull();
  }

  @SuppressWarnings("WeakerAccess")
  public JobReportBuilder writeText(String text) {
    if (text != null) {
      report.append(text);
      return this;
    }

    return writeNull();
  }

  @SuppressWarnings("WeakerAccess")
  public JobReportBuilder writeText(Long value) {
    if (value != null) {
      return writeText(value.toString());
    }

    return writeNull();
  }

  @SuppressWarnings("WeakerAccess")
  public JobReportBuilder writeText(Integer value) {
    if (value != null) {
      return writeText(value.toString());
    }

    return writeNull();
  }

  @SuppressWarnings("WeakerAccess")
  public JobReportBuilder writeText(char character) {
    return writeText(Character.toString(character));
  }

  @SuppressWarnings("WeakerAccess")
  public JobReportBuilder writeText(Object value) {
    if (value != null) {
      return writeText(value.toString());
    }

    return writeNull();
  }

  @SuppressWarnings("WeakerAccess")
  public JobReportBuilder writeNull() {
    return writeText(NULL);
  }

  @SuppressWarnings("WeakerAccess")
  public JobReportBuilder writeFilledLine(char filler) {
    return writeFilled(filler, 40).writeNewLine();
  }

  @SuppressWarnings("WeakerAccess")
  public JobReportBuilder writeFilled(char filler, int count) {
    return writeText(leftPad("", count, filler));
  }

  @SuppressWarnings("WeakerAccess")
  public JobReportBuilder writeNewLine() {
    return writeText('\n');
  }
}
