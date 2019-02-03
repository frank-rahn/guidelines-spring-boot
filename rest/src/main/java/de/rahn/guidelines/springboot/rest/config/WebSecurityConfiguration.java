package de.rahn.guidelines.springboot.rest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityConfiguration {
	
	@Value("${spring.application.name}")
	private String applicationName;
	
	@Autowired
	protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("user").password("{noop}user").roles("USER");
		auth.inMemoryAuthentication().withUser("admin").password("{noop}admin").roles("USER", "ADMIN", "ACTUATOR");
		auth.inMemoryAuthentication().withUser("gast").password("{noop}gast").roles("GAST");
	}
	
	@Configuration
	@Order(1)
	protected class ActuatorWebSecurityConfiguration extends WebSecurityConfigurerAdapter {
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.antMatcher("/actuator/*")
					.authorizeRequests()
					.anyRequest()
					.hasRole("ADMIN");
			
			http.httpBasic().realmName("Actuator-API");
			
			http.csrf().disable();
		}
	}
	
	@Configuration
	@Order(2)
	protected class ApiWebSecurityConfiguration extends WebSecurityConfigurerAdapter {
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.antMatcher("/api/*")
					.authorizeRequests()
					.anyRequest()
					.hasRole("USER");
			
			http.httpBasic().realmName(applicationName + "-API");
			
			http.csrf().disable();
		}
	}
	
}