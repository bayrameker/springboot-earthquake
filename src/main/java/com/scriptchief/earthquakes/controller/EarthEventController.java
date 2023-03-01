package com.scriptchief.earthquakes.controller;

import com.scriptchief.earthquakes.model.dto.EarthEventReadDto;
import com.scriptchief.earthquakes.service.EarthEventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EarthEventController {
    private final EarthEventService earthEventService;

    @Operation(summary = "Get all events by location")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "No events for this location"),
            @ApiResponse(responseCode = "500", description = "Server error")})
    @GetMapping
    public List<EarthEventReadDto> findByLocationName(@RequestParam String location) {
        return earthEventService.findByLocationName(location);
    }

    @Operation(summary = "Get all locations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "No events for this location"),
            @ApiResponse(responseCode = "500", description = "Server error")})
    @GetMapping("/locations")
    public List<String> findLocations(@RequestParam(name = "days ago", required = false) String daysNumber) {
        return earthEventService.findLocations(daysNumber);
    }
}
