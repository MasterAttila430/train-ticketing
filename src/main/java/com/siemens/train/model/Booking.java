package com.siemens.train.model;

import java.time.LocalDateTime;

// Represents a ticket booking made by a customer for a specific train
public class Booking extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Train train;
    private String customerEmail;
    private int numberOfSeats;
    private LocalDateTime bookingDate;

    public Booking() {
        super();
    }

    public Booking(Long id, Train train, String customerEmail,
                   int numberOfSeats, LocalDateTime bookingDate) {
        super(id);
        this.train = train;
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
                + ", customerEmail='" + customerEmail + '\''
                + ", numberOfSeats=" + numberOfSeats
                + ", bookingDate=" + bookingDate
                + '}';
    }
}