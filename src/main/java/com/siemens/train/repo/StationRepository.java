package com.siemens.train.repo;

import com.siemens.train.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StationRepository extends JpaRepository<Station, Long> {

    // Find all stations in a specific city
    List<Station> findByCity(String city);

    // Check if a station with the given name already exists
    boolean existsByName(String name);
}