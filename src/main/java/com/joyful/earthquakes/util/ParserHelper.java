package com.joyful.earthquakes.util;

import com.joyful.earthquakes.model.LocationType;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

import static com.joyful.earthquakes.model.LocationType.LOCATION;
import static com.joyful.earthquakes.model.LocationType.REGION;
import static com.joyful.earthquakes.util.ParserConstants.COMMA;
import static com.joyful.earthquakes.util.ParserConstants.SPACE;
import static java.lang.Character.isLetter;
import static java.lang.Character.isLowerCase;
import static java.util.Arrays.stream;

@Log4j2
public final class ParserHelper {

    public static Map<LocationType, String> parseLocation(String fullLocation) {
        final HashMap<LocationType, String> resultMap = new HashMap<>();

        int separatorNum = fullLocation.indexOf(COMMA);
        final String[] splitLocation = fullLocation.split(SPACE);

        String region =  separatorNum == -1
                ? splitLocation[0]
                : fullLocation.substring(0, separatorNum);

        String locationSeparator = separatorNum == -1 ? SPACE : COMMA + SPACE;
        String location = "";

        location = region.length() == fullLocation.length()
                ? region
                : fullLocation.substring(region.length() + locationSeparator.length());

        if (hasNotCapitalizedChar(location)) {
            region = region + SPACE + location;
            location = region.split(SPACE)[0];
        }

        resultMap.put(LOCATION, location);
        resultMap.put(REGION, region);
        return resultMap;
    }

    private static boolean hasNotCapitalizedChar(String region) {
        return stream(region.split(SPACE))
                .anyMatch(word -> isLetter(word.charAt(0)) && isLowerCase(word.charAt(0)));

    }
}
