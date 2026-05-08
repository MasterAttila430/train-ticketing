package com.siemens.train.repo;

import com.siemens.train.entities.TrainBE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainRepository extends JpaRepository<TrainBE, Long> {

    // Find all trains on a specific route
    List<TrainBE> findByRouteId(Long routeId);

    // Find all delayed trains
    List<TrainBE> findByDelayedTrue();

    // Check if a train with the given name exists
    boolean existsByName(String name);
}