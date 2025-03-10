package com.dhillon.twitterclone.service;

import com.dhillon.twitterclone.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service for managing users.
 */
public interface UserService {
    
    /**
     * Find a user by ID.
     *
     * @param id the user ID
     * @return optional user if found
     */
    Optional<User> findById(UUID id);
    
    /**
     * Find a user by username.
     *
     * @param username the username
     * @return optional user if found
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Find a user by email.
     *
     * @param email the email
     * @return optional user if found
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Create a new user.
     *
     * @param user the user to create
     * @return the created user
     */
    User createUser(User user);
    
    /**
     * Update a user.
     *
     * @param id the user ID
     * @param user the updated user data
     * @return the updated user
     */
    User updateUser(UUID id, User user);
    
    /**
     * Delete a user.
     *
     * @param id the user ID
     */
    void deleteUser(UUID id);
    
    /**
     * Search for users by username or display name.
     *
     * @param query the search query
     * @return list of matching users
     */
    List<User> searchUsers(String query);
    
    /**
     * Check if a username is available.
     *
     * @param username the username to check
     * @return true if available, false otherwise
     */
    boolean isUsernameAvailable(String username);
    
    /**
     * Check if an email is available.
     *
     * @param email the email to check
     * @return true if available, false otherwise
     */
    boolean isEmailAvailable(String email);
} 