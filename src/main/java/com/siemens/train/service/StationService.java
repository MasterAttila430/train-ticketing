package com.siemens.train.service;

import com.siemens.train.exception.ResourceNotFoundException;
import com.siemens.train.model.Station;
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

    public List<Station> getAllStations() {
        return stationRepository.findAll();
    }

    public Station getStationById(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Station with id " + id + " not found"));
    }

    public Station createStation(Station station) {
        return stationRepository.save(station);
    }

    public Station updateStation(Long id, Station updated) {
        Station existing = getStationById(id);
        existing.setName(updated.getName());
        existing.setCity(updated.getCity());
        return stationRepository.save(existing);
    }

    public void deleteStation(Long id) {
        getStationById(id); // throws if not found
        stationRepository.deleteById(id);
    }

    public List<Station> getStationsByCity(String city) {
        return stationRepository.findByCity(city);
    }
}