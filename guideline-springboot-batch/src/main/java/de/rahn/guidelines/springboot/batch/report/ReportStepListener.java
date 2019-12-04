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

import de.rahn.guidelines.springboot.batch.report.support.ReportHelper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.listener.ItemListenerSupport;
import org.springframework.stereotype.Component;

/**
 * @author Frank Rahn
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class ReportStepListener extends ItemListenerSupport<Object, Object>
    implements SkipListener<Object, Object> {

  private final ReportHelper reportHelper;

  @Override
  public void onProcessError(Object item, Exception exception) {
    final String message = "Exception inside ItemProcessor for Item -> ";

    reportHelper.reportThrowable(message, item, exception, LOGGER);
  }

  @Override
  public void onWriteError(Exception exception, List<?> items) {
    final String message = "Exception inside ItemProcessor for Items -> ";

    reportHelper.reportThrowable(message, items, exception, LOGGER);
  }

  @Override
  public void onSkipInRead(Throwable throwable) {
    final String message = "Skippable Exception inside ItemReader -> ";

    reportHelper.reportThrowable(message, null, throwable, LOGGER);
  }

  @Override
  public void onSkipInWrite(Object item, Throwable throwable) {
    final String message = "Skippable Exception inside ItemWriter for Item -> ";

    reportHelper.reportThrowable(message, item, throwable, LOGGER);
  }

  @Override
  public void onSkipInProcess(Object item, Throwable throwable) {
    final String message = "Skippable Exception inside ItemProcessor for Item -> ";

    reportHelper.reportThrowable(message, item, throwable, LOGGER);
  }
}
