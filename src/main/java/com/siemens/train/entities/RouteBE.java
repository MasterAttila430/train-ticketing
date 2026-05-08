package com.siemens.train.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "routes")
public class RouteBE extends BaseEntity {

    @Column(nullable = false)
    private String name;

    // One route has many stations, ordered by position
    @ManyToMany
    @JoinTable(
            name = "route_stations",
            joinColumns = @JoinColumn(name = "route_id"),
            inverseJoinColumns = @JoinColumn(name = "station_id")
    )
    @OrderColumn(name = "stop_order")
    private List<StationBE> stations = new ArrayList<>();

    public RouteBE() {
        super();
    }

    public RouteBE(Long id, String name, List<StationBE> stations) {
        super(id);
        this.name = name;
        this.stations = stations;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<StationBE> getStations() { return stations; }
    public void setStations(List<StationBE> stations) { this.stations = stations; }

    @Override
    public String toString() {
        return "Route{id=" + id + ", name='" + name + "', stations=" + stations + "}";
    }
}