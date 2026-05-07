package com.siemens.train.model;

import java.time.LocalDateTime;

public class TrainStop extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Train train;
    private Station station;
    private LocalDateTime arrivalTime;
    private LocalDateTime departureTime;
    private int stopOrder;

    public TrainStop() {
        super();
    }

    public TrainStop(Long id, Train train, Station station, LocalDateTime arrivalTime,
                     LocalDateTime departureTime, int stopOrder) {
        super(id);
        this.train = train;
        this.station = station;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.stopOrder = stopOrder;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public int getStopOrder() {
        return stopOrder;
    }

    public void setStopOrder(int stopOrder) {
        this.stopOrder = stopOrder;
    }

    @Override
    public String toString() {
        return "TrainStop{"
                + "id=" + id
                + ", train=" + train.getName()
                + ", station=" + station.getName()
                + ", arrivalTime=" + arrivalTime
                + ", departureTime=" + departureTime
                + ", stopOrder=" + stopOrder
                + '}';
    }
}