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
package de.rahn.guidelines.springboot.batch.job.userimport;

import static de.rahn.guidelines.springboot.batch.customized.CustomizedExitStatus.COMPLETED_WITH_ERRORS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.batch.core.ExitStatus.COMPLETED;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {"spring.batch.job.enabled=false"})
@SpringBatchTest
class UserImportJobTests {

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  private JobLauncherTestUtils jobLauncherTestUtils;

  @Test
  void givenJobWithoutParameters_whenJobExecuted_thenSuccess() throws Exception {
    // Given

    // When
    JobExecution jobExecution = jobLauncherTestUtils.launchJob();

    // Then
    assertThat(jobExecution).isNotNull();

    assertThat(jobExecution.getJobInstance()).isNotNull();
    assertThat(jobExecution.getJobInstance().getJobName()).isEqualTo("userImportJob");

    assertThat(jobExecution.getExitStatus()).isNotNull();
    assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo(COMPLETED.getExitCode());
  }

  @Test
  void givenJobWithParameters_whenJobExecuted_thenSuccess() throws Exception {
    // Given
    JobParameters jobParameters =
        new JobParametersBuilder()
            .addString("skip", "Schmitz")
            .addString("filter", "Meier")
            .toJobParameters();

    // When
    JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

    // Then
    assertThat(jobExecution).isNotNull();

    assertThat(jobExecution.getJobInstance()).isNotNull();
    assertThat(jobExecution.getJobInstance().getJobName()).isEqualTo("userImportJob");

    assertThat(jobExecution.getExitStatus()).isNotNull();
    assertThat(jobExecution.getExitStatus().getExitCode())
        .isEqualTo(COMPLETED_WITH_ERRORS.getExitCode());
  }
}
