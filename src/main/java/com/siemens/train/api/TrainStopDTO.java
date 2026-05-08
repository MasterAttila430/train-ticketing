package com.siemens.train.api;

import java.time.LocalDateTime;

public class TrainStopDTO {
    private Long id;
    private TrainDTO train;
    private StationDTO station;
    private LocalDateTime arrivalTime;
    private LocalDateTime departureTime;
    private int stopOrder;

    public TrainStopDTO() {}

    public TrainStopDTO(Long id, TrainDTO train, StationDTO station, LocalDateTime arrivalTime, LocalDateTime departureTime, int stopOrder) {
        this.id = id;
        this.train = train;
        this.station = station;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.stopOrder = stopOrder;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public TrainDTO getTrain() { return train; }
    public void setTrain(TrainDTO train) { this.train = train; }
    public StationDTO getStation() { return station; }
    public void setStation(StationDTO station) { this.station = station; }
    public LocalDateTime getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(LocalDateTime arrivalTime) { this.arrivalTime = arrivalTime; }
    public LocalDateTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(LocalDateTime departureTime) { this.departureTime = departureTime; }
    public int getStopOrder() { return stopOrder; }
    public void setStopOrder(int stopOrder) { this.stopOrder = stopOrder; }
}