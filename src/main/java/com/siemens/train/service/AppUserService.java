package com.siemens.train.service;

import com.siemens.train.api.AppUserDTO;
import com.siemens.train.entities.AppUserBE;
import com.siemens.train.entities.Role;
import com.siemens.train.exception.BookingException;
import com.siemens.train.exception.ResourceNotFoundException;
import com.siemens.train.mapper.AppUserMapper;
import com.siemens.train.repo.AppUserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final AppUserMapper appUserMapper;

    public AppUserService(AppUserRepository appUserRepository, AppUserMapper appUserMapper) {
        this.appUserRepository = appUserRepository;
        this.appUserMapper = appUserMapper;
    }

    public List<AppUserDTO> getAllUsers() {
        return appUserRepository.findAll().stream()
                .map(appUserMapper::toDto)
                .collect(Collectors.toList());
    }

    public AppUserDTO getUserById(Long id) {
        AppUserBE user = appUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
        return appUserMapper.toDto(user);
    }

    public AppUserDTO register(String username, String email, String plainPassword) {
        if (appUserRepository.existsByUsername(username)) {
            throw new BookingException("Username already taken: " + username);
        }
        if (appUserRepository.existsByEmail(email)) {
            throw new BookingException("Email already registered: " + email);
        }

        String passwordHash = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        AppUserBE user = new AppUserBE(null, username, passwordHash, email, Role.CUSTOMER);

        return appUserMapper.toDto(appUserRepository.save(user));
    }

    public AppUserDTO login(String username, String plainPassword) {
        AppUserBE user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        if (!BCrypt.checkpw(plainPassword, user.getPasswordHash())) {
            throw new BookingException("Invalid password");
        }

        return appUserMapper.toDto(user);
    }

    public void deleteUser(Long id) {
        if (!appUserRepository.existsById(id)) {
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }
        appUserRepository.deleteById(id);
    }
}