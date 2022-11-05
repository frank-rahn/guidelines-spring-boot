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

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;

/**
 * @author Frank Rahn
 */
@Configuration
@OpenAPIDefinition(
    info =
        @Info(
            title = "Guidelines for Spring Boot API",
            description = "Guidelines for Spring Boot - Rest",
            version = "1.0",
            termsOfService = "https://www.frank-rahn.de/",
            license =
                @License(
                    name = "Apache License Version 2.0",
                    url = "https://www.apache.org/licenses/LICENSE-2.0"),
            contact =
                @Contact(
                    name = "Frank Rahn",
                    url = "https://www.frank-rahn.de/",
                    email = "frank@frank-rahn.de")),
    security = {@SecurityRequirement(name = "basicAuth")},
    tags = {
      @Tag(name = "People", description = "API für den Zugriff auf die Personen"),
      @Tag(name = "Book", description = "API für den Zugriff auf die Bücher"),
      @Tag(name = "Actuator", description = "Spring Boot 2 Actuator API")
    })
@SecurityScheme(name = "basicAuth", type = SecuritySchemeType.HTTP, scheme = "basic")
class OpenAPIConfiguration {}
