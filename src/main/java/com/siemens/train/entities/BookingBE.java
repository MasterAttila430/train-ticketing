package com.siemens.train.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class BookingBE extends BaseEntity {

    // Many bookings can be for the same train
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "train_id", nullable = false)
    private TrainBE train;

    // The station where the passenger boards
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "departure_station_id", nullable = false)
    private StationBE departureStation;

    // The station where the passenger exits
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "arrival_station_id", nullable = false)
    private StationBE arrivalStation;

    @Column(nullable = false)
    private String customerEmail;

    @Column(nullable = false)
    private int numberOfSeats;

    @Column(nullable = false)
    private LocalDateTime bookingDate;

    public BookingBE() {
        super();
    }

    public BookingBE(Long id, TrainBE train, StationBE departureStation, StationBE arrivalStation,
                     String customerEmail, int numberOfSeats, LocalDateTime bookingDate) {
        super(id);
        this.train = train;
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
        this.customerEmail = customerEmail;
        this.numberOfSeats = numberOfSeats;
        this.bookingDate = bookingDate;
    }

    public TrainBE getTrain() { return train; }
    public void setTrain(TrainBE train) { this.train = train; }

    public StationBE getDepartureStation() { return departureStation; }
    public void setDepartureStation(StationBE departureStation) { this.departureStation = departureStation; }

    public StationBE getArrivalStation() { return arrivalStation; }
    public void setArrivalStation(StationBE arrivalStation) { this.arrivalStation = arrivalStation; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public int getNumberOfSeats() { return numberOfSeats; }
    public void setNumberOfSeats(int numberOfSeats) { this.numberOfSeats = numberOfSeats; }

    public LocalDateTime getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDateTime bookingDate) { this.bookingDate = bookingDate; }

    @Override
    public String toString() {
        return "Booking{id=" + id
                + ", train=" + train.getName()
                + ", from=" + departureStation.getName()
                + ", to=" + arrivalStation.getName()
                + ", email='" + customerEmail + "'"
                + ", seats=" + numberOfSeats
                + ", date=" + bookingDate + "}";
    }
}