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

-- Tabelle Person
CREATE TABLE PERSON
(
    ID                 VARCHAR(36)  NOT NULL,
    FIRST_NAME         VARCHAR(255),
    LAST_NAME          VARCHAR(255) NOT NULL,
    EMAIL_ADDRESS      VARCHAR(255),
    BIRTHDAY           DATE         NOT NULL,
    CREATED_BY         VARCHAR(200),
    CREATED_DATE       DATETIME,
    LAST_MODIFIED_BY   VARCHAR(200),
    LAST_MODIFIED_DATE DATETIME,
    PRIMARY KEY (ID)
);

-- Tabelle Adresse
CREATE TABLE ADDRESS
(
    PERSON VARCHAR(36) NOT NULL,
    STREET VARCHAR(255),
    CITY   VARCHAR(255)
);

ALTER TABLE ADDRESS
    ADD CONSTRAINT FK_ADDRESS_PERSON
        FOREIGN KEY (PERSON)
            REFERENCES PERSON;
