package com.talstaya.carparks.loader;

import com.jayway.jsonpath.JsonPath;
import com.talstaya.carparks.configuration.CityLoaderConfiguration;
import com.talstaya.carparks.entity.CarPark;
import com.talstaya.carparks.entity.City;
import com.talstaya.carparks.repository.CityRepository;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.talstaya.carparks.loader.CarParkConstants.*;

@Service
@Log4j2
public class CityLoaderImpl implements CityLoader {

    @Autowired
    private CityLoaderConfiguration cityLoaderConfiguration;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    @PostConstruct
    public void loadCitiesAndTheirStaticInfo() {
        loadCities(CityLoaderConfiguration.CarParkSyncPolicy.STATIC);
    }

    @Override
    public void loadCities(CityLoaderConfiguration.CarParkSyncPolicy syncPolicy) {
        log.info("Initializing city loader with sync policy: " + syncPolicy);

        for (var cityEntry : cityLoaderConfiguration.getCities().entrySet()) {
            log.info("Processing city = " + cityEntry.getValue().getName());
            processCity(cityEntry.getValue(), syncPolicy);
        }

        log.info("City loading finished");
    }

    private void processCity(CityLoaderConfiguration.CityConfig cityConfig, CityLoaderConfiguration.CarParkSyncPolicy syncPolicy) {
        if (syncPolicy == CityLoaderConfiguration.CarParkSyncPolicy.STATIC) {
            createCity(cityConfig);
        }

        log.info("Processing car parks with sync policy = " + syncPolicy);
        processCarParks(cityConfig.getId(), cityConfig.getSchedulingSyncsAndCarParks().get(syncPolicy));
    }

    private void createCity(CityLoaderConfiguration.CityConfig cityConfig) {
        var city = City.builder()
                .id(cityConfig.getId())
                .name(cityConfig.getName())
                .country(cityConfig.getCountry())
                .build();

        cityRepository.save(city);
    }

    private void processCarParks(String cityId, List<CityLoaderConfiguration.CarParkConfig> carParkConfigs) {
        for (var carParkConfig : carParkConfigs) {
            var carParksFromHttp = getCarParksFromHttp(carParkConfig.getSource());
            List<LinkedHashMap<String, Object>> carParksFields = JsonPath.read(carParksFromHttp, carParkConfig.getDataPath());

            Map<String, String> fieldMapping = carParkConfig.getFieldMapping();
            BulkOperations bulkOps = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, CarPark.class);
            for (var carParkFields : carParksFields) {
                String carParkIdValue = carParkFields.get(fieldMapping.get(ID_FIELD_NAME)).toString();
                Query query = new Query(Criteria.where("_id").is(carParkIdValue));

                Update update = setCarParkFieldForUpdate(cityId, fieldMapping, carParkFields);

                bulkOps.upsert(query, update);
            }

            bulkOps.execute();
        }
    }

    @SneakyThrows
    private String getCarParksFromHttp(String carParksUrl) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(carParksUrl))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == HttpStatus.OK.value()) {
            return response.body();
        } else {
            throw new RuntimeException("Failed to load car parks from URL: " + carParksUrl);
        }
    }

    private Update setCarParkFieldForUpdate(String cityId, Map<String, String> fieldMapping, LinkedHashMap<String, Object> carParkFields) {
        Update update = new Update();
        if (fieldMapping.containsKey(LATITUDE_FIELD_NAME) && fieldMapping.containsKey(LONGITUDE_FIELD_NAME)) {
            Double latitude = (Double) carParkFields.get(fieldMapping.get(LATITUDE_FIELD_NAME));
            Double longitude = (Double) carParkFields.get(fieldMapping.get(LONGITUDE_FIELD_NAME));
            update.set(LOCATION_FIELD_NAME, new GeoJsonPoint(latitude, longitude));
        }

        for (var fieldMappingEntry : fieldMapping.entrySet()) {
            boolean isNotLocationField = !fieldMappingEntry.getKey().equals(LATITUDE_FIELD_NAME) && !fieldMappingEntry.getKey().equals(LONGITUDE_FIELD_NAME);
            if (isNotLocationField) {
                update.set(fieldMappingEntry.getKey(), carParkFields.get(fieldMappingEntry.getValue()));
            }
        }
        update.set(CITY_ID_FIELD_NAME, cityId);
        update.currentDate(LAST_SYNC_FIELD_NAME);

        return update;
    }
}
