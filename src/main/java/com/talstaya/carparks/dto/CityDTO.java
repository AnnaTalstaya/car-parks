package com.talstaya.carparks.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CityDTO implements Serializable {
    private String id;
    private String name;
    private String country;
}
