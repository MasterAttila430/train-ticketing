package com.siemens.train.api;

public class TrainDTO {
    private Long id;
    private String name;
    private int capacity;
    private boolean delayed;
    private RouteDTO route;

    public TrainDTO() {}

    public TrainDTO(Long id, String name, int capacity, boolean delayed, RouteDTO route) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.delayed = delayed;
        this.route = route;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public boolean isDelayed() { return delayed; }
    public void setDelayed(boolean delayed) { this.delayed = delayed; }
    public RouteDTO getRoute() { return route; }
    public void setRoute(RouteDTO route) { this.route = route; }
}