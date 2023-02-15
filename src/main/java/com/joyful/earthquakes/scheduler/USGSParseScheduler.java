package com.joyful.earthquakes.scheduler;

import com.joyful.earthquakes.service.USGSParserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class USGSParseScheduler {

    private final USGSParserService parserService;

    @Scheduled(cron = " */60 * * * * *")
    public void parseUSGS() {
        parserService.parse();
    }
}
