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
package de.rahn.guidelines.springboot.app.jdbc.config;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

import de.rahn.guidelines.springboot.app.jdbc.domain.WithUUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.relational.core.mapping.event.BeforeSaveEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Frank Rahn
 */
@Configuration
@EnableJdbcRepositories(basePackages = {"de.rahn.guidelines.springboot.app.jdbc.domain"})
@Slf4j
public class AggregateConfiguration extends AbstractJdbcConfiguration {

  @Bean
  ApplicationListener<BeforeSaveEvent> idSetting() {
    return new WithUUID.BeforeUuidSaveListener();
  }

  @Bean
  AuditorAware<String> auditorAware() {
    return () -> {
      String currentAuditor =
          ofNullable(SecurityContextHolder.getContext().getAuthentication())
              .map(Authentication::getName)
              .orElse("anonymous");

      return of(currentAuditor);
    };
  }
}
