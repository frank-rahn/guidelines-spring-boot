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
import static de.rahn.guidelines.springboot.batch.customized.CustomizedExitStatus.COMPLETED_WITH_SKIPS;
import static org.springframework.batch.core.ExitStatus.COMPLETED;
import static org.springframework.batch.core.ExitStatus.EXECUTING;
import static org.springframework.batch.core.ExitStatus.FAILED;
import static org.springframework.batch.core.ExitStatus.NOOP;
import static org.springframework.batch.core.ExitStatus.STOPPED;
import static org.springframework.batch.core.ExitStatus.UNKNOWN;

import java.util.ArrayList;
import java.util.Collection;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.autoconfigure.batch.JobExecutionEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author Frank Rahn
 */
@Component
public class CustomizedJobExecutionExitCodeGenerator
    implements ApplicationListener<JobExecutionEvent>, ExitCodeGenerator {

  private final Collection<JobExecution> jobExecutions = new ArrayList<>();

  @Override
  public void onApplicationEvent(JobExecutionEvent event) {
    jobExecutions.add(event.getJobExecution());
  }

  @Override
  public int getExitCode() {
    return jobExecutions.stream()
        .map(JobExecution::getExitStatus)
        .map(this::mapExitStatusToExitCode)
        .max(Integer::compareTo)
        .orElse(0);
  }

  protected int mapExitStatusToExitCode(ExitStatus exitStatus) {
    if (COMPLETED.getExitCode().equals(exitStatus.getExitCode())) {
      return 0;
    }
    if (COMPLETED_WITH_ERRORS.getExitCode().equals(exitStatus.getExitCode())) {
      return 1;
    }
    if (COMPLETED_WITH_SKIPS.getExitCode().equals(exitStatus.getExitCode())) {
      return 2;
    }
    if (NOOP.getExitCode().equals(exitStatus.getExitCode())) {
      return 3;
    }
    if (FAILED.getExitCode().equals(exitStatus.getExitCode())) {
      return 4;
    }
    if (EXECUTING.getExitCode().equals(exitStatus.getExitCode())) {
      return 5;
    }
    if (STOPPED.getExitCode().equals(exitStatus.getExitCode())) {
      return 6;
    }
    if (UNKNOWN.getExitCode().equals(exitStatus.getExitCode())) {
      return 7;
    }
    return 8;
  }
}
