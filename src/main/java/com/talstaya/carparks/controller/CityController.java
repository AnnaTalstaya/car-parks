package com.talstaya.carparks.controller;

import com.talstaya.carparks.dto.CarParkDTO;
import com.talstaya.carparks.dto.CityDTO;
import com.talstaya.carparks.dto.Geolocation;
import com.talstaya.carparks.service.CityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<CityDTO> searchCities() {
        return cityService.findAllCities();
    }

    @GetMapping("/{cityId}/carParks")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Retrieves a list of existing car parks for specified city")
    public List<CarParkDTO> searchCarParks(@PathVariable String cityId,
                                           @RequestParam(required = false) Double longitude,
                                           @RequestParam(required = false) Double latitude,
                                           @RequestParam(defaultValue = "false") boolean showOnlyAvailable,
                                           @RequestParam(defaultValue = "10") double maxDistanceInKm) {
        Geolocation location = null;
        if (longitude != null && latitude != null) {
            location = Geolocation.builder()
                    .longitude(longitude)
                    .latitude(latitude)
                    .build();
        }

        return cityService.findClosestCarParks(cityId, location, showOnlyAvailable, maxDistanceInKm);
    }
}
