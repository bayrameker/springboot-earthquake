package com.scriptchief.earthquakes.model.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class EarthEventReadDto {
    String magnitude;
    String fullLocation;
    String time;
    String timeDiff;
//    String hourFrequency;
//    String hourFactor;
//    String dayFrequency;
//    String dayFactor;
}
