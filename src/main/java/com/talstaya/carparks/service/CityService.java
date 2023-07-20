package com.talstaya.carparks.service;

import com.talstaya.carparks.dto.CarParkDTO;
import com.talstaya.carparks.dto.CityDTO;
import com.talstaya.carparks.dto.Geolocation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CityService {

    Page<CityDTO> findAllCities(Pageable pageable);

    Page<CarParkDTO> findClosestCarParks(String cityId, Geolocation location, boolean showOnlyAvailable, double maxDistanceInM, Pageable pageable);
}
