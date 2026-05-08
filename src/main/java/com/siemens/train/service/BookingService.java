package com.siemens.train.service;

import com.siemens.train.exception.BookingException;
import com.siemens.train.exception.ResourceNotFoundException;
import com.siemens.train.model.Booking;
import com.siemens.train.model.Station;
import com.siemens.train.model.Train;
import com.siemens.train.model.TrainStop;
import com.siemens.train.repo.BookingRepository;
import com.siemens.train.repo.TrainStopRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final TrainService trainService;
    private final StationService stationService;
    private final TrainStopRepository trainStopRepository;
    private final EmailService emailService;

    public BookingService(BookingRepository bookingRepository,
                          TrainService trainService,
                          StationService stationService,
                          TrainStopRepository trainStopRepository,
                          EmailService emailService) {
        this.bookingRepository = bookingRepository;
        this.trainService = trainService;
        this.stationService = stationService;
        this.trainStopRepository = trainStopRepository;
        this.emailService = emailService;
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public List<Booking> getBookingsByTrain(Long trainId) {
        trainService.getTrainById(trainId); // throws if not found
        return bookingRepository.findByTrainId(trainId);
    }

    public List<Booking> getBookingsByCustomer(String email) {
        return bookingRepository.findByCustomerEmail(email);
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Booking with id " + id + " not found"));
    }

    public Booking createBooking(Long trainId, Long departureStationId,
                                 Long arrivalStationId, String customerEmail,
                                 int numberOfSeats) {

        Train train = trainService.getTrainById(trainId);
        Station departure = stationService.getStationById(departureStationId);
        Station arrival = stationService.getStationById(arrivalStationId);

        // Check both stations are on the train's route
        List<Station> routeStations = train.getRoute().getStations();
        if (!routeStations.contains(departure)) {
            throw new BookingException(
                    "Departure station is not on this train's route");
        }
        if (!routeStations.contains(arrival)) {
            throw new BookingException(
                    "Arrival station is not on this train's route");
        }

        // Check travel direction is valid (departure comes before arrival)
        int departureOrder = getStopOrder(trainId, departureStationId);
        int arrivalOrder = getStopOrder(trainId, arrivalStationId);
        if (departureOrder >= arrivalOrder) {
            throw new BookingException(
                    "Departure station must come before arrival station");
        }

        // Check for overbooking
        int bookedSeats = bookingRepository.getTotalBookedSeats(trainId);
        if (bookedSeats + numberOfSeats > train.getCapacity()) {
            throw new BookingException(
                    "Not enough seats available. Available: "
                            + (train.getCapacity() - bookedSeats));
        }

        // All checks passed - create the booking
        Booking booking = new Booking(
                null, train, departure, arrival,
                customerEmail, numberOfSeats, LocalDateTime.now()
        );
        Booking saved = bookingRepository.save(booking);

        // Send confirmation email
        emailService.sendBookingConfirmation(customerEmail, train.getName(),
                departure.getName(), arrival.getName(), numberOfSeats);

        return saved;
    }

    public void deleteBooking(Long id) {
        getBookingById(id); // throws if not found
        bookingRepository.deleteById(id);
    }

    // Helper: get stop order for a station on a specific train
    private int getStopOrder(Long trainId, Long stationId) {
        return trainStopRepository.findByTrainIdOrderByStopOrder(trainId)
                .stream()
                .filter(stop -> stop.getStation().getId().equals(stationId))
                .findFirst()
                .map(TrainStop::getStopOrder)
                .orElseThrow(() -> new BookingException(
                        "Station not found in train schedule"));
    }
}