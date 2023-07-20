package com.talstaya.carparks.controller;

import com.talstaya.carparks.dto.CarParkDTO;
import com.talstaya.carparks.dto.CityDTO;
import com.talstaya.carparks.dto.Geolocation;
import com.talstaya.carparks.service.CityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cities")
@Tag(name = "Cities API")
public class CityController {

    @Autowired
    private CityService cityService;

    @GetMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Retrieves a list of all existing cities")
    public Page<CityDTO> searchCities(Pageable pageable) {
        return cityService.findAllCities(pageable);
    }

    @GetMapping("/{cityId}/carParks")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Retrieves a list of existing car parks for specified city")
    public Page<CarParkDTO> searchCarParks(@PathVariable String cityId,
                                           @RequestParam(required = false) Double longitude,
                                           @RequestParam(required = false) Double latitude,
                                           @RequestParam(defaultValue = "false") boolean showOnlyAvailable,
                                           @RequestParam(defaultValue = "1000") double maxDistanceInM,
                                           Pageable pageable) {
        Geolocation location = null;
        if (longitude != null && latitude != null) {
            location = Geolocation.builder()
                    .longitude(longitude)
                    .latitude(latitude)
                    .build();
        }

        return cityService.findClosestCarParks(cityId, location, showOnlyAvailable, maxDistanceInM, pageable);
    }
}
