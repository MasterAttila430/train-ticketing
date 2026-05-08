package com.siemens.train.controller;

import com.siemens.train.api.CreateTrainStopRequest;
import com.siemens.train.api.TrainStopDTO;
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

    @GetMapping("/train/{trainId}")
    public ResponseEntity<List<TrainStopDTO>> getStopsForTrain(@PathVariable Long trainId) {
        return ResponseEntity.ok(trainStopService.getStopsForTrain(trainId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainStopDTO> getTrainStopById(@PathVariable Long id) {
        return ResponseEntity.ok(trainStopService.getTrainStopById(id));
    }

    @PostMapping
    public ResponseEntity<TrainStopDTO> createTrainStop(@RequestBody CreateTrainStopRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(trainStopService.createTrainStop(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrainStopDTO> updateTrainStop(@PathVariable Long id, @RequestBody CreateTrainStopRequest request) {
        return ResponseEntity.ok(trainStopService.updateTrainStop(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrainStop(@PathVariable Long id) {
        trainStopService.deleteTrainStop(id);
        return ResponseEntity.noContent().build();
    }
}