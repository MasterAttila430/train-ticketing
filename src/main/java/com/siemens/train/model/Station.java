package com.siemens.train.model;

public class Station extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String name;
    private String city;

    public Station() {
        super();
    }

    public Station(Long id, String name, String city) {
        super(id);
        this.name = name;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Station{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", city='" + city + '\''
                + '}';
    }
}