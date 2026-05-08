package com.siemens.train.controller;

import com.siemens.train.api.BookingDTO;
import com.siemens.train.api.BookingRequest;
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

    @GetMapping
    public ResponseEntity<List<BookingDTO>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @GetMapping("/train/{trainId}")
    public ResponseEntity<List<BookingDTO>> getBookingsByTrain(@PathVariable Long trainId) {
        return ResponseEntity.ok(bookingService.getBookingsByTrain(trainId));
    }

    @GetMapping("/customer")
    public ResponseEntity<List<BookingDTO>> getBookingsByCustomer(@RequestParam String email) {
        return ResponseEntity.ok(bookingService.getBookingsByCustomer(email));
    }

    @PostMapping
    public ResponseEntity<BookingDTO> createBooking(@RequestBody BookingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.createBooking(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }
}