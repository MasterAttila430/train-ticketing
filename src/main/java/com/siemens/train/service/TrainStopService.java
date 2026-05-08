package com.siemens.train.service;

import com.siemens.train.exception.ResourceNotFoundException;
import com.siemens.train.model.Station;
import com.siemens.train.model.Train;
import com.siemens.train.model.TrainStop;
import com.siemens.train.repo.TrainStopRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainStopService {

    private final TrainStopRepository trainStopRepository;
    private final TrainService trainService;
    private final StationService stationService;

    public TrainStopService(TrainStopRepository trainStopRepository,
                            TrainService trainService,
                            StationService stationService) {
        this.trainStopRepository = trainStopRepository;
        this.trainService = trainService;
        this.stationService = stationService;
    }

    public List<TrainStop> getStopsForTrain(Long trainId) {
        // Verify train exists first
        trainService.getTrainById(trainId);
        return trainStopRepository.findByTrainIdOrderByStopOrder(trainId);
    }

    public TrainStop getTrainStopById(Long id) {
        return trainStopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "TrainStop with id " + id + " not found"));
    }

    public TrainStop createTrainStop(TrainStop trainStop) {
        // Verify train and station both exist
        Train train = trainService.getTrainById(trainStop.getTrain().getId());
        Station station = stationService.getStationById(trainStop.getStation().getId());

        // Check that the station is actually on the train's route
        if (!train.getRoute().getStations().contains(station)) {
            throw new ResourceNotFoundException(
                    "Station " + station.getName() + " is not on this train's route");
        }

        trainStop.setTrain(train);
        trainStop.setStation(station);
        return trainStopRepository.save(trainStop);
    }

    public TrainStop updateTrainStop(Long id, TrainStop updated) {
        TrainStop existing = getTrainStopById(id);
        existing.setArrivalTime(updated.getArrivalTime());
        existing.setDepartureTime(updated.getDepartureTime());
        existing.setStopOrder(updated.getStopOrder());
        return trainStopRepository.save(existing);
    }

    public void deleteTrainStop(Long id) {
        getTrainStopById(id); // throws if not found
        trainStopRepository.deleteById(id);
    }
}