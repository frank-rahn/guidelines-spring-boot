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
package de.rahn.guidelines.springboot.batch.customized;

import static de.rahn.guidelines.springboot.batch.customized.CustomizedExitStatus.COMPLETED_WITH_ERRORS;
import static org.springframework.batch.core.ExitStatus.COMPLETED;
import static org.springframework.batch.core.ExitStatus.FAILED;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepListener;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.stereotype.Component;

/**
 * @author Frank Rahn
 */
@Component
class CustomizedExitStatusListener implements JobExecutionListener, StepListener {

  @Override
  public void beforeJob(JobExecution jobExecution) {
    // Empty
  }

  @AfterStep
  public ExitStatus afterStep(StepExecution stepExecution) {
    String exitCode = stepExecution.getExitStatus().getExitCode();

    if (!FAILED.getExitCode().equals(exitCode)
        && (stepExecution.getSkipCount() > 0 || stepExecution.getRollbackCount() > 0)) {
      return COMPLETED_WITH_ERRORS;
    }

    return null;
  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    if (COMPLETED.equals(jobExecution.getExitStatus())) {
      jobExecution
          .getStepExecutions()
          .forEach(
              stepExecution -> {
                if (COMPLETED_WITH_ERRORS.equals(stepExecution.getExitStatus())) {
                  jobExecution.setExitStatus(COMPLETED_WITH_ERRORS);
                }
              });
    }
  }
}
