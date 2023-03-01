package com.scriptchief.earthquakes.util;

import com.scriptchief.earthquakes.model.LocationType;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

import static com.scriptchief.earthquakes.model.LocationType.LOCATION;
import static com.scriptchief.earthquakes.model.LocationType.REGION;
import static java.lang.Character.isLetter;
import static java.lang.Character.isLowerCase;
import static java.util.Arrays.stream;

@Log4j2
public final class ParserHelper {

    public static Map<LocationType, String> parseLocation(String fullLocation) {
        final HashMap<LocationType, String> resultMap = new HashMap<>();

        int separatorNum = fullLocation.indexOf(ParserConstants.COMMA);
        final String[] splitLocation = fullLocation.split(ParserConstants.SPACE);

        String region =  separatorNum == -1
                ? splitLocation[0]
                : fullLocation.substring(0, separatorNum);

        String locationSeparator = separatorNum == -1 ? ParserConstants.SPACE : ParserConstants.COMMA + ParserConstants.SPACE;
        String location = "";

        location = region.length() == fullLocation.length()
                ? region
                : fullLocation.substring(region.length() + locationSeparator.length());

        if (hasNotCapitalizedChar(location)) {
            region = region + ParserConstants.SPACE + location;
            location = region.split(ParserConstants.SPACE)[0];
        }

        resultMap.put(LOCATION, location);
        resultMap.put(REGION, region);
        return resultMap;
    }

    private static boolean hasNotCapitalizedChar(String region) {
        return stream(region.split(ParserConstants.SPACE))
                .anyMatch(word -> isLetter(word.charAt(0)) && isLowerCase(word.charAt(0)));

    }
}
