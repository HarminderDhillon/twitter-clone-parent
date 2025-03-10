package com.dhillon.twitterclone.service.impl;

import com.dhillon.twitterclone.entity.User;
import com.dhillon.twitterclone.exception.ResourceNotFoundException;
import com.dhillon.twitterclone.repository.UserRepository;
import com.dhillon.twitterclone.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of the UserService interface.
 */
@Service
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    /**
     * Constructor with dependencies.
     *
     * @param userRepository the user repository
     * @param passwordEncoder the password encoder
     */
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }
    
    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    @Override
    @Transactional
    public User createUser(User user) {
        // Encode the password before saving
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        
        // Set default values
        user.setEnabled(true);
        user.setEmailVerified(false);
        user.setVerified(false);
        
        return userRepository.save(user);
    }
    
    @Override
    @Transactional
    public User updateUser(UUID id, User updatedUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        
        // Update fields that are allowed to be updated
        if (updatedUser.getDisplayName() != null) {
            existingUser.setDisplayName(updatedUser.getDisplayName());
        }
        
        if (updatedUser.getBio() != null) {
            existingUser.setBio(updatedUser.getBio());
        }
        
        if (updatedUser.getLocation() != null) {
            existingUser.setLocation(updatedUser.getLocation());
        }
        
        if (updatedUser.getWebsite() != null) {
            existingUser.setWebsite(updatedUser.getWebsite());
        }
        
        if (updatedUser.getProfileImage() != null) {
            existingUser.setProfileImage(updatedUser.getProfileImage());
        }
        
        if (updatedUser.getHeaderImage() != null) {
            existingUser.setHeaderImage(updatedUser.getHeaderImage());
        }
        
        return userRepository.save(existingUser);
    }
    
    @Override
    @Transactional
    public void deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        
        userRepository.delete(user);
    }
    
    @Override
    public List<User> searchUsers(String query) {
        return userRepository.searchUsers(query);
    }
    
    @Override
    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }
    
    @Override
    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }
} 