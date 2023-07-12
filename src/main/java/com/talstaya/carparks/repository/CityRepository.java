package com.talstaya.carparks.repository;

import com.talstaya.carparks.entity.City;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends MongoRepository<City, String> {
}
