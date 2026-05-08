package com.siemens.train.service;

import com.siemens.train.exception.ResourceNotFoundException;
import com.siemens.train.entities.StationBE;
import com.siemens.train.repo.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StationService {

    private final StationRepository stationRepository;

    // Spring automatically injects StationRepository
    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public List<StationBE> getAllStations() {
        return stationRepository.findAll();
    }

    public StationBE getStationById(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Station with id " + id + " not found"));
    }

    public StationBE createStation(StationBE station) {
        return stationRepository.save(station);
    }

    public StationBE updateStation(Long id, StationBE updated) {
        StationBE existing = getStationById(id);
        existing.setName(updated.getName());
        existing.setCity(updated.getCity());
        return stationRepository.save(existing);
    }

    public void deleteStation(Long id) {
        getStationById(id); // throws if not found
        stationRepository.deleteById(id);
    }

    public List<StationBE> getStationsByCity(String city) {
        return stationRepository.findByCity(city);
    }
}