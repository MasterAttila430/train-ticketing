package com.siemens.train.repo;

import com.siemens.train.model.TrainStop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainStopRepository extends JpaRepository<TrainStop, Long> {

    // Find all stops for a specific train, ordered by position
    List<TrainStop> findByTrainIdOrderByStopOrder(Long trainId);

    // Find all stops at a specific station
    List<TrainStop> findByStationId(Long stationId);
}