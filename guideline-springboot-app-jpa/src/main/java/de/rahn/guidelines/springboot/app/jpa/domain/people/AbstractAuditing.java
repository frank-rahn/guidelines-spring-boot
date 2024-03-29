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
package de.rahn.guidelines.springboot.app.jpa.domain.people;

import static javax.persistence.AccessType.FIELD;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Access;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author Frank Rahn
 */
@Access(FIELD)
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
@Audited
@Getter
@Setter
public abstract class AbstractAuditing implements Serializable {

  private static final long serialVersionUID = 1L;

  @Version private long version;

  @CreatedDate
  @DateTimeFormat(iso = DATE_TIME)
  private LocalDateTime createdDate;

  @CreatedBy private String createdBy;

  @LastModifiedDate
  @DateTimeFormat(iso = DATE_TIME)
  private LocalDateTime lastModifiedDate;

  @LastModifiedBy private String lastModifiedBy;
}
