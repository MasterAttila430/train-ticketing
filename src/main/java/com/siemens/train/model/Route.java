package com.siemens.train.model;

import java.util.ArrayList;
import java.util.List;

// Represents a train route consisting of ordered stations
public class Route extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String name;
    private List<Station> stations;

    public Route() {
        super();
        this.stations = new ArrayList<>();
    }

    public Route(Long id, String name, List<Station> stations) {
        super(id);
        this.name = name;
        this.stations = stations;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Station> getStations() {
        return stations;
    }

    public void setStations(List<Station> stations) {
        this.stations = stations;
    }

    @Override
    public String toString() {
        return "Route{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", stations=" + stations
                + '}';
    }
}