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
package de.rahn.guidelines.springboot.batch.report;

import de.rahn.guidelines.springboot.batch.report.support.ReportBuilder;
import de.rahn.guidelines.springboot.batch.report.support.ReportHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author Frank Rahn
 */
@Component(ReportJobExecutionListener.REPORT)
@Order
@Slf4j
@RequiredArgsConstructor
public class ReportJobExecutionListener extends JobExecutionListenerSupport {

  private final ReportHelper reportHelper;

  /**
   * Die Konstante für den Logger.
   */
  public static final String REPORT = "REPORT";

  @Override
  public void afterJob(JobExecution jobExecution) {
    ReportBuilder reportBuilder = ReportBuilder.of().writeHeader(jobExecution);

    jobExecution.getStepExecutions().forEach(reportBuilder::writeStepExecution);

    reportHelper.reportInformation(reportBuilder.writeFooter(jobExecution).build());
  }
}
