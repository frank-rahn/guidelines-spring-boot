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
package de.rahn.guidelines.springboot.app.core.config.properties;

import static java.time.format.DateTimeFormatter.ISO_DATE;

import java.time.LocalDate;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * @author Frank Rahn
 */
@Component
@ConfigurationPropertiesBinding
class LocalDateConverter implements Converter<String, LocalDate> {

  @Override
  public LocalDate convert(String source) {
    //noinspection ConstantValue
    if (source == null) {
      throw new IllegalArgumentException("Argument source ist null");
    }

    return LocalDate.parse(source, ISO_DATE);
  }
}
