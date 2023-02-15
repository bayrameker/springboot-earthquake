--liquibase formatted sql
--changeset joyful:2.10.1 splitStatements:false failOnError:true dbms:postgresql

ALTER TABLE usgs_event RENAME TO earth_event;

ALTER TABLE earth_event ADD COLUMN event_time_diff BIGINT;

-- ALTER TABLE earth_event DROP COLUMN event_time_diff;
-- ALTER TABLE earth_event RENAME TO usgs_event;

