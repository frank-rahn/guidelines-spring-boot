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
package de.rahn.guidelines.springboot.app.jpa.domain.history;

import static javax.persistence.AccessType.FIELD;

import java.io.Serializable;
import javax.persistence.Access;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

/**
 * @author Frank Rahn
 */
@Access(FIELD)
@Entity
@Table(name = "REVISION_INFO")
@RevisionEntity(UserRevisionListener.class)
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class UserRevisionEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(generator = "revision", strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(name = "revision", sequenceName = "REVISION_SEQ")
  @RevisionNumber
  @Column(name = "REVISION_ID")
  private int id;

  @RevisionTimestamp
  @Column(name = "REVISION_DATE")
  private long timestamp;

  @EqualsAndHashCode.Exclude
  private String userId;
}
