package com.dhillon.twitterclone.repository;

import com.dhillon.twitterclone.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for the UserRepository.
 * Uses DataJpaTest which sets up an in-memory database.
 */
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    private UserRepository userRepository;
    
    private User testUser;
    
    @BeforeEach
    public void setup() {
        // Create a test user
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPasswordHash("hashedpassword");
        testUser.setDisplayName("Test User");
        testUser.setBio("Test bio");
        
        // Use TestEntityManager to persist the test user
        testUser = entityManager.persistAndFlush(testUser);
    }
    
    @Test
    public void findByUsername_WhenUserExists_ReturnsUser() {
        // Act
        Optional<User> foundUser = userRepository.findByUsername("testuser");
        
        // Assert
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("testuser");
        assertThat(foundUser.get().getEmail()).isEqualTo("test@example.com");
    }
    
    @Test
    public void findByUsername_WhenUserDoesNotExist_ReturnsEmpty() {
        // Act
        Optional<User> foundUser = userRepository.findByUsername("nonexistent");
        
        // Assert
        assertThat(foundUser).isEmpty();
    }
    
    @Test
    public void findByEmail_WhenUserExists_ReturnsUser() {
        // Act
        Optional<User> foundUser = userRepository.findByEmail("test@example.com");
        
        // Assert
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("test@example.com");
    }
    
    @Test
    public void existsByUsername_WhenUserExists_ReturnsTrue() {
        // Act
        boolean exists = userRepository.existsByUsername("testuser");
        
        // Assert
        assertThat(exists).isTrue();
    }
    
    @Test
    public void existsByUsername_WhenUserDoesNotExist_ReturnsFalse() {
        // Act
        boolean exists = userRepository.existsByUsername("nonexistent");
        
        // Assert
        assertThat(exists).isFalse();
    }
    
    @Test
    public void existsByEmail_WhenUserExists_ReturnsTrue() {
        // Act
        boolean exists = userRepository.existsByEmail("test@example.com");
        
        // Assert
        assertThat(exists).isTrue();
    }
    
    @Test
    public void searchUsers_ByUsername_ReturnsMatchingUsers() {
        // Arrange
        // Create another user that will match the search
        User anotherUser = new User();
        anotherUser.setUsername("testsearch");
        anotherUser.setEmail("search@example.com");
        anotherUser.setPasswordHash("hashedpassword");
        entityManager.persistAndFlush(anotherUser);
        
        // Act
        List<User> users = userRepository.searchUsers("test");
        
        // Assert
        assertThat(users).hasSize(2);
        assertThat(users).extracting("username").containsOnly("testuser", "testsearch");
    }
    
    @Test
    public void searchUsers_ByDisplayName_ReturnsMatchingUsers() {
        // Arrange
        // Create another user that will match the search by display name
        User anotherUser = new User();
        anotherUser.setUsername("different");
        anotherUser.setEmail("different@example.com");
        anotherUser.setPasswordHash("hashedpassword");
        anotherUser.setDisplayName("Testing Match");
        entityManager.persistAndFlush(anotherUser);
        
        // Act
        List<User> users = userRepository.searchUsers("test");
        
        // Assert
        assertThat(users).hasSize(2);
        assertThat(users).extracting("username").containsOnly("testuser", "different");
    }
} 