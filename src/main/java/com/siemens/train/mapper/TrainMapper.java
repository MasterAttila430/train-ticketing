package com.siemens.train.mapper;

import com.siemens.train.api.TrainDTO;
import com.siemens.train.entities.TrainBE;
import org.springframework.stereotype.Component;

@Component
public class TrainMapper {

    private final RouteMapper routeMapper;

    public TrainMapper(RouteMapper routeMapper) {
        this.routeMapper = routeMapper;
    }

    public TrainDTO toDto(TrainBE entity) {
        if (entity == null) return null;
        return new TrainDTO(
                entity.getId(),
                entity.getName(),
                entity.getCapacity(),
                entity.isDelayed(),
                routeMapper.toDto(entity.getRoute())
        );
    }
}