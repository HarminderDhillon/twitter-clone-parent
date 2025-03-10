package com.dhillon.twitterclone.service;

import com.dhillon.twitterclone.entity.User;
import com.dhillon.twitterclone.exception.ResourceNotFoundException;
import com.dhillon.twitterclone.repository.UserRepository;
import com.dhillon.twitterclone.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the UserService.
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @InjectMocks
    private UserServiceImpl userService;
    
    private User testUser;
    private UUID testUserId;
    
    @BeforeEach
    public void setup() {
        testUserId = UUID.randomUUID();
        testUser = new User();
        testUser.setId(testUserId);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPasswordHash("password");
        testUser.setDisplayName("Test User");
    }
    
    @Test
    public void findById_WhenUserExists_ReturnsUser() {
        // Arrange
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        
        // Act
        Optional<User> result = userService.findById(testUserId);
        
        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(testUserId);
        verify(userRepository).findById(testUserId);
    }
    
    @Test
    public void findById_WhenUserDoesNotExist_ReturnsEmpty() {
        // Arrange
        when(userRepository.findById(testUserId)).thenReturn(Optional.empty());
        
        // Act
        Optional<User> result = userService.findById(testUserId);
        
        // Assert
        assertThat(result).isEmpty();
        verify(userRepository).findById(testUserId);
    }
    
    @Test
    public void findByUsername_WhenUserExists_ReturnsUser() {
        // Arrange
        String username = "testuser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(testUser));
        
        // Act
        Optional<User> result = userService.findByUsername(username);
        
        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo(username);
        verify(userRepository).findByUsername(username);
    }
    
    @Test
    public void createUser_EncodesPasswordAndSetsDefaults() {
        // Arrange
        User newUser = new User();
        newUser.setUsername("newuser");
        newUser.setEmail("new@example.com");
        newUser.setPasswordHash("password");
        
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // Act
        User result = userService.createUser(newUser);
        
        // Assert
        assertThat(result.getPasswordHash()).isEqualTo("encodedPassword");
        assertThat(result.isEnabled()).isTrue();
        assertThat(result.isEmailVerified()).isFalse();
        assertThat(result.isVerified()).isFalse();
        verify(passwordEncoder).encode("password");
        verify(userRepository).save(newUser);
    }
    
    @Test
    public void updateUser_WhenUserExists_UpdatesFields() {
        // Arrange
        User updatedUser = new User();
        updatedUser.setDisplayName("Updated Name");
        updatedUser.setBio("Updated bio");
        
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // Act
        User result = userService.updateUser(testUserId, updatedUser);
        
        // Assert
        assertThat(result.getDisplayName()).isEqualTo("Updated Name");
        assertThat(result.getBio()).isEqualTo("Updated bio");
        verify(userRepository).findById(testUserId);
        verify(userRepository).save(testUser);
    }
    
    @Test
    public void updateUser_WhenUserDoesNotExist_ThrowsException() {
        // Arrange
        User updatedUser = new User();
        
        when(userRepository.findById(testUserId)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThatThrownBy(() -> userService.updateUser(testUserId, updatedUser))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found with id:");
        
        verify(userRepository).findById(testUserId);
        verify(userRepository, never()).save(any(User.class));
    }
    
    @Test
    public void deleteUser_WhenUserExists_DeletesUser() {
        // Arrange
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        
        // Act
        userService.deleteUser(testUserId);
        
        // Assert
        verify(userRepository).findById(testUserId);
        verify(userRepository).delete(testUser);
    }
    
    @Test
    public void deleteUser_WhenUserDoesNotExist_ThrowsException() {
        // Arrange
        when(userRepository.findById(testUserId)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThatThrownBy(() -> userService.deleteUser(testUserId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found with id:");
        
        verify(userRepository).findById(testUserId);
        verify(userRepository, never()).delete(any(User.class));
    }
    
    @Test
    public void searchUsers_ReturnsMatchingUsers() {
        // Arrange
        String query = "test";
        List<User> expectedUsers = Arrays.asList(testUser);
        
        when(userRepository.searchUsers(query)).thenReturn(expectedUsers);
        
        // Act
        List<User> result = userService.searchUsers(query);
        
        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUsername()).isEqualTo("testuser");
        verify(userRepository).searchUsers(query);
    }
    
    @Test
    public void isUsernameAvailable_WhenUsernameDoesNotExist_ReturnsTrue() {
        // Arrange
        String username = "available";
        when(userRepository.existsByUsername(username)).thenReturn(false);
        
        // Act
        boolean result = userService.isUsernameAvailable(username);
        
        // Assert
        assertThat(result).isTrue();
        verify(userRepository).existsByUsername(username);
    }
    
    @Test
    public void isUsernameAvailable_WhenUsernameExists_ReturnsFalse() {
        // Arrange
        String username = "taken";
        when(userRepository.existsByUsername(username)).thenReturn(true);
        
        // Act
        boolean result = userService.isUsernameAvailable(username);
        
        // Assert
        assertThat(result).isFalse();
        verify(userRepository).existsByUsername(username);
    }
} 