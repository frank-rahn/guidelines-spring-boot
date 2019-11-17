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

import static java.time.format.DateTimeFormatter.ISO_DATE;

import de.rahn.guidlines.springboot.batch.job.userimport.Person;
import de.rahn.guidlines.springboot.batch.job.userimport.UserImportPersonProcessor;
import java.beans.PropertyEditorSupport;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Collection;
import javax.sql.DataSource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.validation.DataBinder;

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
        .names("firstName", "lastName", "emailAddress", "birthday")
        .fieldSetMapper(
            new BeanWrapperFieldSetMapper<>() {
              {
                setTargetType(Person.class);
              }

              @Override
              protected void initBinder(DataBinder binder) {
                super.initBinder(binder);

                binder.registerCustomEditor(
                    LocalDate.class,
                    new PropertyEditorSupport() {
                      @Override
                      public void setAsText(String text) throws IllegalArgumentException {
                        if (StringUtils.isNotEmpty(text)) {
                          setValue(LocalDate.parse(text, ISO_DATE));
                        } else {
                          setValue(null);
                        }
                      }

                      @Override
                      public String getAsText() {
                        var date = getValue();
                        if (date != null) {
                          return ((LocalDate) date).format(ISO_DATE);
                        }

                        return "";
                      }
                    });
              }
            })
        .build();
  }

  @Bean
  JdbcBatchItemWriter<Person> userImportPersonWriter(DataSource dataSource) {
    return new JdbcBatchItemWriterBuilder<Person>()
        .sql(
            "INSERT INTO people (first_name, last_name, email_address, birth_day) "
                + "VALUES (?, ?, ?, ?)")
        .itemPreparedStatementSetter(
            (person, ps) -> {
              ps.setString(1, person.getFirstName());
              ps.setString(2, person.getLastName());
              ps.setString(3, person.getEmailAddress());
              ps.setDate(4, Date.valueOf(person.getBirthday()));
            })
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
    var builder =
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
    var builder =
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
