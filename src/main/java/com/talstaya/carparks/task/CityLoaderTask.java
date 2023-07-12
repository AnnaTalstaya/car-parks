package com.talstaya.carparks.task;

import com.talstaya.carparks.configuration.CityLoaderConfiguration;
import com.talstaya.carparks.loader.CityLoader;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class CityLoaderTask {
    @Autowired
    private CityLoader cityLoader;

    @Scheduled(fixedDelay = 60000)
    public void syncCarParksEvery1Min() {
        cityLoader.loadCities(CityLoaderConfiguration.CarParkSyncPolicy.EVERY_1_MINUTE);
    }
}
