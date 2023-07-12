package com.talstaya.carparks.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "city-loader")
public class CityLoaderConfiguration {
    private Map<String, CityConfig> cities = new HashMap<>();

    @Data
    public static class CityConfig {
        private String id;
        private String country;
        private String name;
        private Map<CarParkSyncPolicy, List<CarParkConfig>> schedulingSyncsAndCarParks = new HashMap<>();
    }

    public enum CarParkSyncPolicy {
        STATIC,
        EVERY_1_MINUTE
    }

    @Data
    public static class CarParkConfig {
        private CarParkSyncPolicy syncPolicy;
        private String source;
        private String dataPath;
        private Map<String, String> fieldMapping = new HashMap<>();
    }
}
