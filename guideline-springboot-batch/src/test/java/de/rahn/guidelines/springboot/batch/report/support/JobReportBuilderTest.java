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
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;

class JobReportBuilderTest {

  private JobReportBuilder classUnderTest = new JobReportBuilder();

  @Test
  void givenNothing_whenOf_thenReturnJobReportBuilder() {
    // When
    JobReportBuilder result = JobReportBuilder.of();

    // Then
    assertThat(result).isNotNull();
    assertThat(result.build()).hasSize(0);
  }

  @Test
  void givenNothing_whenWriteNewLine_thenReportOnlyNewLine() {
    // When
    JobReportBuilder result = classUnderTest.writeNewLine();

    // Then
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(classUnderTest);
    assertThat(result.build()).isEqualTo("\n");
  }

  @Test
  void givenNothing_whenWriteNull_thenReportNull() {
    // When
    JobReportBuilder result = classUnderTest.writeNull();

    // Then
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(classUnderTest);
    assertThat(result.build()).isEqualTo(JobReportBuilder.NULL);
  }

  @Test
  void givenFillerAndCount0_whenWriteFilled_thenReportEmptyLine() {
    // Given
    char filler = '#';
    int count = 0;

    // When
    JobReportBuilder result = classUnderTest.writeFilled(filler, count);

    // Then
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(classUnderTest);
    assertThat(result.build()).isEqualTo("");
  }

  @Test
  void givenFillerAndCount5_whenWriteFilled_thenReportOnlyFilledLine() {
    // Given
    char filler = '#';
    int count = 5;

    // When
    JobReportBuilder result = classUnderTest.writeFilled(filler, count);

    // Then
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(classUnderTest);
    assertThat(result.build()).isEqualTo("#####");
  }

  @Test
  void givenFiller_whenWriteFilledLine_thenReportOnlyFilledLine() {
    // Given
    char filler = '#';

    // When
    JobReportBuilder result = classUnderTest.writeFilledLine(filler);

    // Then
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(classUnderTest);
    assertThat(result.build()).isEqualTo(Character.toString(filler).repeat(40) + '\n');
  }

  @Test
  void givenCharacter_whenWriteText_thenReportCharacter() {
    // Given
    char character = '#';

    // When
    JobReportBuilder result = classUnderTest.writeText(character);

    // Then
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(classUnderTest);
    assertThat(result.build()).isEqualTo(Character.toString(character));
  }

  @Test
  void givenObject_whenWriteText_thenReportToString() {
    // Given
    Object obj = new Object();

    // When
    JobReportBuilder result = classUnderTest.writeText(obj);

    // Then
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(classUnderTest);
    assertThat(result.build()).isEqualTo(obj.toString());
  }

  @Test
  void givenNoneObject_whenWriteText_thenReportNull() {
    // Given
    Object obj = null;

    // When
    @SuppressWarnings("ConstantConditions")
    JobReportBuilder result = classUnderTest.writeText(obj);

    // Then
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(classUnderTest);
    assertThat(result.build()).isEqualTo(JobReportBuilder.NULL);
  }

  @Test
  void givenNoneLong_whenWriteText_thenReportNull() {
    // Given
    Long value = null;

    // When
    JobReportBuilder result = classUnderTest.writeText(value);

    // Then
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(classUnderTest);
    assertThat(result.build()).isEqualTo(JobReportBuilder.NULL);
  }

  @Test
  void givenLong_whenWriteText_thenReportNumber() {
    // Given
    Long value = 100L;

    // When
    JobReportBuilder result = classUnderTest.writeText(value);

    // Then
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(classUnderTest);
    assertThat(result.build()).isEqualTo(value.toString());
  }

  @Test
  void givenNoneInteger_whenWriteText_thenReportNull() {
    // Given
    Integer value = null;

    // When
    JobReportBuilder result = classUnderTest.writeText(value);

    // Then
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(classUnderTest);
    assertThat(result.build()).isEqualTo(JobReportBuilder.NULL);
  }

  @Test
  void givenInteger_whenWriteText_thenReportNumber() {
    // Given
    Integer value = 100;

    // When
    JobReportBuilder result = classUnderTest.writeText(value);

    // Then
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(classUnderTest);
    assertThat(result.build()).isEqualTo(value.toString());
  }

