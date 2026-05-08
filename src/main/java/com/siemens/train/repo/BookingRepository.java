package com.siemens.train.repo;

import com.siemens.train.entities.BookingBE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<BookingBE, Long> {

    // Find all bookings for a specific train
    List<BookingBE> findByTrainId(Long trainId);

    // Find all bookings made by a specific customer
    List<BookingBE> findByCustomerEmail(String customerEmail);

    // Get total booked seats for a train to check overbooking
    @Query("SELECT COALESCE(SUM(b.numberOfSeats), 0) FROM BookingBE b WHERE b.train.id = :trainId")
    int getTotalBookedSeats(@Param("trainId") Long trainId);
}