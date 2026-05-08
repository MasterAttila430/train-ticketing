package com.siemens.train.controller;

import com.siemens.train.entities.BookingBE;
import com.siemens.train.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // List all bookings (Admin only)
    @GetMapping
    public ResponseEntity<List<BookingBE>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    // Get single booking by ID
    @GetMapping("/{id}")
    public ResponseEntity<BookingBE> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    // List all bookings for a train (Admin only)
    @GetMapping("/train/{trainId}")
    public ResponseEntity<List<BookingBE>> getBookingsByTrain(@PathVariable Long trainId) {
        return ResponseEntity.ok(bookingService.getBookingsByTrain(trainId));
    }

    // List bookings by customer email
    @GetMapping("/customer")
    public ResponseEntity<List<BookingBE>> getBookingsByCustomer(@RequestParam String email) {
        return ResponseEntity.ok(bookingService.getBookingsByCustomer(email));
    }

    // Create a new booking
    @PostMapping
    public ResponseEntity<BookingBE> createBooking(@RequestBody BookingRequest request) {
        BookingBE created = bookingService.createBooking(
                request.trainId(),
                request.departureStationId(),
                request.arrivalStationId(),
                request.customerEmail(),
                request.numberOfSeats()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Cancel a booking
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }

    // DTO for booking requests
    public record BookingRequest(
            Long trainId,
            Long departureStationId,
            Long arrivalStationId,
            String customerEmail,
            int numberOfSeats
    ) {}
}