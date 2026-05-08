package com.siemens.train.controller;

import com.siemens.train.api.CreateTrainRequest;
import com.siemens.train.api.TrainDTO;
import com.siemens.train.service.TrainService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trains")
public class TrainController {

    private final TrainService trainService;

    public TrainController(TrainService trainService) {
        this.trainService = trainService;
    }

    @GetMapping
    public ResponseEntity<List<TrainDTO>> getAllTrains() {
        return ResponseEntity.ok(trainService.getAllTrains());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainDTO> getTrainById(@PathVariable Long id) {
        return ResponseEntity.ok(trainService.getTrainById(id));
    }

    @GetMapping("/route/{routeId}")
    public ResponseEntity<List<TrainDTO>> getTrainsByRoute(@PathVariable Long routeId) {
        return ResponseEntity.ok(trainService.getTrainsByRoute(routeId));
    }

    @GetMapping("/delayed")
    public ResponseEntity<List<TrainDTO>> getDelayedTrains() {
        return ResponseEntity.ok(trainService.getDelayedTrains());
    }

    @PostMapping
    public ResponseEntity<TrainDTO> createTrain(@RequestBody CreateTrainRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(trainService.createTrain(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrainDTO> updateTrain(@PathVariable Long id, @RequestBody CreateTrainRequest request) {
        return ResponseEntity.ok(trainService.updateTrain(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrain(@PathVariable Long id) {
        trainService.deleteTrain(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/delay")
    public ResponseEntity<TrainDTO> markAsDelayed(@PathVariable Long id) {
        return ResponseEntity.ok(trainService.markAsDelayed(id));
    }
}