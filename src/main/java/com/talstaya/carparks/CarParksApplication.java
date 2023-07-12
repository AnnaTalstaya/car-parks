package com.talstaya.carparks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ConfigurationPropertiesScan
public class CarParksApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarParksApplication.class, args);
    }

}
