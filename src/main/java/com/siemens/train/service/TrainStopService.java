package com.siemens.train.service;

import com.siemens.train.api.CreateTrainStopRequest;
import com.siemens.train.api.TrainStopDTO;
import com.siemens.train.entities.StationBE;
import com.siemens.train.entities.TrainBE;
import com.siemens.train.entities.TrainStopBE;
import com.siemens.train.exception.ResourceNotFoundException;
import com.siemens.train.mapper.TrainStopMapper;
import com.siemens.train.repo.TrainStopRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainStopService {

    private final TrainStopRepository trainStopRepository;
    private final TrainService trainService;
    private final StationService stationService;
    private final TrainStopMapper trainStopMapper;

    public TrainStopService(TrainStopRepository trainStopRepository,
                            TrainService trainService,
                            StationService stationService,
                            TrainStopMapper trainStopMapper) {
        this.trainStopRepository = trainStopRepository;
        this.trainService = trainService;
        this.stationService = stationService;
        this.trainStopMapper = trainStopMapper;
    }

    public List<TrainStopDTO> getStopsForTrain(Long trainId) {
        trainService.getTrainEntityById(trainId);
        return trainStopRepository.findByTrainIdOrderByStopOrder(trainId).stream()
                .map(trainStopMapper::toDto)
                .collect(Collectors.toList());
    }

    public TrainStopDTO getTrainStopById(Long id) {
        return trainStopMapper.toDto(getTrainStopEntityById(id));
    }

    public TrainStopDTO createTrainStop(CreateTrainStopRequest request) {
        TrainBE train = trainService.getTrainEntityById(request.trainId());
        StationBE station = stationService.getStationEntityById(request.stationId());

        if (!train.getRoute().getStations().contains(station)) {
            throw new ResourceNotFoundException("Station " + station.getName() + " is not on this train's route");
        }

        TrainStopBE trainStop = new TrainStopBE(null, train, station, request.arrivalTime(), request.departureTime(), request.stopOrder());
        return trainStopMapper.toDto(trainStopRepository.save(trainStop));
    }

    public TrainStopDTO updateTrainStop(Long id, CreateTrainStopRequest request) {
        TrainStopBE existing = getTrainStopEntityById(id);
        existing.setArrivalTime(request.arrivalTime());
        existing.setDepartureTime(request.departureTime());
        existing.setStopOrder(request.stopOrder());
        return trainStopMapper.toDto(trainStopRepository.save(existing));
    }

    public void deleteTrainStop(Long id) {
        if (!trainStopRepository.existsById(id)) {
            throw new ResourceNotFoundException("TrainStop with id " + id + " not found");
        }
        trainStopRepository.deleteById(id);
    }

    // Helper for internal use
    public TrainStopBE getTrainStopEntityById(Long id) {
        return trainStopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TrainStop with id " + id + " not found"));
    }
}