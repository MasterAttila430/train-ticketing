package com.siemens.train.mapper;

import com.siemens.train.api.RouteDTO;
import com.siemens.train.entities.RouteBE;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;
import java.util.Objects;

@Component
public class RouteMapper {

    private final StationMapper stationMapper;

    public RouteMapper(StationMapper stationMapper) {
        this.stationMapper = stationMapper;
    }

    public RouteDTO toDto(RouteBE entity) {
        if (entity == null) return null;
        return new RouteDTO(
                entity.getId(),
                entity.getName(),
                entity.getStations().stream()
                        .filter(Objects::nonNull)
                        .map(stationMapper::toDto)
                        .collect(Collectors.toList())
        );
    }
}