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
package de.rahn.guidelines.springboot.batch.config;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
class DataSourceConfiguration {

  /** spring.datasource.url=jdbc:h2:mem:standard */
  @Bean
  @Primary
  @ConfigurationProperties(prefix = "spring.datasource")
  DataSourceProperties springDataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean
  @Primary
  @ConfigurationProperties(prefix = "spring.datasource.hikari")
  DataSource springDataSource() {
    return springDataSourceProperties()
        .initializeDataSourceBuilder()
        .type(HikariDataSource.class)
        .build();
  }

  /** batch.datasource.url=jdbc:hsqldb:mem:batch */
  @Bean
  @ConfigurationProperties(prefix = "batch.datasource")
  DataSourceProperties batchDataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean(name = {"datasource"})
  @ConfigurationProperties(prefix = "batch.datasource.hikari")
  DataSource batchDataSource() {
    return batchDataSourceProperties()
        .initializeDataSourceBuilder()
        .type(HikariDataSource.class)
        .build();
  }
}
