package com.siemens.train.api;

public record CreateTrainRequest(String name, int capacity, Long routeId) {}