  @Test
  void givenText_whenWriteText_thenReportText() {
    // Given
    String text = "100L";

    // When
    JobReportBuilder result = classUnderTest.writeText(text);

    // Then
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(classUnderTest);
    assertThat(result.build()).isEqualTo(text);
  }

  @Test
  void givenNoneText_whenWriteText_thenReportNull() {
    // Given
    String test = null;

    // When
    @SuppressWarnings("ConstantConditions")
    JobReportBuilder result = classUnderTest.writeText(test);

    // Then
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(classUnderTest);
    assertThat(result.build()).isEqualTo(JobReportBuilder.NULL);
  }

  @Test
  void givenLocalDate_whenWriteText_thenReportDate() {
    // Given
    LocalDate date = LocalDate.now();

    // When
    JobReportBuilder result = classUnderTest.writeText(date);

    // Then
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(classUnderTest);
    assertThat(result.build()).isEqualTo(date.format(ISO_LOCAL_DATE));
  }

  @Test
  void givenNoneLocalDate_whenWriteText_thenReportNull() {
    // Given
    LocalDate date = null;

    // When
    @SuppressWarnings("ConstantConditions")
    JobReportBuilder result = classUnderTest.writeText(date);

    // Then
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(classUnderTest);
    assertThat(result.build()).isEqualTo(JobReportBuilder.NULL);
  }

  @Test
  void givenLocalTime_whenWriteText_thenReportTime() {
    // Given
    LocalTime time = LocalTime.now();

    // When
    JobReportBuilder result = classUnderTest.writeText(time);

    // Then
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(classUnderTest);
    assertThat(result.build()).isEqualTo(time.format(ISO_LOCAL_TIME));
  }

  @Test
  void givenNoneLocalTime_whenWriteText_thenReportNull() {
    // Given
    LocalTime time = null;

    // When
    @SuppressWarnings("ConstantConditions")
    JobReportBuilder result = classUnderTest.writeText(time);

    // Then
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(classUnderTest);
    assertThat(result.build()).isEqualTo(JobReportBuilder.NULL);
  }

  @Test
  void givenNoneJobParameters_whenWriteText_thenReportNull() {
    // Given
    JobParameters jobParameters = null;

    // When
    @SuppressWarnings("ConstantConditions")
    JobReportBuilder result = classUnderTest.writeText(jobParameters);

    // Then
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(classUnderTest);
    assertThat(result.build()).isEqualTo(JobReportBuilder.NULL);
  }

  @Test
  void givenJobParametersWithoutParameters_whenWriteText_thenReportNothing() {
    // Given
    JobParameters jobParameters = new JobParameters();

    // When
    JobReportBuilder result = classUnderTest.writeText(jobParameters);

    // Then
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(classUnderTest);
    assertThat(result.build()).hasSize(0);
  }

  @Test
  void givenJobParameters_whenWriteText_thenReportParameters() {
    // Given
    Map<String, JobParameter> parameters = new HashMap<>();
    parameters.put("name", new JobParameter("value"));
    JobParameters jobParameters = new JobParameters(parameters);

    // When
    JobReportBuilder result = classUnderTest.writeText(jobParameters);

    // Then
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(classUnderTest);
    assertThat(result.build()).isEqualTo("\n\tname -> value");
  }

  @Test
  void givenJobExecution_whenWriteHeader_thenReportHeader() {
    // Given
    JobInstance jobInstance = new JobInstance(4711L, "Job-Name");

    Map<String, JobParameter> parameters = new HashMap<>();
    parameters.put("name", new JobParameter("value"));
    JobParameters jobParameters = new JobParameters(parameters);

    JobExecution jobExecution =
        new JobExecution(jobInstance, 1L, jobParameters, "Job-Configuration-Name");
    jobExecution.setStartTime(new Date());
    jobExecution.setEndTime(new Date());
    jobExecution.setExitStatus(ExitStatus.COMPLETED.addExitDescription("Completed"));

    // When
    JobReportBuilder result = classUnderTest.writeHeader(jobExecution);

    // Then
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(classUnderTest);
    assertThat(result.build()).contains("Job-Name", "Completed", "name -> value", "4711");
  }

