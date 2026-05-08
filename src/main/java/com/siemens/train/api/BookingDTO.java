package com.siemens.train.api;

import java.time.LocalDateTime;

public class BookingDTO {
    private Long id;
    private TrainDTO train;
    private StationDTO departureStation;
    private StationDTO arrivalStation;
    private String customerEmail;
    private int numberOfSeats;
    private LocalDateTime bookingDate;

    public BookingDTO() {}

    public BookingDTO(Long id, TrainDTO train, StationDTO departureStation, StationDTO arrivalStation, String customerEmail, int numberOfSeats, LocalDateTime bookingDate) {
        this.id = id;
        this.train = train;
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
        this.customerEmail = customerEmail;
        this.numberOfSeats = numberOfSeats;
        this.bookingDate = bookingDate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public TrainDTO getTrain() { return train; }
    public void setTrain(TrainDTO train) { this.train = train; }
    public StationDTO getDepartureStation() { return departureStation; }
    public void setDepartureStation(StationDTO departureStation) { this.departureStation = departureStation; }
    public StationDTO getArrivalStation() { return arrivalStation; }
    public void setArrivalStation(StationDTO arrivalStation) { this.arrivalStation = arrivalStation; }
    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    public int getNumberOfSeats() { return numberOfSeats; }
    public void setNumberOfSeats(int numberOfSeats) { this.numberOfSeats = numberOfSeats; }
    public LocalDateTime getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDateTime bookingDate) { this.bookingDate = bookingDate; }
}