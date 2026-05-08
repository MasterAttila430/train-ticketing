package com.siemens.train.service;

import com.siemens.train.exception.ResourceNotFoundException;
import com.siemens.train.model.Booking;
import com.siemens.train.model.Train;
import com.siemens.train.repo.BookingRepository;
import com.siemens.train.repo.TrainRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainService {

    private final TrainRepository trainRepository;
    private final BookingRepository bookingRepository;
    private final EmailService emailService;

    public TrainService(TrainRepository trainRepository,
                        BookingRepository bookingRepository,
                        EmailService emailService) {
        this.trainRepository = trainRepository;
        this.bookingRepository = bookingRepository;
        this.emailService = emailService;
    }

    public List<Train> getAllTrains() {
        return trainRepository.findAll();
    }

    public Train getTrainById(Long id) {
        return trainRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Train with id " + id + " not found"));
    }

    public List<Train> getTrainsByRoute(Long routeId) {
        return trainRepository.findByRouteId(routeId);
    }

    public List<Train> getDelayedTrains() {
        return trainRepository.findByDelayedTrue();
    }

    public Train createTrain(Train train) {
        return trainRepository.save(train);
    }

    public Train updateTrain(Long id, Train updated) {
        Train existing = getTrainById(id);
        existing.setName(updated.getName());
        existing.setCapacity(updated.getCapacity());
        existing.setRoute(updated.getRoute());
        return trainRepository.save(existing);
    }

    public void deleteTrain(Long id) {
        getTrainById(id); // throws if not found
        trainRepository.deleteById(id);
    }

    // Mark train as delayed and notify all affected passengers via email
    public Train markAsDelayed(Long id) {
        Train train = getTrainById(id);
        train.setDelayed(true);
        trainRepository.save(train);

        // Find all bookings for this train and notify customers
        List<Booking> bookings = bookingRepository.findByTrainId(id);
        bookings.forEach(booking ->
                emailService.sendDelayNotification(
                        booking.getCustomerEmail(),
                        train.getName()
                )
        );

        return train;
    }
}