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

import static java.util.Collections.singletonList;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.DocExpansion;
import springfox.documentation.swagger.web.ModelRendering;
import springfox.documentation.swagger.web.OperationsSorter;
import springfox.documentation.swagger.web.TagsSorter;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
class Swagger2Configuration {

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
        .pathMapping("/")
        .directModelSubstitute(LocalDate.class, Date.class)
        .genericModelSubstitutes(List.class, ResponseEntity.class)
        .apiInfo(actuatorInfo())
        .securitySchemes(singletonList(new BasicAuth("Actuator-API")))
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
        .useDefaultResponseMessages(false)
        .pathMapping("/")
        .directModelSubstitute(LocalDate.class, Date.class)
        .genericModelSubstitutes(List.class, ResponseEntity.class)
        .apiInfo(apiInfo())
        .tags(tagPeople())
        .securitySchemes(singletonList(new BasicAuth(applicationName + "-API")))
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

  @Bean
  UiConfiguration uiConfiguration() {
    return UiConfigurationBuilder
        .builder()
        .deepLinking(true)
        .displayOperationId(false)
        .defaultModelsExpandDepth(1)
        .defaultModelExpandDepth(1)
        .defaultModelRendering(ModelRendering.EXAMPLE)
        .displayRequestDuration(true)
        .docExpansion(DocExpansion.NONE)
        .filter(false)
        .maxDisplayedTags(0)
        .operationsSorter(OperationsSorter.ALPHA)
        .showExtensions(true)
        .tagsSorter(TagsSorter.ALPHA)
        .supportedSubmitMethods(UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS)
        .validatorUrl(null)
        .build();
  }
}
