package com.siemens.train.repo;

import com.siemens.train.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {

    // Find route by exact name
    Optional<Route> findByName(String name);

    // Find all routes that contain a specific station
    @Query("SELECT r FROM Route r JOIN r.stations s WHERE s.id = :stationId")
    List<Route> findByStationId(@Param("stationId") Long stationId);
}