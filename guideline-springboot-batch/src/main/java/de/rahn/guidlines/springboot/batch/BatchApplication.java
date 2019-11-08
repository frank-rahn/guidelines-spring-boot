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
package de.rahn.guidlines.springboot.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * @author Frank Rahn
 */
@SpringBootApplication
public class BatchApplication {

  private static final Logger LOGGER = LoggerFactory.getLogger(BatchApplication.class);

  public static void main(String... args) {
    ApplicationContext applicationContext = SpringApplication.run(BatchApplication.class, args);

    int exitCode = SpringApplication.exit(applicationContext);
    LOGGER.info("Batch finished with exit code {}", exitCode);
    System.exit(exitCode);
  }
}
