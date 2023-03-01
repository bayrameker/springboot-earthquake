package com.scriptchief.earthquakes.mapper;

import com.scriptchief.earthquakes.model.dto.EarthEventReadDto;
import com.scriptchief.earthquakes.model.entity.EarthEvent;
import com.scriptchief.earthquakes.util.ParserConstants;
import org.springframework.stereotype.Component;

import java.time.temporal.TemporalUnit;
import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Component
public class EarthEventMapper {

    public List<EarthEventReadDto> toDtoList(List<EarthEvent> eventList) {
        final List<EarthEvent> sortedEvents = eventList.stream()
                .sorted(comparing(EarthEvent::getTime).reversed())
                .collect(toList());

        return sortedEvents.stream()
                .map(earthEvent -> EarthEventReadDto.builder()
                        .magnitude(String.valueOf(earthEvent.getMagnitude()))
                        .fullLocation(earthEvent.getRegion() + ParserConstants.COMMA + ParserConstants.SPACE + earthEvent.getLocation())
                        .time(earthEvent.getTime().toString())
                        .timeDiff(getTieDiff(earthEvent.getTimeDiffSec()))
//                        .hourFrequency(getFrequency(earthEvent, sortedEvents, HOURS) + SPACE + "min.")
//                        .hourFactor(getFactor(earthEvent, sortedEvents, HOURS) + SPACE + "min. difference")
//                        .dayFrequency(getFrequency(earthEvent, sortedEvents, DAYS) + SPACE + "min.")
//                        .dayFactor(getFactor(earthEvent, sortedEvents, DAYS) + SPACE + "min. difference")
                        .build())
                .collect(toList());
    }

    private String getTieDiff(Long diff) {
        if (diff == null) {
            return "no previous data";
        }

        long totalSeconds = diff;
        long totalMinutes = totalSeconds / 60;
        long totalHours = totalMinutes / 60;
        long totalDays = totalHours / 24;

        long hours = totalHours % 24;
        long minutes = totalMinutes % 60;
        long seconds = totalSeconds % 60;

        if (totalDays != 0) {
            return String.format("%d days %d hours %d min", totalDays, hours, minutes);
        }
        if (hours != 0) {
            return String.format("%dh %dmin", hours, minutes);

        }
        if (minutes != 0) {
            return String.format("%d min %d sec", minutes, seconds);

        } else {
            return String.format("%d seconds", seconds);
        }
    }

    private double getFactor(EarthEvent mainEvent, List<EarthEvent> eventList, TemporalUnit timeUnit) {
        final double oldFrequency = getFrequency(mainEvent, eventList, timeUnit, 2);
        final double currentFrequency = getFrequency(mainEvent, eventList, timeUnit, 1);
        return oldFrequency - currentFrequency;
    }

    private double getFrequency(EarthEvent mainEvent, List<EarthEvent> eventList, TemporalUnit timeUnit, int unitsNumber) {
        List<EarthEvent> lastEvents = eventList.stream()
                .filter(event -> event.getTime().isAfter(mainEvent.getTime().minus(unitsNumber, timeUnit)))
                .filter(event -> event.getTime().isBefore(mainEvent.getTime()))
                .filter(event -> event.getTimeDiffSec() != null)
                .collect(toList());

        final double diffSum = lastEvents.stream()
                .mapToLong(EarthEvent::getTimeDiffSec)
                .sum();

        return diffSum / lastEvents.size() / 60;
    }

    private double getFrequency(EarthEvent mainEvent, List<EarthEvent> eventList, TemporalUnit timeUnit) {
        return getFrequency(mainEvent, eventList, timeUnit, 1);
    }
}
