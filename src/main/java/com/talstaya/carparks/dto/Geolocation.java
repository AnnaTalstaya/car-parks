package com.talstaya.carparks.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Geolocation implements Serializable {
    private Double longitude;
    private Double latitude;
}
