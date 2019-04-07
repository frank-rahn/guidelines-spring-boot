package de.rahn.guidelines.springboot.rest.config;

import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Configuration {

  @Value("${spring.application.name}")
  private String applicationName;

  @Value("${info.application.version}")
  private String version;

  @Value("${info.application.description}")
  private String description;

  @Bean
  Docket actuator() {
    return new Docket(DocumentationType.SWAGGER_2)
        .groupName("Actuator")
        .useDefaultResponseMessages(false)
        .apiInfo(actuatorInfo())
        .securitySchemes(Collections.singletonList(new BasicAuth("Actuator-API")))
        .enableUrlTemplating(true)
        .select()
        .apis(RequestHandlerSelectors.any())
        .paths(PathSelectors.regex("/actuator.*"))
        .build();
  }

  private ApiInfo actuatorInfo() {
    return new ApiInfoBuilder()
        .title("Spring Boot 2 Actuator API")
        .description("Documentation for Spring Boot Actuator")
        .termsOfServiceUrl("https://spring.io/projects/spring-boot")
        .build();
  }

  @Bean
  Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .groupName("API")
        .apiInfo(apiInfo())
        .tags(tagPeople())
        .securitySchemes(Collections.singletonList(new BasicAuth(applicationName + "-API")))
        .enableUrlTemplating(true)
        .select()
        .apis(RequestHandlerSelectors.any())
        .paths(PathSelectors.regex("/api.*"))
        .build();
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("Guidelines for Spring Boot API")
        .description(description)
        .version(version)
        .contact(contact())
        .license("Apache License Version 2.0")
        .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
        .termsOfServiceUrl("https://www.frank-rahn.de/")
        .build();
  }

  private Tag tagPeople() {
    return new Tag("People", "Zugriff auf die Personen");
  }

  private Contact contact() {
    return new Contact("Frank Rahn", "https://www.frank-rahn.de/", "frank@frank-rahn.de");
  }
}
