DROP TABLE IF EXISTS people;

CREATE TABLE people
(
    person_id     BIGINT IDENTITY NOT NULL PRIMARY KEY,
    first_name    VARCHAR(20)     NOT NULL,
    last_name     VARCHAR(20)     NOT NULL,
    email_address VARCHAR(20),
    birth_day     DATE
);