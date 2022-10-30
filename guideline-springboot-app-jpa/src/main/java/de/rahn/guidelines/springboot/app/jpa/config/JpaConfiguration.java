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
package de.rahn.guidelines.springboot.app.jpa.config;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

import de.rahn.guidelines.springboot.app.jpa.domain.people.PersonRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Frank Rahn
 */
@Configuration
@EnableJpaRepositories(
    basePackageClasses = {PersonRepository.class},
    repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)
@EnableJpaAuditing
public class JpaConfiguration {

  @Bean
  AuditorAware<String> auditorAware() {
    return () -> {
      var currentAuditor =
          ofNullable(SecurityContextHolder.getContext().getAuthentication())
              .map(Authentication::getName)
              .orElse("anonymous");

      return of(currentAuditor);
    };
  }
}
