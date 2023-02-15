package com.joyful.earthquakes.mapper;

import com.joyful.earthquakes.model.LocationType;
import com.joyful.earthquakes.model.entity.EarthEvent;
import com.joyful.earthquakes.util.ParserHelper;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.joyful.earthquakes.util.ParserConstants.*;
import static java.lang.Double.parseDouble;
import static java.time.ZonedDateTime.parse;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

@Component
public class USGSEventMapper {
    public EarthEvent mapToEarthEvent(Element htmlInfo) {

        final String title = htmlInfo.selectFirst(TITLE_TAG).text();
        final String updated = htmlInfo.selectFirst(TIME_TAG).text();

        final String[] splitTitle = title.split(SPACE);

        String magnitude = splitTitle[1];
        String fullLocation = stream(splitTitle).skip(3).collect(joining(SPACE));

        final Map<LocationType, String> locationMap = ParserHelper.parseLocation(fullLocation);

        return EarthEvent.builder()
                .magnitude(parseDouble(magnitude))
                .region(locationMap.get(LocationType.REGION))
                .location(locationMap.get(LocationType.LOCATION))
                .time(parse(updated))
                .build();
    }
}
