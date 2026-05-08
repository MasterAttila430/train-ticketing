package com.siemens.train.api;

import java.time.LocalDateTime;

public record RouteSegment(
        String trainName,
        String fromStation,
        LocalDateTime departure,
        String toStation,
        LocalDateTime arrival
) {}