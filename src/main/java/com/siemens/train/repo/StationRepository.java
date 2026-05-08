package com.siemens.train.repo;

import com.siemens.train.entities.StationBE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StationRepository extends JpaRepository<StationBE, Long> {

    // Find all stations in a specific city
    List<StationBE> findByCity(String city);

    // Check if a station with the given name already exists
    boolean existsByName(String name);
}