package com.siemens.train.service;

import com.siemens.train.api.CreateTrainRequest;
import com.siemens.train.api.TrainDTO;
import com.siemens.train.entities.BookingBE;
import com.siemens.train.entities.RouteBE;
import com.siemens.train.entities.TrainBE;
import com.siemens.train.exception.ResourceNotFoundException;
import com.siemens.train.mapper.TrainMapper;
import com.siemens.train.repo.BookingRepository;
import com.siemens.train.repo.TrainRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainService {

    private final TrainRepository trainRepository;
    private final BookingRepository bookingRepository;
    private final EmailService emailService;
    private final RouteService routeService;
    private final TrainMapper trainMapper;

    public TrainService(TrainRepository trainRepository,
                        BookingRepository bookingRepository,
                        EmailService emailService,
                        RouteService routeService,
                        TrainMapper trainMapper) {
        this.trainRepository = trainRepository;
        this.bookingRepository = bookingRepository;
        this.emailService = emailService;
        this.routeService = routeService;
        this.trainMapper = trainMapper;
    }

    public List<TrainDTO> getAllTrains() {
        return trainRepository.findAll().stream()
                .map(trainMapper::toDto)
                .collect(Collectors.toList());
    }

    public TrainDTO getTrainById(Long id) {
        TrainBE train = trainRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Train with id " + id + " not found"));
        return trainMapper.toDto(train);
    }

    public List<TrainDTO> getTrainsByRoute(Long routeId) {
        return trainRepository.findByRouteId(routeId).stream()
                .map(trainMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<TrainDTO> getDelayedTrains() {
        return trainRepository.findByDelayedTrue().stream()
                .map(trainMapper::toDto)
                .collect(Collectors.toList());
    }

    public TrainDTO createTrain(CreateTrainRequest request) {
        RouteBE route = routeService.getRouteEntityById(request.routeId());
        TrainBE train = new TrainBE(null, request.name(), request.capacity(), false, route);
        return trainMapper.toDto(trainRepository.save(train));
    }

    public TrainDTO updateTrain(Long id, CreateTrainRequest request) {
        TrainBE existing = trainRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Train with id " + id + " not found"));
        RouteBE route = routeService.getRouteEntityById(request.routeId());

        existing.setName(request.name());
        existing.setCapacity(request.capacity());
        existing.setRoute(route);

        return trainMapper.toDto(trainRepository.save(existing));
    }

    public void deleteTrain(Long id) {
        if (!trainRepository.existsById(id)) {
            throw new ResourceNotFoundException("Train with id " + id + " not found");
        }
        trainRepository.deleteById(id);
    }

    public TrainDTO markAsDelayed(Long id) {
        TrainBE train = trainRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Train with id " + id + " not found"));
        train.setDelayed(true);
        trainRepository.save(train);

        List<BookingBE> bookings = bookingRepository.findByTrainId(id);
        bookings.forEach(booking ->
                emailService.sendDelayNotification(booking.getCustomerEmail(), train.getName())
        );

        return trainMapper.toDto(train);
    }

    // Helper for internal use returning Entity
    protected TrainBE getTrainEntityById(Long id) {
        return trainRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Train with id " + id + " not found"));
    }
}