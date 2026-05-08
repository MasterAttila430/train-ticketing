package com.siemens.train.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "stations")
public class StationBE extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String city;

    public StationBE() {
        super();
    }

    public StationBE(Long id, String name, String city) {
        super(id);
        this.name = name;
        this.city = city;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    @Override
    public String toString() {
        return "Station{id=" + id + ", name='" + name + "', city='" + city + "'}";
    }
}