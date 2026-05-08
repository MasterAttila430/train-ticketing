package com.siemens.train.repo;

import com.siemens.train.entities.AppUserBE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUserBE, Long> {

    // Find user by username for login
    Optional<AppUserBE> findByUsername(String username);

    // Check if username or email already taken during registration
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}