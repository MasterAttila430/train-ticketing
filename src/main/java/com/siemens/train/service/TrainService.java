package com.siemens.train.service;

import com.siemens.train.exception.ResourceNotFoundException;
import com.siemens.train.entities.BookingBE;
import com.siemens.train.entities.TrainBE;
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

    public List<TrainBE> getAllTrains() {
        return trainRepository.findAll();
    }

    public TrainBE getTrainById(Long id) {
        return trainRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Train with id " + id + " not found"));
    }

    public List<TrainBE> getTrainsByRoute(Long routeId) {
        return trainRepository.findByRouteId(routeId);
    }

    public List<TrainBE> getDelayedTrains() {
        return trainRepository.findByDelayedTrue();
    }

    public TrainBE createTrain(TrainBE train) {
        return trainRepository.save(train);
    }

    public TrainBE updateTrain(Long id, TrainBE updated) {
        TrainBE existing = getTrainById(id);
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
    public TrainBE markAsDelayed(Long id) {
        TrainBE train = getTrainById(id);
        train.setDelayed(true);
        trainRepository.save(train);

        // Find all bookings for this train and notify customers
        List<BookingBE> bookings = bookingRepository.findByTrainId(id);
        bookings.forEach(booking ->
                emailService.sendDelayNotification(
                        booking.getCustomerEmail(),
                        train.getName()
                )
        );

        return train;
    }
}