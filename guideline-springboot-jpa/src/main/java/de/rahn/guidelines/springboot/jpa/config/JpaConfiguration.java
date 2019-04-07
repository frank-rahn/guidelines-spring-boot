package de.rahn.guidelines.springboot.jpa.config;

import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@EnableJpaAuditing
public class JpaConfiguration {

  @Bean
  protected AuditorAware<String> auditorAware() {
    return () -> {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      if (authentication == null || !authentication.isAuthenticated()) {
        return Optional.of("anonymous");
      }

      return Optional.of(authentication.getName());
    };
  }
}
