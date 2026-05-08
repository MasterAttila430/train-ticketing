package com.siemens.train.service;

import com.siemens.train.exception.BookingException;
import com.siemens.train.exception.ResourceNotFoundException;
import com.siemens.train.model.AppUser;
import com.siemens.train.model.Role;
import com.siemens.train.repo.AppUserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;

    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public List<AppUser> getAllUsers() {
        return appUserRepository.findAll();
    }

    public AppUser getUserById(Long id) {
        return appUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with id " + id + " not found"));
    }

    public AppUser register(String username, String email, String plainPassword) {
        // Check username and email are not already taken
        if (appUserRepository.existsByUsername(username)) {
            throw new BookingException("Username already taken: " + username);
        }
        if (appUserRepository.existsByEmail(email)) {
            throw new BookingException("Email already registered: " + email);
        }

        // Hash the password before storing - never store plain text!
        String passwordHash = BCrypt.hashpw(plainPassword, BCrypt.gensalt());

        AppUser user = new AppUser(null, username, passwordHash, email, Role.CUSTOMER);
        return appUserRepository.save(user);
    }

    public AppUser login(String username, String plainPassword) {
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found: " + username));

        // Compare plain password against stored hash
        if (!BCrypt.checkpw(plainPassword, user.getPasswordHash())) {
            throw new BookingException("Invalid password");
        }

        return user;
    }

    public void deleteUser(Long id) {
        getUserById(id); // throws if not found
        appUserRepository.deleteById(id);
    }
}