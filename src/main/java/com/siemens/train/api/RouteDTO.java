package com.siemens.train.api;

import java.util.List;

public class RouteDTO {
    private Long id;
    private String name;
    private List<StationDTO> stations;

    public RouteDTO() {}

    public RouteDTO(Long id, String name, List<StationDTO> stations) {
        this.id = id;
        this.name = name;
        this.stations = stations;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<StationDTO> getStations() { return stations; }
    public void setStations(List<StationDTO> stations) { this.stations = stations; }
}