  @Test
  void givenJobExecution_whenWriteFooter_thenReportFooter() {
    // Given
    JobInstance jobInstance = new JobInstance(4711L, "Job-Name");

    Map<String, JobParameter> parameters = new HashMap<>();
    parameters.put("name", new JobParameter("value"));
    JobParameters jobParameters = new JobParameters(parameters);

    JobExecution jobExecution =
        new JobExecution(jobInstance, 1L, jobParameters, "Job-Configuration-Name");
    jobExecution.setStartTime(new Date());
    jobExecution.setEndTime(new Date());
    jobExecution.setExitStatus(ExitStatus.COMPLETED.addExitDescription("Completed"));

    // When
    JobReportBuilder result = classUnderTest.writeFooter(jobExecution);

    // Then
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(classUnderTest);
    assertThat(result.build()).containsSequence("*");
  }

  @Test
  void givenNoneTitle_whenWriteTitle_thenReportNull() {
    // Given
    String title = null;

    // When
    @SuppressWarnings("ConstantConditions")
    JobReportBuilder result = classUnderTest.writeTitle(title);

    // Then
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(classUnderTest);
    assertThat(result.build()).contains("\t", JobReportBuilder.NULL);
  }

  @Test
  void givenTitle_whenWriteTitle_thenReportTitle() {
    // Given
    String title = "Title";

    // When
    JobReportBuilder result = classUnderTest.writeTitle(title);

    // Then
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(classUnderTest);
    assertThat(result.build()).contains("\t", "Title");
  }

  @Test
  void givenNoneTitleAndNoneCounter_whenWriteCount_thenReportNulls() {
    // Given
    String title = null;
    Integer counter = null;

    // When
    @SuppressWarnings("ConstantConditions")
    JobReportBuilder result = classUnderTest.writeCount(title, counter);

    // Then
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(classUnderTest);
    assertThat(result.build()).contains("\t", JobReportBuilder.NULL, JobReportBuilder.NULL, "\n");
  }

  @Test
  void givenTitleAndNoneCounter_whenWriteCount_thenReportTitleAndNull() {
    // Given
    String title = "title";
    Integer counter = null;

    // When
    JobReportBuilder result = classUnderTest.writeCount(title, counter);

    // Then
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(classUnderTest);
    assertThat(result.build()).contains("\t", title, JobReportBuilder.NULL, "\n");
  }

  @Test
  void givenTitleAndCounter_whenWriteCount_thenReportTitleAndCounter() {
    // Given
    String title = "title";
    Integer counter = 100;

    // When
    JobReportBuilder result = classUnderTest.writeCount(title, counter);

    // Then
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(classUnderTest);
    assertThat(result.build()).contains("\t", title, "100", "\n").hasSize(19);
  }

  @Test
  void givenNoneStepExecution_whenWriteStepExecution_thenReportNull() {
    // Given
    StepExecution stepExecution = null;

    // When
    @SuppressWarnings("ConstantConditions")
    JobReportBuilder result = classUnderTest.writeStepExecution(stepExecution);

    // Then
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(classUnderTest);
    assertThat(result.build()).isEqualTo(JobReportBuilder.NULL + '\n');
  }

  @Test
  void givenStepExecution_whenWriteStepExecution_thenReportStep() {
    // Given
    JobInstance jobInstance = new JobInstance(4711L, "Job-Name");

    Map<String, JobParameter> parameters = new HashMap<>();
    parameters.put("name", new JobParameter("value"));
    JobParameters jobParameters = new JobParameters(parameters);

    JobExecution jobExecution =
        new JobExecution(jobInstance, 1L, jobParameters, "Job-Configuration-Name");
    jobExecution.setStartTime(new Date());
    jobExecution.setEndTime(new Date());
    jobExecution.setExitStatus(ExitStatus.COMPLETED.addExitDescription("Completed"));

    StepExecution stepExecution = new StepExecution("Step-Name", jobExecution);

    // When
    JobReportBuilder result = classUnderTest.writeStepExecution(stepExecution);

    // Then
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(classUnderTest);
    assertThat(result.build())
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
