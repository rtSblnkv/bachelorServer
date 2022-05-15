package com.diploma.logisticsService.repository;

import com.diploma.logisticsService.models.dto.NodeDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface NodeRepository extends CrudRepository<NodeDTO, Long> {

    Optional<NodeDTO> findFirstByLatitudeAndLongitude(double lat,double lon);

    @Query("select node.id from NodeDTO node " +
            "where node.latitude = :lat " +
            "and node.longitude = :lon")
    Optional<Long> findFirstIdByLatitudeAndLongitude(double lat, double lon);
}
