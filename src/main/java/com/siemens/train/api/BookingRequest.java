package com.siemens.train.api;

public record BookingRequest(
        Long trainId,
        Long departureStationId,
        Long arrivalStationId,
        String customerEmail,
        int numberOfSeats
) {}