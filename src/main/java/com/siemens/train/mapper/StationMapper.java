package com.siemens.train.mapper;

import com.siemens.train.api.StationDTO;
import com.siemens.train.entities.StationBE;
import org.springframework.stereotype.Component;

@Component
public class StationMapper {

    public StationDTO toDto(StationBE entity) {
        if (entity == null) return null;
        return new StationDTO(
                entity.getId(),
                entity.getName(),
                entity.getCity()
        );
    }
}