package com.siemens.train.service;

import com.siemens.train.exception.ResourceNotFoundException;
import com.siemens.train.entities.RouteBE;
import com.siemens.train.entities.StationBE;
import com.siemens.train.repo.RouteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteService {

    private final RouteRepository routeRepository;
    private final StationService stationService;

    public RouteService(RouteRepository routeRepository, StationService stationService) {
        this.routeRepository = routeRepository;
        this.stationService = stationService;
    }

    public List<RouteBE> getAllRoutes() {
        return routeRepository.findAll();
    }

    public RouteBE getRouteById(Long id) {
        return routeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Route with id " + id + " not found"));
    }

    public RouteBE createRoute(RouteBE route) {
        return routeRepository.save(route);
    }

    public RouteBE updateRoute(Long id, RouteBE updated) {
        RouteBE existing = getRouteById(id);
        existing.setName(updated.getName());
        existing.setStations(updated.getStations());
        return routeRepository.save(existing);
    }

    public void deleteRoute(Long id) {
        getRouteById(id); // throws if not found
        routeRepository.deleteById(id);
    }

    // Add a station to the end of a route
    public RouteBE addStationToRoute(Long routeId, Long stationId) {
        RouteBE route = getRouteById(routeId);
        StationBE station = stationService.getStationById(stationId);

        if (route.getStations().contains(station)) {
            throw new com.siemens.train.exception.BookingException(
                    "Station is already on this route");
        }

        route.getStations().add(station);
        return routeRepository.save(route);
    }

    // Remove a station from a route
    public RouteBE removeStationFromRoute(Long routeId, Long stationId) {
        RouteBE route = getRouteById(routeId);
        StationBE station = stationService.getStationById(stationId);
        route.getStations().remove(station);
        return routeRepository.save(route);
    }
}