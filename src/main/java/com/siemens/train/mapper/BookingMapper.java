package com.siemens.train.mapper;

import com.siemens.train.api.BookingDTO;
import com.siemens.train.entities.BookingBE;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

    private final TrainMapper trainMapper;
    private final StationMapper stationMapper;

    public BookingMapper(TrainMapper trainMapper, StationMapper stationMapper) {
        this.trainMapper = trainMapper;
        this.stationMapper = stationMapper;
    }

    public BookingDTO toDto(BookingBE entity) {
        if (entity == null) return null;
        return new BookingDTO(
                entity.getId(),
                trainMapper.toDto(entity.getTrain()),
                stationMapper.toDto(entity.getDepartureStation()),
                stationMapper.toDto(entity.getArrivalStation()),
                entity.getCustomerEmail(),
                entity.getNumberOfSeats(),
                entity.getBookingDate()
        );
    }
}