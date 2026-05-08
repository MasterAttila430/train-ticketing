package com.siemens.train.controller;

import com.siemens.train.api.StationDTO;
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

    @GetMapping
    public ResponseEntity<List<StationDTO>> getAllStations() {
        return ResponseEntity.ok(stationService.getAllStations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StationDTO> getStationById(@PathVariable Long id) {
        return ResponseEntity.ok(stationService.getStationById(id));
    }

    @GetMapping("/city")
    public ResponseEntity<List<StationDTO>> getStationsByCity(@RequestParam String city) {
        return ResponseEntity.ok(stationService.getStationsByCity(city));
    }

    @PostMapping
    public ResponseEntity<StationDTO> createStation(@RequestBody StationDTO station) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(stationService.createStation(station));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StationDTO> updateStation(
            @PathVariable Long id,
            @RequestBody StationDTO station) {
        return ResponseEntity.ok(stationService.updateStation(id, station));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        stationService.deleteStation(id);
        return ResponseEntity.noContent().build();
    }
}