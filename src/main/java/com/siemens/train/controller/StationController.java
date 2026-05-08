package com.siemens.train.controller;

import com.siemens.train.model.Station;
import com.siemens.train.service.StationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stations")
public class StationController {

    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    // Get all stations
    @GetMapping
    public ResponseEntity<List<Station>> getAllStations() {
        return ResponseEntity.ok(stationService.getAllStations());
    }

    // Get one station by id
    @GetMapping("/{id}")
    public ResponseEntity<Station> getStationById(@PathVariable Long id) {
        return ResponseEntity.ok(stationService.getStationById(id));
    }

    // Get stations by city
    @GetMapping("/city")
    public ResponseEntity<List<Station>> getStationsByCity(@RequestParam String city) {
        return ResponseEntity.ok(stationService.getStationsByCity(city));
    }

    // Create a new station (admin)
    @PostMapping
    public ResponseEntity<Station> createStation(@RequestBody Station station) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(stationService.createStation(station));
    }

    // Update a station (admin)
    @PutMapping("/{id}")
    public ResponseEntity<Station> updateStation(
            @PathVariable Long id,
            @RequestBody Station station) {
        return ResponseEntity.ok(stationService.updateStation(id, station));
    }

    // Delete a station (admin)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        stationService.deleteStation(id);
        return ResponseEntity.noContent().build();
    }
}