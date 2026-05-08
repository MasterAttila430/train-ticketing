package com.siemens.train.service;

import com.siemens.train.api.RouteDTO;
import com.siemens.train.api.UpdateRouteRequest;
import com.siemens.train.entities.RouteBE;
import com.siemens.train.entities.StationBE;
import com.siemens.train.exception.BookingException;
import com.siemens.train.exception.ResourceNotFoundException;
import com.siemens.train.mapper.RouteMapper;
import com.siemens.train.repo.RouteRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RouteService {

    private final RouteRepository routeRepository;
    private final StationService stationService;
    private final RouteMapper routeMapper;

    public RouteService(RouteRepository routeRepository, StationService stationService, RouteMapper routeMapper) {
        this.routeRepository = routeRepository;
        this.stationService = stationService;
        this.routeMapper = routeMapper;
    }

    public List<RouteDTO> getAllRoutes() {
        return routeRepository.findAll().stream()
                .map(routeMapper::toDto)
                .collect(Collectors.toList());
    }

    public RouteDTO getRouteById(Long id) {
        return routeMapper.toDto(getRouteEntityById(id));
    }

    public RouteDTO createRoute(RouteDTO routeDTO) {
        RouteBE route = new RouteBE(null, routeDTO.getName(), new ArrayList<>());
        return routeMapper.toDto(routeRepository.save(route));
    }

    public RouteDTO updateRoute(Long id, UpdateRouteRequest request) {
        RouteBE existing = getRouteEntityById(id);

        // Update name
        existing.setName(request.name());

        // Update stations list based on the provided IDs
        List<StationBE> newStations = new ArrayList<>();
        if (request.stationIds() != null) {
            for (Long stationId : request.stationIds()) {
                newStations.add(stationService.getStationEntityById(stationId));
            }
        }
        existing.setStations(newStations);

        return routeMapper.toDto(routeRepository.save(existing));
    }

    public void deleteRoute(Long id) {
        if (!routeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Route with id " + id + " not found");
        }
        routeRepository.deleteById(id);
    }

    public RouteDTO addStationToRoute(Long routeId, Long stationId) {
        RouteBE route = getRouteEntityById(routeId);
        StationBE station = stationService.getStationEntityById(stationId);

        if (route.getStations().contains(station)) {
            throw new BookingException("Station is already on this route");
        }

        route.getStations().add(station);
        return routeMapper.toDto(routeRepository.save(route));
    }

    public RouteDTO removeStationFromRoute(Long routeId, Long stationId) {
        RouteBE route = getRouteEntityById(routeId);
        StationBE station = stationService.getStationEntityById(stationId);

        route.getStations().remove(station);
        return routeMapper.toDto(routeRepository.save(route));
    }

    // Helper for internal service-to-service communication
    public RouteBE getRouteEntityById(Long id) {
        return routeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Route with id " + id + " not found"));
    }
}