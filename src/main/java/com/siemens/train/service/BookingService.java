package com.siemens.train.service;

import com.siemens.train.api.BookingDTO;
import com.siemens.train.api.BookingRequest;
import com.siemens.train.entities.BookingBE;
import com.siemens.train.entities.StationBE;
import com.siemens.train.entities.TrainBE;
import com.siemens.train.entities.TrainStopBE;
import com.siemens.train.exception.BookingException;
import com.siemens.train.exception.ResourceNotFoundException;
import com.siemens.train.mapper.BookingMapper;
import com.siemens.train.repo.BookingRepository;
import com.siemens.train.repo.TrainStopRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final TrainService trainService;
    private final StationService stationService;
    private final TrainStopRepository trainStopRepository;
    private final EmailService emailService;
    private final BookingMapper bookingMapper;

    public BookingService(BookingRepository bookingRepository,
                          TrainService trainService,
                          StationService stationService,
                          TrainStopRepository trainStopRepository,
                          EmailService emailService,
                          BookingMapper bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.trainService = trainService;
        this.stationService = stationService;
        this.trainStopRepository = trainStopRepository;
        this.emailService = emailService;
        this.bookingMapper = bookingMapper;
    }

    public List<BookingDTO> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(bookingMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<BookingDTO> getBookingsByTrain(Long trainId) {
        trainService.getTrainEntityById(trainId); // verify existence
        return bookingRepository.findByTrainId(trainId).stream()
                .map(bookingMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<BookingDTO> getBookingsByCustomer(String email) {
        return bookingRepository.findByCustomerEmail(email).stream()
                .map(bookingMapper::toDto)
                .collect(Collectors.toList());
    }

    public BookingDTO getBookingById(Long id) {
        BookingBE booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking with id " + id + " not found"));
        return bookingMapper.toDto(booking);
    }

    public BookingDTO createBooking(BookingRequest request) {
        TrainBE train = trainService.getTrainEntityById(request.trainId());
        StationBE departure = stationService.getStationEntityById(request.departureStationId());
        StationBE arrival = stationService.getStationEntityById(request.arrivalStationId());

        List<StationBE> routeStations = train.getRoute().getStations();
        if (!routeStations.contains(departure)) {
            throw new BookingException("Departure station is not on this train's route");
        }
        if (!routeStations.contains(arrival)) {
            throw new BookingException("Arrival station is not on this train's route");
        }

        int departureOrder = getStopOrder(train.getId(), departure.getId());
        int arrivalOrder = getStopOrder(train.getId(), arrival.getId());

        if (departureOrder >= arrivalOrder) {
            throw new BookingException("Departure station must come before arrival station");
        }

        int bookedSeats = bookingRepository.getTotalBookedSeats(train.getId());
        if (bookedSeats + request.numberOfSeats() > train.getCapacity()) {
            throw new BookingException("Not enough seats available. Available: " + (train.getCapacity() - bookedSeats));
        }

        BookingBE booking = new BookingBE(
                null, train, departure, arrival,
                request.customerEmail(), request.numberOfSeats(), LocalDateTime.now()
        );
        BookingBE saved = bookingRepository.save(booking);

        emailService.sendBookingConfirmation(request.customerEmail(), train.getName(),
                departure.getName(), arrival.getName(), request.numberOfSeats());

        return bookingMapper.toDto(saved);
    }

    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new ResourceNotFoundException("Booking with id " + id + " not found");
        }
        bookingRepository.deleteById(id);
    }

    private int getStopOrder(Long trainId, Long stationId) {
        return trainStopRepository.findByTrainIdOrderByStopOrder(trainId)
                .stream()
                .filter(stop -> stop.getStation().getId().equals(stationId))
                .findFirst()
                .map(TrainStopBE::getStopOrder)
                .orElseThrow(() -> new BookingException("Station not found in train schedule"));
    }
}