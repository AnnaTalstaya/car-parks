package com.talstaya.carparks.loader;

import com.talstaya.carparks.configuration.CityLoaderConfiguration;

public interface CityLoader {
    void loadCities(CityLoaderConfiguration.CarParkSyncPolicy syncPolicy);
}
