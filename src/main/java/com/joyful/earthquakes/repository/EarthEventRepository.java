package com.joyful.earthquakes.repository;

import com.joyful.earthquakes.model.entity.EarthEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;

public interface EarthEventRepository extends JpaRepository<EarthEvent, Long> {

    EarthEvent save(EarthEvent event);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN 'false' ELSE 'true' END FROM EarthEvent u WHERE u.time = ?1")
    Boolean notExists(ZonedDateTime time);

    List<EarthEvent> findAllByLocation(String location);

    @Query(nativeQuery = true, value = "SELECT max(event_time) FROM earth_event")
    Timestamp findMaxTime();

    @Query(nativeQuery = true, value = "SELECT max(event_time) FROM earth_event WHERE location = :location")
    Timestamp findMaxTimeByLocation(String location);

    @Query(nativeQuery = true,
            value = "SELECT DISTINCT location FROM earth_event WHERE event_time > :time")
    List<String> findLocationsByDaysNumber(Timestamp time);
}
