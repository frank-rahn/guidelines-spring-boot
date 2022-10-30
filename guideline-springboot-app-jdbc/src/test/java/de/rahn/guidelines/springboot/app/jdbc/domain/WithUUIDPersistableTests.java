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
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.data.relational.core.conversion.MutableAggregateChange;
import org.springframework.data.relational.core.mapping.event.BeforeSaveEvent;

/**
 * @author Frank Rahn
 */
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class WithUUIDPersistableTests {

  private final BeforeUuidSaveListener classUnderTest = new BeforeUuidSaveListener();

  @Test
  void given_BeforeSaveEvent_with_Person_without_Uuid_when_onApplicationEvent_then_sets_Uuid() {
    // Given
    var birthday = LocalDate.of(1967, 5, 5);
    var entity = new Person("Rahn", birthday);
    var change = MutableAggregateChange.forSave(entity);
    var event = new BeforeSaveEvent<>(entity, change);

    // When
    classUnderTest.onApplicationEvent(event);

    // Then
    assertThat(entity).extracting(Person::isNew, InstanceOfAssertFactories.BOOLEAN).isFalse();
    assertThat(entity).extracting(Person::getId).isNotNull();
  }

  @Test
  void given_BeforeSaveEvent_with_Person_with_Uuid_when_onApplicationEvent_then_sets_Uuid() {
    // Given
    var birthday = LocalDate.of(1967, 5, 5);
    var uuid = UUID.randomUUID();
    var entity = new Person("Rahn", birthday);
    entity.id = uuid;
    var change = MutableAggregateChange.forSave(entity);
    var event = new BeforeSaveEvent<>(entity, change);

    // When
    classUnderTest.onApplicationEvent(event);

    // Then
    assertThat(entity).extracting(Person::isNew, InstanceOfAssertFactories.BOOLEAN).isFalse();
    assertThat(entity).extracting(Person::getId).isEqualTo(uuid);
  }

  @Test
  void given_BeforeSaveEvent_with_Address_when_onApplicationEvent_then_do_nothing() {
    // Given
    var entity = new Address("street", "city");
    var change = MutableAggregateChange.forSave(entity);
    var event = new BeforeSaveEvent<>(entity, change);

    // When
    classUnderTest.onApplicationEvent(event);

    // Then
    assertThat(entity).extracting(Address::getStreet).isEqualTo("street");
    assertThat(entity).extracting(Address::getCity).isEqualTo("city");
  }
}
