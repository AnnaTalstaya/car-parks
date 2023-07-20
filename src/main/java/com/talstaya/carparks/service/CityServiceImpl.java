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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    public Page<CityDTO> findAllCities(Pageable pageable) {
        Page<City> cities = cityRepository.findAll(pageable);
        List<CityDTO> cityDTOS =  cities.stream()
                .map(city -> modelMapper.map(city, CityDTO.class))
                .collect(Collectors.toList());
        return new PageImpl<>(cityDTOS, pageable, cities.getTotalElements());
    }

    @Override
    public Page<CarParkDTO> findClosestCarParks(String cityId, Geolocation location, boolean showOnlyAvailable, double maxDistanceInM, Pageable pageable) {
        Page<CarPark> carParks;
        if (location == null) {
            carParks = carParkRepository.findAllByCityIdAndByAvailability(cityId, showOnlyAvailable, pageable);
        } else {
            carParks = carParkRepository.findClosestByCityIdAndByAvailability(cityId, showOnlyAvailable, location.getLongitude(), location.getLatitude(), maxDistanceInM, pageable);
        }
        List<CarParkDTO> carParkDTOS = carParks.stream()
                .map(carPark -> modelMapper.map(carPark, CarParkDTO.class))
                .collect(Collectors.toList());
        return new PageImpl<>(carParkDTOS, pageable, carParks.getTotalElements());
    }
}
