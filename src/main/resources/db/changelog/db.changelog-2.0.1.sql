--liquibase formatted sql
--changeset joyful:0.2.1 splitStatements:false failOnError:true dbms:postgresql

CREATE TABLE usgs_event
(
    id        BIGSERIAL,
    magnitude DOUBLE PRECISION NOT NULL,
    region    VARCHAR(256),
    location  VARCHAR(256) NOT NULL,
    event_time      TIMESTAMP UNIQUE NOT NULL,
    PRIMARY KEY (id)
);

CREATE INDEX usgs_event_time_index ON usgs_event (event_time);

-- DROP INDEX usgs_event_time_index;
-- DROP TABLE usgs_event;