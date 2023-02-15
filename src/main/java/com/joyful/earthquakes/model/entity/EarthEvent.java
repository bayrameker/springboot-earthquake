package com.joyful.earthquakes.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.ZonedDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class EarthEvent {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "event_time")
    private ZonedDateTime time;

    private Double magnitude;

    private String region;
    private String location;

    @Column(name = "event_time_diff")
    private Long timeDiffSec;
}
