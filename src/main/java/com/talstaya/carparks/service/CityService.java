package com.talstaya.carparks.service;

import com.talstaya.carparks.dto.CarParkDTO;
import com.talstaya.carparks.dto.CityDTO;
import com.talstaya.carparks.dto.Geolocation;

import java.util.List;

public interface CityService {

    List<CityDTO> findAllCities();

    List<CarParkDTO> findClosestCarParks(String cityId, Geolocation location, boolean showOnlyAvailable, double maxDistanceInKm);
}
