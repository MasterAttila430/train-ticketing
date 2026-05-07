package com.siemens.train.model;

import java.time.LocalDateTime;

// Represents a train operating on a specific route
public class Train extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String name;
    private int capacity;
    private LocalDateTime departureTime;
    private boolean delayed;
    private Route route;

    public Train() {
        super();
    }

    public Train(Long id, String name, int capacity,
                 LocalDateTime departureTime, boolean delayed, Route route) {
        super(id);
        this.name = name;
        this.capacity = capacity;
        this.departureTime = departureTime;
        this.delayed = delayed;
        this.route = route;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public boolean isDelayed() {
        return delayed;
    }

    public void setDelayed(boolean delayed) {
        this.delayed = delayed;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    @Override
    public String toString() {
        return "Train{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", capacity=" + capacity
                + ", departureTime=" + departureTime
                + ", delayed=" + delayed
                + ", route=" + route.getName()
                + '}';
    }
}