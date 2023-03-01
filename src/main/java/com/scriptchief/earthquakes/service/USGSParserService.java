package com.scriptchief.earthquakes.service;

import com.scriptchief.earthquakes.mapper.USGSEventMapper;
import com.scriptchief.earthquakes.model.entity.EarthEvent;
import com.scriptchief.earthquakes.util.ParserConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.ZoneId;
import java.util.*;

import static com.scriptchief.earthquakes.util.ConnectionConstants.*;
import static com.scriptchief.earthquakes.util.ConnectionConstants.REQUESTER;
import static java.util.Collections.singletonList;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class USGSParserService {
    private final EarthEventService earthEventService;
    private final USGSEventMapper usgsEventMapper;

    @Transactional
    public void parse() {
        final Document doc;
        try {
            doc = Jsoup.connect(NO_SCRIPT_URL)
                    .userAgent(BROWSER)
                    .timeout(TIMEOUT)
                    .referrer(REQUESTER)
                    .get();
        } catch (IOException e) {
            log.error("Can't parse USGS");
            throw new RuntimeException(e);
        }

        final Element feed = doc.selectFirst(ParserConstants.FEED_TAG);
        final Elements entries = feed.select(ParserConstants.ENTRY_TAG);

        final HashMap<String, List<EarthEvent>> locationNewEvents = new HashMap<>();
        for (Element entry : entries) {
            final EarthEvent event = usgsEventMapper.mapToEarthEvent(entry);

            locationNewEvents.merge(event.getLocation(), new ArrayList<>(singletonList(event)), (list1, list2) -> {
                list1.addAll(list2);
                list1.sort(comparing(EarthEvent::getTime));
                return list1;
            });
        }

        setEventsDiff(locationNewEvents);

        earthEventService.saveAll(locationNewEvents.values().stream()
                .flatMap(List::stream).collect(toList()));
    }

    private void setEventsDiff(HashMap<String, List<EarthEvent>> locationNewEvents) {
        for (Map.Entry<String, List<EarthEvent>> entry : locationNewEvents.entrySet()) {
            final List<EarthEvent> events = entry.getValue();

            final EarthEvent firstEvent = events.get(0);
            earthEventService.findLastTimeByLocation(entry.getKey())
                    .ifPresent(lastTime -> firstEvent.setTimeDiffSec(firstEvent.getTime().toEpochSecond()
                            - lastTime.toLocalDateTime().atZone(ZoneId.systemDefault()).toEpochSecond()));

            for (int i = 1; i < events.size(); i++) {
                final EarthEvent currentEvent = events.get(i);
                final long currentEventTime = currentEvent.getTime().toEpochSecond();
                final long previousEventTime = events.get(i - 1).getTime().toEpochSecond();

                currentEvent.setTimeDiffSec(currentEventTime - previousEventTime);
            }
        }
    }
}
