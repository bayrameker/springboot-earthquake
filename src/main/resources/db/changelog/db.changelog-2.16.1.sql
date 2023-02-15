--liquibase formatted sql
--changeset joyful:2.16.1 splitStatements:false failOnError:true dbms:postgresql

ALTER TABLE earth_event ALTER COLUMN event_time_diff SET DEFAULT 0;
