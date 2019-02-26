package de.rahn.guidelines.springboot.rest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Configuration {
	
	@Value("${info.application.version}")
	private String version;
	
	@Value("${info.application.description}")
	private String description;
	
	@Bean
	Docket docket() {
		return new Docket(DocumentationType.SWAGGER_2)
//				.groupName("public-api")
				.apiInfo(apiInfo())
				.select()
					.apis(RequestHandlerSelectors.any())
					.paths(PathSelectors.any())
					.build()
				;
	}
	
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("Guidelines for Spring Boot")
				.description(description)
				.version(version)
				.contact(contact())
				.license("Apache License Version 2.0")
				.licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
				.termsOfServiceUrl("https://www.frank-rahn.de/")
				.build();
	}
	
	private Contact contact() {
		return new Contact("Frank Rahn", "https://www.frank-rahn.de/", "frank@frank-rahn.de");
	}
	
}