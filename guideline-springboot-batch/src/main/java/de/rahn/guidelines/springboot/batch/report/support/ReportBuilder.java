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

public class ReportBuilder {

  public static final String NULL = "<null>";

  public static final String TAB = "    ";

  public static final char FILLER = '*';

  public static final int LINE_SIZE = 80;

  private final StringBuilder report = new StringBuilder();

  private int reportCurrentLineLength = 0;

  public static ReportBuilder of() {
    return new ReportBuilder();
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

  public ReportBuilder writeHeader(JobExecution jobExecution) {
    ZonedDateTime zonedStartDateTime =
        computeZonedDateTime(jobExecution.getStartTime(), jobExecution.getCreateTime());
    ZonedDateTime zonedEndDateTime =
        computeZonedDateTime(jobExecution.getEndTime(), jobExecution.getCreateTime());

    return writeFilledLine(FILLER)
        .writeFilled(FILLER, 5)
        .writeText(" Batch ")
        .writeText(jobExecution.getJobInstance().getJobName())
        .writeTextAtEndOfLine(fill(FILLER, 5))
        .writeFilledLine(FILLER)
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
        .writeText("ID des Batchlaufes")
        .writeFillUpToMiddleOfLine(' ')
        .writeText(": ")
        .writeText(jobExecution.getId())
        .writeNewLine()
        .writeText("ID der Instanz des Batchlaufes")
        .writeFillUpToMiddleOfLine(' ')
        .writeText(": ")
        .writeText(jobExecution.getJobInstance().getInstanceId())
        .writeText(" (Für Wiederanlauf, ...)")
        .writeNewLine()
        .writeText("Parameters")
        .writeFillUpToMiddleOfLine(' ')
        .writeText(": ")
        .writeText(jobExecution.getJobParameters())
        .writeNewLine()
        .writeText("Exit Code")
        .writeFillUpToMiddleOfLine(' ')
        .writeText(": ")
        .writeText(jobExecution.getExitStatus().getExitCode())
        .writeNewLine()
        .writeText("Exit Meldung")
        .writeFillUpToMiddleOfLine(' ')
        .writeText(": ")
        .writeText(jobExecution.getExitStatus().getExitDescription())
        .writeNewLine();
  }

  public ReportBuilder writeFooter(@SuppressWarnings("unused") JobExecution jobExecution) {
    return writeFilledLine(FILLER);
  }

  public String build() {
    try {
      return report.toString();
    } finally {
      report.setLength(0);
      reportCurrentLineLength = 0;
    }
  }

  public ReportBuilder writeStepExecution(StepExecution stepExecution) {
    writeFilledLine(FILLER);

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

  public ReportBuilder writeCount(String title, Integer counter) {
    writeTitle(title);

    if (counter != null) {
      writeText(counter);
    } else {
      writeNull();
    }

    return writeNewLine();
  }

  public ReportBuilder writeTitle(String title) {
    if (title == null) {
      title = NULL;
    }
    return writeTab().writeText(rightPad(title, 12)).writeFillUpToMiddleOfLine(' ').writeText(": ");
  }

  public ReportBuilder writeText(JobParameters jobParameters) {
    if (jobParameters != null) {
      jobParameters
          .getParameters()
          .forEach(
              (key, value) ->
                  writeNewLine()
                      .writeTitle(key)
                      .writeText(value.getValue()));
      return this;
    }

    return writeNull();
  }

  public ReportBuilder writeText(LocalTime localTime) {
    if (localTime != null) {
      return writeText(localTime.format(ISO_LOCAL_TIME));
    }

    return writeNull();
  }

  public ReportBuilder writeText(LocalDate localDate) {
    if (localDate != null) {
      return writeText(localDate.format(ISO_LOCAL_DATE));
    }

    return writeNull();
  }

  public ReportBuilder writeText(String text) {
    if (text != null) {
      reportCurrentLineLength += text.length();
      report.append(text);
      return this;
    }

    return writeNull();
  }

  public ReportBuilder writeText(Long value) {
    if (value != null) {
      return writeText(value.toString());
    }

    return writeNull();
  }

  public ReportBuilder writeText(Integer value) {
    if (value != null) {
      return writeText(value.toString());
    }

    return writeNull();
  }

  public ReportBuilder writeText(char character) {
    return writeText(Character.toString(character));
  }

  public ReportBuilder writeText(Object value) {
    if (value != null) {
      return writeText(value.toString());
    }

    return writeNull();
  }

  public ReportBuilder writeNull() {
    return writeText(NULL);
  }

  public ReportBuilder writeFilledLine(char filler) {
    return writeFilled(filler, LINE_SIZE).writeNewLine();
  }

  public ReportBuilder writeFilled(char filler, int count) {
    return writeText(fill(filler, count));
  }

  public ReportBuilder writeNewLine() {
    try {
      return writeText('\n');
    } finally {
      reportCurrentLineLength = 0;
    }
  }

  public ReportBuilder writeTextAtEndOfLine(String text) {
    return writeText(leftPad(text, LINE_SIZE - reportCurrentLineLength)).writeNewLine();
  }

  public ReportBuilder writeFillUpToMiddleOfLine(char filler) {
    return writeText(fill(filler, LINE_SIZE / 2 - reportCurrentLineLength));
  }

  public ReportBuilder writeTab() {
    return writeText(TAB);
  }

  public String fill(char filler, int count) {
    return leftPad("", count, filler);
  }
}
