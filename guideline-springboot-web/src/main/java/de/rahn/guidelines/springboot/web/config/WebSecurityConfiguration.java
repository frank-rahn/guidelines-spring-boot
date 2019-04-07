package de.rahn.guidelines.springboot.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication().withUser("user").password("{noop}user").roles("USER");
    auth.inMemoryAuthentication()
        .withUser("admin")
        .password("{noop}admin")
        .roles("USER", "ADMIN", "ACTUATOR");
    auth.inMemoryAuthentication().withUser("gast").password("{noop}gast").roles("GAST");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers("/css/*.css", "/js/*.js", "/webjars/**")
        .permitAll()
        .anyRequest()
        .hasRole("USER");

    http.formLogin().loginPage("/login").permitAll();

    http.logout()
        .permitAll()
        .deleteCookies()
        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
  }
}
