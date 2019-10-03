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

import java.net.MalformedURLException;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.UrlResource;

@Configuration
@EnableBatchProcessing
class BatchConfiguration {

  /* TODO */
  public static class Item {

  }

  public static class Result {

  }

  @Bean
  JsonItemReader<Item> jsonItemReader() throws MalformedURLException {
    return new JsonItemReaderBuilder<Item>()
        .jsonObjectReader(new JacksonJsonObjectReader<>(Item.class))
        .resource(new UrlResource("https://www.frank-rahn.de/items.json"))
        .name("itemJsonItemReader")
        .build();
  }

  @Bean
  ItemProcessor<Item, Result> itemProcessor() {
    return item -> new Result();
  }

  @Bean
  FlatFileItemWriter<Result> itemWriter() {
    return new FlatFileItemWriterBuilder<Result>()
        .name("itemWriter")
        .resource(new FileSystemResource("out/result.csv"))
        .delimited()
        .delimiter(",")
        .names(new String[]{"result"})
        .build();
  }

  @Bean
  Step firstStep(
      StepBuilderFactory stepBuilderFactory,
      ItemReader<Item> jsonItemReader,
      ItemProcessor<Item, Result> itemProcessor,
      ItemWriter<Result> itemWriter,
      StepExecutionListener firstStepListener) {
    return stepBuilderFactory
        .get("firstStep")
        .listener(firstStepListener)
        .<Item, Result>chunk(10)
        .reader(jsonItemReader)
        .processor(itemProcessor)
        .writer(itemWriter)
        .build();
  }

  @Bean
  Job firstJob(Step firstStep, JobBuilderFactory jobBuilderFactory) {
    return jobBuilderFactory
        .get("firstJob")
        .incrementer(new RunIdIncrementer())
        .flow(firstStep)
        .end()
        .build();
  }

  @Bean
  StepExecutionListener firstStepListener() {
    return new StepExecutionListener() {

      @Override
      public void beforeStep(StepExecution stepExecution) {
        stepExecution.getExecutionContext().put("start", System.currentTimeMillis());

        System.out.println("Step name:" + stepExecution.getStepName() + " Started");
      }

      @Override
      public ExitStatus afterStep(StepExecution stepExecution) {
        long elapsed =
            System.currentTimeMillis() - stepExecution.getExecutionContext().getLong("start");

        System.out.println(
            "Step name:"
                + stepExecution.getStepName()
                + " Ended. Running time is "
                + elapsed
                + " milliseconds.");
        System.out.println(
            "Read Count:"
                + stepExecution.getReadCount()
                + " Write Count:"
                + stepExecution.getWriteCount());
        return ExitStatus.COMPLETED;
      }
    };
  }
}
