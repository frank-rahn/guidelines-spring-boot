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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @author Frank Rahn
 */
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
@EnableWebSecurity
class WebSecurityConfiguration {

  @Value("${spring.application.name}")
  private String applicationName;

  @Bean
  public UserDetailsService userDetailsService() {
    var user = User.withUsername("user").password("{noop}user").roles("USER").build();
    var gast = User.withUsername("gast").password("{noop}gast").roles("GAST").build();
    var admin = User.withUsername("admin").password("{noop}admin").roles("USER", "ADMIN").build();
    return new InMemoryUserDetailsManager(user, gast, admin);
  }

  @Bean
  @Order(1)
  public SecurityFilterChain actuatorSecurityFilterChain(HttpSecurity http) throws Exception {
    return http.requestMatcher(EndpointRequest.toAnyEndpoint())
        .authorizeRequests(customizer -> customizer.anyRequest().hasRole("ADMIN"))
        .httpBasic(customizer -> customizer.realmName(applicationName + "API"))
        .csrf()
        .disable()
        .build();
  }

  @Bean
  @Order(2)
  public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
    return http.authorizeRequests(
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
            })
        .build();
  }
}
