package com.siemens.train.controller;

import com.siemens.train.model.Route;
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

    // Get all routes
    @GetMapping
    public ResponseEntity<List<Route>> getAllRoutes() {
        return ResponseEntity.ok(routeService.getAllRoutes());
    }

    // Get one route by id
    @GetMapping("/{id}")
    public ResponseEntity<Route> getRouteById(@PathVariable Long id) {
        return ResponseEntity.ok(routeService.getRouteById(id));
    }

    // Create a new route (admin)
    @PostMapping
    public ResponseEntity<Route> createRoute(@RequestBody Route route) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(routeService.createRoute(route));
    }

    // Update a route (admin)
    @PutMapping("/{id}")
    public ResponseEntity<Route> updateRoute(
            @PathVariable Long id,
            @RequestBody Route route) {
        return ResponseEntity.ok(routeService.updateRoute(id, route));
    }

    // Delete a route (admin)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoute(@PathVariable Long id) {
        routeService.deleteRoute(id);
        return ResponseEntity.noContent().build();
    }

    // Add a station to a route (admin)
    @PostMapping("/{routeId}/stations/{stationId}")
    public ResponseEntity<Route> addStation(
            @PathVariable Long routeId,
            @PathVariable Long stationId) {
        return ResponseEntity.ok(routeService.addStationToRoute(routeId, stationId));
    }

    // Remove a station from a route (admin)
    @DeleteMapping("/{routeId}/stations/{stationId}")
    public ResponseEntity<Route> removeStation(
            @PathVariable Long routeId,
            @PathVariable Long stationId) {
        return ResponseEntity.ok(routeService.removeStationFromRoute(routeId, stationId));
    }

    // Find route between two stations — supports transfers
    // Example: GET /api/routes/find?from=1&to=5&after=2026-05-10T08:00:00
    @GetMapping("/find")
    public ResponseEntity<List<RouteFinderService.RouteSegment>> findRoute(
            @RequestParam Long from,
            @RequestParam Long to,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime after) {
        List<RouteFinderService.RouteSegment> result = routeFinderService.findRoute(from, to, after);
        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }
}