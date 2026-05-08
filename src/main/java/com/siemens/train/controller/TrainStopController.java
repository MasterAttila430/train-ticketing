package com.siemens.train.controller;

import com.siemens.train.entities.TrainStopBE;
import com.siemens.train.service.TrainStopService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trainstops")
public class TrainStopController {

    private final TrainStopService trainStopService;

    public TrainStopController(TrainStopService trainStopService) {
        this.trainStopService = trainStopService;
    }

    // Get all stops for a train, ordered by stop order
    @GetMapping("/train/{trainId}")
    public ResponseEntity<List<TrainStopBE>> getStopsForTrain(@PathVariable Long trainId) {
        return ResponseEntity.ok(trainStopService.getStopsForTrain(trainId));
    }

    // Get one stop by id
    @GetMapping("/{id}")
    public ResponseEntity<TrainStopBE> getTrainStopById(@PathVariable Long id) {
        return ResponseEntity.ok(trainStopService.getTrainStopById(id));
    }

    // Create a new stop (admin)
    @PostMapping
    public ResponseEntity<TrainStopBE> createTrainStop(@RequestBody TrainStopBE trainStop) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(trainStopService.createTrainStop(trainStop));
    }

    // Update a stop's times or order (admin)
    @PutMapping("/{id}")
    public ResponseEntity<TrainStopBE> updateTrainStop(
            @PathVariable Long id,
            @RequestBody TrainStopBE trainStop) {
        return ResponseEntity.ok(trainStopService.updateTrainStop(id, trainStop));
    }

    // Delete a stop (admin)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrainStop(@PathVariable Long id) {
        trainStopService.deleteTrainStop(id);
        return ResponseEntity.noContent().build();
    }
}