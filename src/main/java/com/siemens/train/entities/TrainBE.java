package com.siemens.train.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "trains")
public class TrainBE extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int capacity;

    @Column(name = "is_delayed", nullable = false)
    private boolean delayed = false;

    // Many trains can use the same route
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "route_id", nullable = false)
    private RouteBE route;

    public TrainBE() {
        super();
    }

    public TrainBE(Long id, String name, int capacity, boolean delayed, RouteBE route) {
        super(id);
        this.name = name;
        this.capacity = capacity;
        this.delayed = delayed;
        this.route = route;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public boolean isDelayed() { return delayed; }
    public void setDelayed(boolean delayed) { this.delayed = delayed; }

    public RouteBE getRoute() { return route; }
    public void setRoute(RouteBE route) { this.route = route; }

    @Override
    public String toString() {
        return "Train{id=" + id + ", name='" + name + "', capacity=" + capacity
                + ", delayed=" + delayed + ", route=" + route.getName() + "}";
    }
}