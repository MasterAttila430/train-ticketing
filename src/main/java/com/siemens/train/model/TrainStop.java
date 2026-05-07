package com.siemens.train.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "train_stops")
public class TrainStop extends BaseEntity {

    // Many stops can belong to one train
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "train_id", nullable = false)
    private Train train;

    // Many stops can reference the same station
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    @Column(name = "arrival_time")
    private LocalDateTime arrivalTime;

    @Column(name = "departure_time")
    private LocalDateTime departureTime;

    @Column(name = "stop_order", nullable = false)
    private int stopOrder;

    public TrainStop() {
        super();
    }

    public TrainStop(Long id, Train train, Station station,
                     LocalDateTime arrivalTime, LocalDateTime departureTime, int stopOrder) {
        super(id);
        this.train = train;
        this.station = station;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.stopOrder = stopOrder;
    }

    public Train getTrain() { return train; }
    public void setTrain(Train train) { this.train = train; }

    public Station getStation() { return station; }
    public void setStation(Station station) { this.station = station; }

    public LocalDateTime getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(LocalDateTime arrivalTime) { this.arrivalTime = arrivalTime; }

    public LocalDateTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(LocalDateTime departureTime) { this.departureTime = departureTime; }

    public int getStopOrder() { return stopOrder; }
    public void setStopOrder(int stopOrder) { this.stopOrder = stopOrder; }

    @Override
    public String toString() {
        return "TrainStop{id=" + id + ", train=" + train.getName()
                + ", station=" + station.getName()
                + ", arrival=" + arrivalTime
                + ", departure=" + departureTime
                + ", stopOrder=" + stopOrder + "}";
    }
}