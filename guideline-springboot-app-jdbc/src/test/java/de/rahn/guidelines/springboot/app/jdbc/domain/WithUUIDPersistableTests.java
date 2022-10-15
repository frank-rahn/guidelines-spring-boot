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

import static org.assertj.core.api.Assertions.assertThat;

import de.rahn.guidelines.springboot.app.jdbc.domain.WithUUIDPersistable.BeforeUuidSaveListener;
import de.rahn.guidelines.springboot.app.jdbc.domain.people.Address;
import de.rahn.guidelines.springboot.app.jdbc.domain.people.Person;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.data.relational.core.conversion.AggregateChange;
import org.springframework.data.relational.core.conversion.MutableAggregateChange;
import org.springframework.data.relational.core.mapping.event.BeforeSaveEvent;

/** @author Frank Rahn */
class WithUUIDPersistableTests {

  private final BeforeUuidSaveListener classUnderTest = new BeforeUuidSaveListener();

  @Test
  void givenBeforeSaveEventWithPersonWithoutUUID_whenOnApplicationEvent_thenSetUuid() {
    // Given
    LocalDate birthday = LocalDate.of(1967, 5, 5);
    Person entity = new Person("Rahn", birthday);
    AggregateChange<Person> change = MutableAggregateChange.forSave(entity);
    BeforeSaveEvent<Person> event = new BeforeSaveEvent<>(entity, change);

    // When
    classUnderTest.onApplicationEvent(event);

    // Then
    assertThat(entity.isNew()).isFalse();
    assertThat(entity.getId()).isNotNull();
  }

  @Test
  void givenBeforeSaveEventWithPersonWithUUID_whenOnApplicationEvent_thenSetUuid() {
    // Given
    LocalDate birthday = LocalDate.of(1967, 5, 5);
    UUID uuid = UUID.randomUUID();
    Person entity = new Person("Rahn", birthday);
    entity.id = uuid;
    AggregateChange<Person> change = MutableAggregateChange.forSave(entity);
    BeforeSaveEvent<Person> event = new BeforeSaveEvent<>(entity, change);

    // When
    classUnderTest.onApplicationEvent(event);

    // Then
    assertThat(entity.isNew()).isFalse();
    assertThat(entity.getId()).isEqualTo(uuid);
  }

  @Test
  void givenBeforeSaveEventWithAddress_whenOnApplicationEvent_thenDoNothing() {
    // Given
    Address entity = new Address("street", "city");
    AggregateChange<Address> change = MutableAggregateChange.forSave(entity);
    BeforeSaveEvent<Address> event = new BeforeSaveEvent<>(entity, change);

    // When
    classUnderTest.onApplicationEvent(event);

    // Then
    assertThat(entity.getStreet()).isEqualTo("street");
    assertThat(entity.getCity()).isEqualTo("city");
  }
}
