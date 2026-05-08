package com.siemens.train.repo;

import com.siemens.train.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Find all bookings for a specific train
    List<Booking> findByTrainId(Long trainId);

    // Find all bookings made by a specific customer
    List<Booking> findByCustomerEmail(String customerEmail);

    // Get total booked seats for a train to check overbooking
    @Query("SELECT COALESCE(SUM(b.numberOfSeats), 0) FROM Booking b WHERE b.train.id = :trainId")
    int getTotalBookedSeats(@Param("trainId") Long trainId);
}