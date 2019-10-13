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
import de.rahn.guidlines.springboot.batch.job.userimport.UserImportJobCompletionNotificationListener;
import de.rahn.guidlines.springboot.batch.job.userimport.UserImportPersonProcessor;
import de.rahn.guidlines.springboot.batch.job.userimport.UserImportStepExecutionListener;
import javax.sql.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

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
        .names(new String[]{"firstName", "lastName"})
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
      UserImportStepExecutionListener userImportStepListener) {
    return stepBuilderFactory
        .get("userImportStep")
        .listener(userImportStepListener)
        .<Person, Person>chunk(2)
        .reader(userImportPersonReader)
        .processor(userImportPersonProcessor)
        .writer(userImportPersonWriter)
        .build();
  }

  @Bean
  Job userImportJob(
      Step userImportStep,
      JobBuilderFactory jobBuilderFactory,
      UserImportJobCompletionNotificationListener listener) {
    return jobBuilderFactory
        .get("userImportJob")
        .incrementer(new RunIdIncrementer())
        .listener(listener)
        .flow(userImportStep)
        .end()
        .build();
  }
}
