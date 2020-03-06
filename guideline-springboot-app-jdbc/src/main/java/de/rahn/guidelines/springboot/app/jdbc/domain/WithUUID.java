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
package de.rahn.guidelines.springboot.app.jdbc.domain;

import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.event.BeforeSaveEvent;

/**
 * @author Frank Rahn
 */
@ToString(includeFieldNames = false)
@EqualsAndHashCode
public abstract class WithUUID {

  @Id
  @Getter
  protected UUID id;

  @Slf4j
  public static class BeforeUuidSaveListener implements ApplicationListener<BeforeSaveEvent> {

    @Override
    public void onApplicationEvent(BeforeSaveEvent event) {
      if (event.getEntity() instanceof WithUUID) {
        WithUUID entity = (WithUUID) event.getEntity();
        entity.id = UUID.randomUUID();

        LOGGER.info("BeforeSaveEvent aufgerufen: Entity={}", entity);
      }
    }
  }
}
