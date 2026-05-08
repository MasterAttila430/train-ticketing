package com.siemens.train.service;

import com.siemens.train.api.StationDTO;
import com.siemens.train.entities.StationBE;
import com.siemens.train.exception.ResourceNotFoundException;
import com.siemens.train.mapper.StationMapper;
import com.siemens.train.repo.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationService {

    private final StationRepository stationRepository;
    private final StationMapper stationMapper;

    public StationService(StationRepository stationRepository, StationMapper stationMapper) {
        this.stationRepository = stationRepository;
        this.stationMapper = stationMapper;
    }

    public List<StationDTO> getAllStations() {
        return stationRepository.findAll().stream()
                .map(stationMapper::toDto)
                .collect(Collectors.toList());
    }

    public StationDTO getStationById(Long id) {
        StationBE station = stationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Station with id " + id + " not found"));
        return stationMapper.toDto(station);
    }

    public StationDTO createStation(StationDTO stationDTO) {
        StationBE entity = new StationBE(null, stationDTO.getName(), stationDTO.getCity());
        StationBE saved = stationRepository.save(entity);
        return stationMapper.toDto(saved);
    }

    public StationDTO updateStation(Long id, StationDTO updated) {
        StationBE existing = stationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Station with id " + id + " not found"));

        // Update properties
        existing.setName(updated.getName());
        existing.setCity(updated.getCity());

        StationBE saved = stationRepository.save(existing);
        return stationMapper.toDto(saved);
    }

    public void deleteStation(Long id) {
        if (!stationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Station with id " + id + " not found");
        }
        stationRepository.deleteById(id);
    }

    public List<StationDTO> getStationsByCity(String city) {
        return stationRepository.findByCity(city).stream()
                .map(stationMapper::toDto)
                .collect(Collectors.toList());
    }

    // Helper for internal service-to-service communication
    public StationBE getStationEntityById(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Station with id " + id + " not found"));
    }
}