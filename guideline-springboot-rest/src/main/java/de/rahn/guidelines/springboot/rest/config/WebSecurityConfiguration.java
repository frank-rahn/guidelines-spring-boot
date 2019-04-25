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
package de.rahn.guidelines.springboot.rest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
class WebSecurityConfiguration {

  @Value("${spring.application.name}")
  private String applicationName;

  @Autowired
  void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication().withUser("user").password("{noop}user").roles("USER");
    auth.inMemoryAuthentication().withUser("gast").password("{noop}gast").roles("GAST");
    auth.inMemoryAuthentication()
        .withUser("admin")
        .password("{noop}admin")
        .roles("USER", "ADMIN", "ACTUATOR");
  }

  @Configuration
  @Order(1)
  class ActuatorWebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.antMatcher("/actuator/*").authorizeRequests().anyRequest().hasRole("ADMIN");

      http.httpBasic().realmName("Actuator-API");

      http.csrf().disable();
    }
  }

  @Configuration
  @Order(2)
  class ApiWebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.antMatcher("/api/*").authorizeRequests().anyRequest().hasRole("USER");

      http.httpBasic().realmName(applicationName + "-API");

      http.csrf().disable();
    }
  }
}
