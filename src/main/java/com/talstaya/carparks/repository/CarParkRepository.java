package com.talstaya.carparks.repository;

import com.talstaya.carparks.entity.CarPark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CarParkRepository extends MongoRepository<CarPark, String> {
    @Query(value = "{$and: [{cityId: ?0}, " +
            "{ $or: [{$expr: { $eq: [?1, false] } }, { $expr: { $gt: [{$subtract: ['$capacity', '$freeSpots']}, 0] } }] }] }")
    Page<CarPark> findAllByCityIdAndByAvailability(String cityId, boolean showOnlyAvailable, Pageable pageable);

    @Query(value = "{$and: [{cityId: ?0}, " +
            "{ $or: [{$expr: { $eq: [?1, false] } }, { $expr: { $gt: [{$subtract: ['$capacity', '$freeSpots']}, 0] } }] }, " +
            "{ location : { $near : { $geometry : { type : 'Point', coordinates : [ ?2, ?3 ] }, $maxDistance : ?4 } } }] }")
    Page<CarPark> findClosestByCityIdAndByAvailability(String cityId, boolean showOnlyAvailable,
                                                       double longitude, double latitude, double maxDistanceInM,
                                                       Pageable pageable);
}
