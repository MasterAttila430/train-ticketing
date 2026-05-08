package com.siemens.train.api;

import java.util.List;

public record UpdateRouteRequest(String name, List<Long> stationIds) {}