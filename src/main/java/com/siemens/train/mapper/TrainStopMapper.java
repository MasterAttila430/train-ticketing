package com.siemens.train.mapper;

import com.siemens.train.api.TrainStopDTO;
import com.siemens.train.entities.TrainStopBE;
import org.springframework.stereotype.Component;

@Component
public class TrainStopMapper {

    private final TrainMapper trainMapper;
    private final StationMapper stationMapper;

    public TrainStopMapper(TrainMapper trainMapper, StationMapper stationMapper) {
        this.trainMapper = trainMapper;
        this.stationMapper = stationMapper;
    }

    public TrainStopDTO toDto(TrainStopBE entity) {
        if (entity == null) return null;
        return new TrainStopDTO(
                entity.getId(),
                trainMapper.toDto(entity.getTrain()),
                stationMapper.toDto(entity.getStation()),
                entity.getArrivalTime(),
                entity.getDepartureTime(),
                entity.getStopOrder()
        );
    }
}