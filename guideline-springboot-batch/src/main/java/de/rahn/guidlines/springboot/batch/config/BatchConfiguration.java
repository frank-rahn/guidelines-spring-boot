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
package de.rahn.guidlines.springboot.batch.config;

import de.rahn.guidlines.springboot.batch.job.userimport.Person;
import de.rahn.guidlines.springboot.batch.job.userimport.UserImportPersonProcessor;
import java.util.Collection;
import javax.sql.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * @author Frank Rahn
 */
@Configuration
@EnableBatchProcessing
class BatchConfiguration {

  @Bean
  FlatFileItemReader<Person> userImportPersonReader() {
    return new FlatFileItemReaderBuilder<Person>()
        .name("userImportPersonReader")
        .resource(new ClassPathResource("sample-data.csv"))
        .linesToSkip(1)
        .delimited()
        .names("firstName", "lastName")
        .fieldSetMapper(
            new BeanWrapperFieldSetMapper<Person>() {
              {
                setTargetType(Person.class);
              }
            })
        .build();
  }

  @Bean
  JdbcBatchItemWriter<Person> userImportPersonWriter(DataSource dataSource) {
    return new JdbcBatchItemWriterBuilder<Person>()
        .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
        .sql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)")
        .dataSource(dataSource)
        .build();
  }

  @Bean
  Step userImportStep(
      StepBuilderFactory stepBuilderFactory,
      FlatFileItemReader<Person> userImportPersonReader,
      UserImportPersonProcessor userImportPersonProcessor,
      JdbcBatchItemWriter<Person> userImportPersonWriter,
      Collection<StepExecutionListener> stepExecutionListeners) {
    SimpleStepBuilder builder =
        stepBuilderFactory
            .get("userImportStep")
            .<Person, Person>chunk(2)
            .faultTolerant()
            .skipLimit(1)
            .skip(RuntimeException.class)
            .reader(userImportPersonReader)
            .processor(userImportPersonProcessor)
            .writer(userImportPersonWriter);

    if (stepExecutionListeners != null) {
      stepExecutionListeners.forEach(builder::listener);
    }

    return builder.build();
  }

  @Bean
  Job userImportJob(
      JobBuilderFactory jobBuilderFactory,
      Step userImportStep,
      Collection<JobExecutionListener> jobExecutionListeners) {
    SimpleJobBuilder builder =
        jobBuilderFactory
            .get("userImportJob")
            .incrementer(new RunIdIncrementer())
            .start(userImportStep);

    if (jobExecutionListeners != null) {
      jobExecutionListeners.forEach(builder::listener);
    }

    return builder.build();
  }
}
