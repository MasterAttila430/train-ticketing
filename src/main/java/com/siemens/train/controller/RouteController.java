package com.siemens.train.controller;

import com.siemens.train.api.RouteDTO;
import com.siemens.train.api.RouteSegment;
import com.siemens.train.api.UpdateRouteRequest;
import com.siemens.train.service.RouteFinderService;
import com.siemens.train.service.RouteService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    private final RouteService routeService;
    private final RouteFinderService routeFinderService;

    public RouteController(RouteService routeService, RouteFinderService routeFinderService) {
        this.routeService = routeService;
        this.routeFinderService = routeFinderService;
    }

    @GetMapping
    public ResponseEntity<List<RouteDTO>> getAllRoutes() {
        return ResponseEntity.ok(routeService.getAllRoutes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RouteDTO> getRouteById(@PathVariable Long id) {
        return ResponseEntity.ok(routeService.getRouteById(id));
    }

    @PostMapping
    public ResponseEntity<RouteDTO> createRoute(@RequestBody RouteDTO route) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(routeService.createRoute(route));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RouteDTO> updateRoute(
            @PathVariable Long id,
            @RequestBody UpdateRouteRequest request) {
        return ResponseEntity.ok(routeService.updateRoute(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoute(@PathVariable Long id) {
        routeService.deleteRoute(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{routeId}/stations/{stationId}")
    public ResponseEntity<RouteDTO> addStation(
            @PathVariable Long routeId,
            @PathVariable Long stationId) {
        return ResponseEntity.ok(routeService.addStationToRoute(routeId, stationId));
    }

    @DeleteMapping("/{routeId}/stations/{stationId}")
    public ResponseEntity<RouteDTO> removeStation(
            @PathVariable Long routeId,
            @PathVariable Long stationId) {
        return ResponseEntity.ok(routeService.removeStationFromRoute(routeId, stationId));
    }

    @GetMapping("/find")
    public ResponseEntity<List<RouteSegment>> findRoute(
            @RequestParam Long from,
            @RequestParam Long to,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime after) {
        List<RouteSegment> result = routeFinderService.findRoute(from, to, after);
        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }
}