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
package de.rahn.guidelines.springboot.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @author Frank Rahn
 */
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
class WebSecurityConfiguration {

  @Autowired
  void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication().withUser("user").password("{noop}user").roles("USER");
    auth.inMemoryAuthentication().withUser("gast").password("{noop}gast").roles("GAST");
    auth.inMemoryAuthentication().withUser("admin").password("{noop}admin").roles("USER", "ADMIN");
  }

  @Configuration
  @Order(1)
  static class ActuatorWebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.requestMatcher(EndpointRequest.toAnyEndpoint())
          .authorizeRequests(customizer -> customizer.anyRequest().hasRole("ADMIN"))
          .httpBasic(customizer -> customizer.realmName("Actuator-API"))
          .csrf(AbstractHttpConfigurer::disable);
    }
  }

  @Configuration
  @Order(2)
  static class WebWebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.authorizeRequests(
          customizer -> {
            customizer.antMatchers("/css/*.css", "/js/*.js", "/webjars/**").permitAll();
            customizer.anyRequest().hasRole("USER");
          })
          .formLogin(customizer -> customizer.loginPage("/login").permitAll())
          .logout(
              customizer -> {
                customizer.permitAll();
                customizer.deleteCookies();
                customizer.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
              });
    }
  }
}
