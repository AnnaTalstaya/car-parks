package com.talstaya.carparks.repository;

import com.talstaya.carparks.entity.CarPark;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarParkRepository extends MongoRepository<CarPark, String> {
    @Query(value = "{$and: [{cityId: ?0}, " +
            "{ $or: [{$expr: { $eq: [?1, false] } }, { $expr: { $gt: [{$subtract: ['$capacity', '$freeSpots']}, 0] } }] }] }")
    List<CarPark> findAllByCityIdAndByAvailability(String cityId, boolean showOnlyAvailable);

    @Query(value = "{$and: [{cityId: ?0}, " +
            "{ $or: [{$expr: { $eq: [?1, false] } }, { $expr: { $gt: [{$subtract: ['$capacity', '$freeSpots']}, 0] } }] }, " +
            "{ location : { $near : { $geometry : { type : 'Point', coordinates : [ ?2, ?3 ] }, $maxDistance : ?4 } } }] }")
    List<CarPark> findClosestByCityIdAndByAvailability(String cityId, boolean showOnlyAvailable,
                                                       double longitude, double latitude, double maxDistanceInM);
}
