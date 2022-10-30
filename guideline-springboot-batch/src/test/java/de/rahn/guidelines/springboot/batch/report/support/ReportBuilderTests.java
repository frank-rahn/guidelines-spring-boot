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
package de.rahn.guidelines.springboot.batch.report.support;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.STRING;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.Map;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ReportBuilderTests {

  private final ReportBuilder classUnderTest = new ReportBuilder();

  @Test
  void given_nothing_when_of_then_returns_JobReportBuilder() {
    // Given
    // When
    var result = ReportBuilder.of();

    // Then
    assertThat(result).extracting(ReportBuilder::build, STRING).hasSize(0);
  }

  @Test
  void given_nothing_when_writeNewLine_then_reports_only_a_new_line() {
    // Given
    // When
    var result = classUnderTest.writeNewLine();

    // Then
    assertThat(result).isEqualTo(classUnderTest).extracting(ReportBuilder::build).isEqualTo("\n");
  }

  @Test
  void given_nothing_when_write_null_then_reports_null() {
    // When
    var result = classUnderTest.writeNull();

    // Then
    assertThat(result)
        .isEqualTo(classUnderTest)
        .extracting(ReportBuilder::build)
        .isEqualTo(ReportBuilder.NULL);
  }

  @Test
  void given_filler_and_count_0_when_writeFilled_then_reports_empty_line() {
    // Given
    var filler = '#';
    var count = 0;

    // When
    var result = classUnderTest.writeFilled(filler, count);

    // Then
    assertThat(result).isEqualTo(classUnderTest).extracting(ReportBuilder::build).isEqualTo("");
  }

  @Test
  void given_filler_and_count_5_when_writeFilled_then_reports_only_a_filled_line() {
    // Given
    var filler = '#';
    var count = 5;

    // When
    var result = classUnderTest.writeFilled(filler, count);

    // Then
    assertThat(result)
        .isEqualTo(classUnderTest)
        .extracting(ReportBuilder::build)
        .isEqualTo("#####");
  }

  @Test
  void given_filler_when_writeFilledLine_then_reports_only_a_filled_lLine() {
    // Given
    var filler = '#';

    // When
    var result = classUnderTest.writeFilledLine(filler);

    // Then
    assertThat(result)
        .isEqualTo(classUnderTest)
        .extracting(ReportBuilder::build)
        .isEqualTo(Character.toString(filler).repeat(ReportBuilder.LINE_SIZE) + '\n');
  }

  @Test
  void given_character_when_writeText_then_reports_character() {
    // Given
    var character = '#';

    // When
    var result = classUnderTest.writeText(character);

    // Then
    assertThat(result)
        .isEqualTo(classUnderTest)
        .extracting(ReportBuilder::build)
        .isEqualTo(Character.toString(character));
  }

  @Test
  void given_Object_when_writeText_then_reports_toString() {
    // Given
    var obj = new Object();

    // When
    var result = classUnderTest.writeText(obj);

    // Then
    assertThat(result)
        .isEqualTo(classUnderTest)
        .extracting(ReportBuilder::build)
        .isEqualTo(obj.toString());
  }

  @Test
  @SuppressWarnings("ConstantConditions")
  void given_none_Object_when_writeText_then_reports_null() {
    // Given
    Object obj = null;

    // When
    var result = classUnderTest.writeText(obj);

    // Then
    assertThat(result)
        .isEqualTo(classUnderTest)
        .extracting(ReportBuilder::build)
        .isEqualTo(ReportBuilder.NULL);
  }

  @Test
  void given_none_Long_when_writeText_then_reports_null() {
    // Given
    Long value = null;

    // When
    var result = classUnderTest.writeText(value);

    // Then
    assertThat(result)
        .isEqualTo(classUnderTest)
        .extracting(ReportBuilder::build)
        .isEqualTo(ReportBuilder.NULL);
  }

  @Test
  void given_Long_when_writeText_then_reports_number() {
    // Given
    Long value = 100L;

    // When
    var result = classUnderTest.writeText(value);

    // Then
    assertThat(result)
        .isEqualTo(classUnderTest)
        .extracting(ReportBuilder::build)
        .isEqualTo(value.toString());
  }

  @Test
  void given_none_Integer_when_writeText_then_report_null() {
    // Given
    Integer value = null;

    // When
    var result = classUnderTest.writeText(value);

    // Then
    assertThat(result)
        .isEqualTo(classUnderTest)
        .extracting(ReportBuilder::build)
        .isEqualTo(ReportBuilder.NULL);
  }

  @Test
  void givenInteger_when_writeText_then_reports_number() {
    // Given
    Integer value = 100;

    // When
    var result = classUnderTest.writeText(value);

    // Then
    assertThat(result)
        .isEqualTo(classUnderTest)
        .extracting(ReportBuilder::build)
        .isEqualTo(value.toString());
  }

  @Test
  void given_Text_when_writeText_then_reports_text() {
    // Given
    var text = "100L";

    // When
    var result = classUnderTest.writeText(text);

    // Then
    assertThat(result).isEqualTo(classUnderTest).extracting(ReportBuilder::build).isEqualTo(text);
  }

  @Test
  @SuppressWarnings("ConstantConditions")
  void given_none_Text_when_writeText_then_reports_null() {
    // Given
    String test = null;

    // When
    var result = classUnderTest.writeText(test);

    // Then
    assertThat(result)
        .isEqualTo(classUnderTest)
        .extracting(ReportBuilder::build)
        .isEqualTo(ReportBuilder.NULL);
  }

  @Test
  void given_LocalDate_when_writeText_then_reports_Date() {
    // Given
    var date = LocalDate.now();

    // When
    var result = classUnderTest.writeText(date);

    // Then
    assertThat(result)
        .isEqualTo(classUnderTest)
        .extracting(ReportBuilder::build)
        .isEqualTo(date.format(ISO_LOCAL_DATE));
  }

  @Test
  @SuppressWarnings("ConstantConditions")
  void given_none_LocalDate_when_writeText_then_reports_null() {
    // Given
    LocalDate date = null;

    // When
    var result = classUnderTest.writeText(date);

    // Then
    assertThat(result)
        .isEqualTo(classUnderTest)
        .extracting(ReportBuilder::build)
        .isEqualTo(ReportBuilder.NULL);
  }

  @Test
  void given_LocalTime_when_writeText_then_reports_Time() {
    // Given
    var time = LocalTime.now();

    // When
    var result = classUnderTest.writeText(time);

    // Then
    assertThat(result)
        .isEqualTo(classUnderTest)
        .extracting(ReportBuilder::build)
        .isEqualTo(time.format(ISO_LOCAL_TIME));
  }

  @Test
  @SuppressWarnings("ConstantConditions")
  void given_none_LocalTime_when_writeText_then_reports_null() {
    // Given
    LocalTime time = null;

    // When
    var result = classUnderTest.writeText(time);

    // Then
    assertThat(result)
        .isEqualTo(classUnderTest)
        .extracting(ReportBuilder::build)
        .isEqualTo(ReportBuilder.NULL);
  }

  @Test
  @SuppressWarnings("ConstantConditions")
  void given_none_JobParameters_when_writeText_then_reports_null() {
    // Given
    JobParameters jobParameters = null;

    // When
    var result = classUnderTest.writeText(jobParameters);

    // Then
    assertThat(result)
        .isEqualTo(classUnderTest)
        .extracting(ReportBuilder::build)
        .isEqualTo(ReportBuilder.NULL);
  }

  @Test
  void given_JobParameters_without_Parameters_when_writeText_then_reports_nothing() {
    // Given
    var jobParameters = new JobParameters();

    // When
    var result = classUnderTest.writeText(jobParameters);

    // Then
    assertThat(result)
        .isEqualTo(classUnderTest)
        .extracting(ReportBuilder::build, STRING)
        .hasSize(0);
  }

  @Test
  void given_JobParameters_when_writeText_then_reports_parameters() {
    // Given
    var parameters = Map.of("name", new JobParameter("value"));
    var jobParameters = new JobParameters(parameters);

    // When
    var result = classUnderTest.writeText(jobParameters);

    // Then
    assertThat(result)
        .isEqualTo(classUnderTest)
        .extracting(ReportBuilder::build, STRING)
        .contains("\n", "name", ": value");
  }

  @Test
  void given_JobExecution_when_writeHeader_then_reports_header() {
    // Given
    var jobInstance = new JobInstance(4711L, "Job-Name");
    var parameters = Map.of("name", new JobParameter("value"));
    var jobParameters = new JobParameters(parameters);
    var jobExecution = new JobExecution(jobInstance, 1L, jobParameters, "Job-Configuration-Name");
    jobExecution.setStartTime(new Date());
    jobExecution.setEndTime(new Date());
    jobExecution.setExitStatus(ExitStatus.COMPLETED.addExitDescription("Completed"));

    // When
    var result = classUnderTest.writeHeader(jobExecution);

    // Then
    assertThat(result)
        .isEqualTo(classUnderTest)
        .extracting(ReportBuilder::build, STRING)
        .contains("Job-Name", "Completed", "name", ": value", "4711");
  }

  @Test
  void given_JobExecution_when_write_footer_then_reports_footer() {
    // Given
    var jobInstance = new JobInstance(4711L, "Job-Name");
    var parameters = Map.of("name", new JobParameter("value"));
    var jobParameters = new JobParameters(parameters);
    var jobExecution = new JobExecution(jobInstance, 1L, jobParameters, "Job-Configuration-Name");
    jobExecution.setStartTime(new Date());
    jobExecution.setEndTime(new Date());
    jobExecution.setExitStatus(ExitStatus.COMPLETED.addExitDescription("Completed"));

    // When
    var result = classUnderTest.writeFooter(jobExecution);

    // Then
    assertThat(result)
        .isEqualTo(classUnderTest)
        .extracting(ReportBuilder::build, STRING)
        .containsSequence("*");
  }

  @Test
  @SuppressWarnings("ConstantConditions")
  void given_none_Title_when_writeTitle_then_reports_null() {
    // Given
    String title = null;

    // When
    var result = classUnderTest.writeTitle(title);

    // Then
    assertThat(result)
        .isEqualTo(classUnderTest)
        .extracting(ReportBuilder::build, STRING)
        .contains(ReportBuilder.NULL);
  }

  @Test
  void given_Title_when_writeTitle_then_reports_Title() {
    // Given
    var title = "Title";

    // When
    var result = classUnderTest.writeTitle(title);

    // Then
    assertThat(result)
        .isEqualTo(classUnderTest)
        .extracting(ReportBuilder::build, STRING)
        .contains(title);
  }

  @Test
  @SuppressWarnings("ConstantConditions")
  void given_none_Title_and_none_counter_when_writeCount_then_reports_nulls() {
    // Given
    String title = null;
    Integer counter = null;

    // When
    var result = classUnderTest.writeCount(title, counter);

    // Then
    assertThat(result)
        .isEqualTo(classUnderTest)
        .extracting(ReportBuilder::build, STRING)
        .contains(ReportBuilder.NULL, ReportBuilder.NULL, "\n");
  }

  @Test
  void given_Title_and_none_Counter_when_writeCount_then_reports_Title_and_null() {
    // Given
    var title = "title";
    Integer counter = null;

    // When
    var result = classUnderTest.writeCount(title, counter);

    // Then
    assertThat(result)
        .isEqualTo(classUnderTest)
        .extracting(ReportBuilder::build, STRING)
        .contains(title, ReportBuilder.NULL, "\n");
  }

  @Test
  void given_Title_and_Counter_when_writeCount_then_report_Title_and_Counter() {
    // Given
    var title = "title";
    var counter = 100;

    // When
    var result = classUnderTest.writeCount(title, counter);

    // Then
    assertThat(result)
        .isEqualTo(classUnderTest)
        .extracting(ReportBuilder::build, STRING)
        .contains(title, ": 100", "\n")
        .hasSize(46);
  }

  @Test
  @SuppressWarnings("ConstantConditions")
  void given_none_StepExecution_when_writeStepExecution_then_reports_null() {
    // Given
    StepExecution stepExecution = null;

    // When
    var result = classUnderTest.writeStepExecution(stepExecution);

    // Then
    assertThat(result)
        .isEqualTo(classUnderTest)
        .extracting(ReportBuilder::build, STRING)
        .contains(ReportBuilder.NULL, "\n");
  }

  @Test
  void given_StepExecution_when_writeStepExecution_then_reports_Step() {
    // Given
    var jobInstance = new JobInstance(4711L, "Job-Name");
    var parameters = Map.of("name", new JobParameter("value"));
    var jobParameters = new JobParameters(parameters);
    var jobExecution = new JobExecution(jobInstance, 1L, jobParameters, "Job-Configuration-Name");
    jobExecution.setStartTime(new Date());
    jobExecution.setEndTime(new Date());
    jobExecution.setExitStatus(ExitStatus.COMPLETED.addExitDescription("Completed"));
    var stepExecution = new StepExecution("Step-Name", jobExecution);

    // When
    var result = classUnderTest.writeStepExecution(stepExecution);

    // Then
    assertThat(result)
        .isEqualTo(classUnderTest)
        .extracting(ReportBuilder::build, STRING)
        .contains(
            "Step-Name",
            "Start",
            "Ende",
            "Exit Code",
            "Exit Meldung",
            "Read",
            "Incorrect",
            "Incorrect",
            "Processed",
            "Rollbacks",
            "Commits");
  }
}
