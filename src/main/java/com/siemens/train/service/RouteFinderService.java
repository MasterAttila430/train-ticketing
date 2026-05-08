package com.siemens.train.service;

import com.siemens.train.api.RouteSegment;
import com.siemens.train.entities.TrainStopBE;
import com.siemens.train.repo.TrainStopRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class RouteFinderService {

    private final TrainStopRepository trainStopRepository;

    public RouteFinderService(TrainStopRepository trainStopRepository) {
        this.trainStopRepository = trainStopRepository;
    }

    public List<RouteSegment> findRoute(Long fromStationId, Long toStationId, LocalDateTime departureAfter) {

        // Load all stops from database
        List<TrainStopBE> allStops = trainStopRepository.findAll();

        // Adjacency list: stationId -> list of next possible stops
        Map<Long, List<TrainStopBE>> graph = new HashMap<>();

        // Group by train and sort by stop order
        Map<Long, List<TrainStopBE>> byTrain = new HashMap<>();
        for (TrainStopBE stop : allStops) {
            byTrain.computeIfAbsent(stop.getTrain().getId(), k -> new ArrayList<>()).add(stop);
        }

        for (List<TrainStopBE> stops : byTrain.values()) {
            stops.sort(Comparator.comparingInt(TrainStopBE::getStopOrder));
            for (int i = 0; i < stops.size() - 1; i++) {
                TrainStopBE current = stops.get(i);
                TrainStopBE next = stops.get(i + 1);
                long currentStationId = current.getStation().getId();
                graph.computeIfAbsent(currentStationId, k -> new ArrayList<>()).add(next);
            }
        }

        // Dijkstra State: current station, total minutes from start, path taken
        record State(long stationId, long minutesElapsed, List<RouteSegment> path) {}

        PriorityQueue<State> pq = new PriorityQueue<>(Comparator.comparingLong(State::minutesElapsed));
        Map<Long, Long> visited = new HashMap<>();

        pq.offer(new State(fromStationId, 0, new ArrayList<>()));

        while (!pq.isEmpty()) {
            State current = pq.poll();

            // Target reached
            if (current.stationId() == toStationId) {
                return current.path();
            }

            // Skip if we already found a faster way here
            if (visited.containsKey(current.stationId()) &&
                    visited.get(current.stationId()) <= current.minutesElapsed()) {
                continue;
            }
            visited.put(current.stationId(), current.minutesElapsed());

            // Explore neighbors
            List<TrainStopBE> neighbors = graph.getOrDefault(current.stationId(), Collections.emptyList());
            for (TrainStopBE nextStop : neighbors) {
                TrainStopBE currentStop = findStop(allStops, current.stationId(), nextStop.getTrain().getId());

                // Safety check: skip if arrival or departure time is null (e.g., end of line)
                if (currentStop == null || currentStop.getDepartureTime() == null || nextStop.getArrivalTime() == null) continue;

                LocalDateTime depTime = currentStop.getDepartureTime();
                LocalDateTime arrTime = nextStop.getArrivalTime();

                // Calculate elapsed minutes relative to our desired departure time
                long depMinutes = ChronoUnit.MINUTES.between(departureAfter, depTime);
                long arrMinutes = ChronoUnit.MINUTES.between(departureAfter, arrTime);

                // Skip if train leaves before we are ready
                if (depMinutes < current.minutesElapsed()) continue;

                // 5-minute transfer rule if changing trains
                if (!current.path().isEmpty()) {
                    String lastTrain = current.path().get(current.path().size() - 1).trainName();
                    if (!lastTrain.equals(nextStop.getTrain().getName())) {
                        if (depMinutes < current.minutesElapsed() + 5) continue; // Transfer too short
                    }
                }

                List<RouteSegment> newPath = new ArrayList<>(current.path());
                newPath.add(new RouteSegment(
                        nextStop.getTrain().getName(),
                        currentStop.getStation().getName(),
                        depTime,
                        nextStop.getStation().getName(),
                        arrTime
                ));

                pq.offer(new State(nextStop.getStation().getId(), arrMinutes, newPath));
            }
        }

        return Collections.emptyList(); // No route found
    }

    // Helper: Find specific stop for a train
    private TrainStopBE findStop(List<TrainStopBE> allStops, long stationId, long trainId) {
        return allStops.stream()
                .filter(s -> s.getStation().getId() == stationId && s.getTrain().getId() == trainId)
                .findFirst()
                .orElse(null);
    }
}