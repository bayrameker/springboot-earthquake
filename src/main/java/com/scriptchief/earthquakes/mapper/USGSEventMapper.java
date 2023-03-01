package com.scriptchief.earthquakes.mapper;

import com.scriptchief.earthquakes.model.LocationType;
import com.scriptchief.earthquakes.model.entity.EarthEvent;
import com.scriptchief.earthquakes.util.ParserHelper;
import com.scriptchief.earthquakes.util.ParserConstants;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.util.Map;

import static java.lang.Double.parseDouble;
import static java.time.ZonedDateTime.parse;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

@Component
public class USGSEventMapper {
    public EarthEvent mapToEarthEvent(Element htmlInfo) {

        final String title = htmlInfo.selectFirst(ParserConstants.TITLE_TAG).text();
        final String updated = htmlInfo.selectFirst(ParserConstants.TIME_TAG).text();

        final String[] splitTitle = title.split(ParserConstants.SPACE);

        String magnitude = splitTitle[1];
        String fullLocation = stream(splitTitle).skip(3).collect(joining(ParserConstants.SPACE));

        final Map<LocationType, String> locationMap = ParserHelper.parseLocation(fullLocation);

        return EarthEvent.builder()
                .magnitude(parseDouble(magnitude))
                .region(locationMap.get(LocationType.REGION))
                .location(locationMap.get(LocationType.LOCATION))
                .time(parse(updated))
                .build();
    }
}
