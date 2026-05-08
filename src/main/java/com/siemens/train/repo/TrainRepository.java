package com.siemens.train.repo;

import com.siemens.train.model.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainRepository extends JpaRepository<Train, Long> {

    // Find all trains on a specific route
    List<Train> findByRouteId(Long routeId);

    // Find all delayed trains
    List<Train> findByDelayedTrue();

    // Check if a train with the given name exists
    boolean existsByName(String name);
}