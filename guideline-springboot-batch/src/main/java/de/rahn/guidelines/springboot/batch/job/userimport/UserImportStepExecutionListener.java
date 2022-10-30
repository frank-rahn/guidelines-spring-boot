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
package de.rahn.guidelines.springboot.batch.job.userimport;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepListener;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.stereotype.Component;

/**
 * @author Frank Rahn
 */
@Component
@Slf4j
class UserImportStepExecutionListener implements StepListener {

  @BeforeStep
  public void beforeStep(StepExecution stepExecution) {
    stepExecution.getExecutionContext().put("start", System.currentTimeMillis());

    LOGGER.info("Step name: {} started", stepExecution.getStepName());
  }

  @AfterStep
  public ExitStatus afterStep(StepExecution stepExecution) {
    long elapsed =
        System.currentTimeMillis() - stepExecution.getExecutionContext().getLong("start");

    LOGGER.info(
        "Step name: {} ended. Running time is {} milliseconds.",
        stepExecution.getStepName(),
        elapsed);
    LOGGER.info(stepExecution.toString());
    return null;
  }
}
