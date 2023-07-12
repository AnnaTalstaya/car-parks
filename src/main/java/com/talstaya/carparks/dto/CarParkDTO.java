package com.talstaya.carparks.dto;

import lombok.Data;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.io.Serializable;

@Data
public class CarParkDTO implements Serializable {
    private String id;
    private String name;
    private String cityId;
    private String info;
    private GeoJsonPoint location;
    private Integer capacity;
    private Integer freeSpots;
}
