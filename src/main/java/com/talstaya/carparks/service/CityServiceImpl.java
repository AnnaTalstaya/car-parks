package com.talstaya.carparks.service;

import com.talstaya.carparks.dto.CarParkDTO;
import com.talstaya.carparks.dto.CityDTO;
import com.talstaya.carparks.dto.Geolocation;
import com.talstaya.carparks.entity.CarPark;
import com.talstaya.carparks.entity.City;
import com.talstaya.carparks.repository.CarParkRepository;
import com.talstaya.carparks.repository.CityRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityServiceImpl implements CityService {

    @Autowired
    private CarParkRepository carParkRepository;
    @Autowired
    private CityRepository cityRepository;
    private final ModelMapper modelMapper;

    public CityServiceImpl() {
        modelMapper = new ModelMapper();
    }

    @Override
    public List<CityDTO> findAllCities() {
        List<City> cities = cityRepository.findAll();
        return cities.stream()
                .map(city -> modelMapper.map(city, CityDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<CarParkDTO> findClosestCarParks(String cityId, Geolocation location, boolean showOnlyAvailable, double maxDistanceInM) {
        List<CarPark> carParks;
        if (location == null) {
            carParks = carParkRepository.findAllByCityIdAndByAvailability(cityId, showOnlyAvailable);
        } else {
            carParks = carParkRepository.findClosestByCityIdAndByAvailability(cityId, showOnlyAvailable, location.getLongitude(), location.getLatitude(), maxDistanceInM);
        }
        return carParks.stream()
                .map(carPark -> modelMapper.map(carPark, CarParkDTO.class))
                .collect(Collectors.toList());
    }
}
