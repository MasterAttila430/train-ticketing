package com.siemens.train.model;

import java.time.LocalDateTime;

public class Booking extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Train train;
    private Station departureStation;
    private Station arrivalStation;
    private String customerEmail;
    private int numberOfSeats;
    private LocalDateTime bookingDate;

    public Booking() {
        super();
    }

    public Booking(Long id, Train train, Station departureStation, Station arrivalStation,
                   String customerEmail, int numberOfSeats, LocalDateTime bookingDate) {
        super(id);
        this.train = train;
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
        this.customerEmail = customerEmail;
        this.numberOfSeats = numberOfSeats;
        this.bookingDate = bookingDate;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public Station getDepartureStation() {
        return departureStation;
    }

    public void setDepartureStation(Station departureStation) {
        this.departureStation = departureStation;
    }

    public Station getArrivalStation() {
        return arrivalStation;
    }

    public void setArrivalStation(Station arrivalStation) {
        this.arrivalStation = arrivalStation;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    @Override
    public String toString() {
        return "Booking{"
                + "id=" + id
                + ", train=" + train.getName()
                + ", departureStation=" + departureStation.getName()
                + ", arrivalStation=" + arrivalStation.getName()
                + ", customerEmail='" + customerEmail + '\''
                + ", numberOfSeats=" + numberOfSeats
                + ", bookingDate=" + bookingDate
                + '}';
    }
}