package com.siemens.train.api;

import java.time.LocalDateTime;

public record CreateTrainStopRequest(Long trainId, Long stationId, LocalDateTime arrivalTime, LocalDateTime departureTime, int stopOrder) {}