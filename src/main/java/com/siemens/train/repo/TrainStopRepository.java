package com.siemens.train.repo;

import com.siemens.train.entities.TrainStopBE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainStopRepository extends JpaRepository<TrainStopBE, Long> {

    // Find all stops for a specific train, ordered by position
    List<TrainStopBE> findByTrainIdOrderByStopOrder(Long trainId);

    // Find all stops at a specific station
    List<TrainStopBE> findByStationId(Long stationId);
}