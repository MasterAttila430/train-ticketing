package com.siemens.train.controller;

import com.siemens.train.model.Train;
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

    // Get all trains
    @GetMapping
    public ResponseEntity<List<Train>> getAllTrains() {
        return ResponseEntity.ok(trainService.getAllTrains());
    }

    // Get one train by id
    @GetMapping("/{id}")
    public ResponseEntity<Train> getTrainById(@PathVariable Long id) {
        return ResponseEntity.ok(trainService.getTrainById(id));
    }

    // Get all trains on a specific route
    @GetMapping("/route/{routeId}")
    public ResponseEntity<List<Train>> getTrainsByRoute(@PathVariable Long routeId) {
        return ResponseEntity.ok(trainService.getTrainsByRoute(routeId));
    }

    // Get all currently delayed trains
    @GetMapping("/delayed")
    public ResponseEntity<List<Train>> getDelayedTrains() {
        return ResponseEntity.ok(trainService.getDelayedTrains());
    }

    // Create a new train (admin)
    @PostMapping
    public ResponseEntity<Train> createTrain(@RequestBody Train train) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(trainService.createTrain(train));
    }

    // Update an existing train (admin)
    @PutMapping("/{id}")
    public ResponseEntity<Train> updateTrain(
            @PathVariable Long id,
            @RequestBody Train train) {
        return ResponseEntity.ok(trainService.updateTrain(id, train));
    }

    // Delete a train (admin)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrain(@PathVariable Long id) {
        trainService.deleteTrain(id);
        return ResponseEntity.noContent().build();
    }

    // Mark train as delayed — notifies all passengers via email automatically
    @PostMapping("/{id}/delay")
    public ResponseEntity<Train> markAsDelayed(@PathVariable Long id) {
        return ResponseEntity.ok(trainService.markAsDelayed(id));
    }
}