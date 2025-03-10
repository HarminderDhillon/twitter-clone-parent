package com.dhillon.twitterclone.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Map;

/**
 * Data Transfer Object for User entity.
 */
@Schema(description = "User data transfer object")
public record UserDto(
    @Schema(description = "Unique identifier of the user", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID id,
    
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Schema(description = "Username of the user", example = "johndoe", minLength = 3, maxLength = 50, required = true)
    String username,
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Schema(description = "Email address of the user", example = "john.doe@example.com", required = true)
    String email,
    
    @Schema(description = "Display name of the user", example = "John Doe")
    String displayName,
    
    @Size(max = 160, message = "Bio cannot exceed 160 characters")
    @Schema(description = "Biography of the user", example = "Software developer and tech enthusiast", maxLength = 160)
    String bio,
    
    @Schema(description = "Location of the user", example = "San Francisco, CA")
    String location,
    
    @Schema(description = "Website of the user", example = "https://johndoe.example.com")
    String website,
    
    @Schema(description = "Profile image URL of the user", example = "/images/profile/johndoe.jpg")
    String profileImage,
    
    @Schema(description = "Header image URL of the user", example = "/images/header/johndoe.jpg")
    String headerImage,
    
    @Schema(description = "Flag indicating if the user is verified", example = "false")
    boolean verified,
    
    @Schema(description = "Timestamp when the user was created", example = "2023-03-15T14:30:00")
    LocalDateTime createdAt,
    
    @Schema(description = "Number of followers the user has", example = "1024")
    int followersCount,
    
    @Schema(description = "Number of users the user is following", example = "512")
    int followingCount,
    
    // For registration/update
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    @Schema(description = "Password for user registration or update", example = "SecureP@ssw0rd", minLength = 8, maxLength = 100, accessMode = Schema.AccessMode.WRITE_ONLY)
    String password
) {
    // Factory methods for common use cases
    
    // Create a user for registration
    public static UserDto forRegistration(String username, String email, String password) {
        return new UserDto(
            null, username, email, username, null, null, null, null, null, 
            false, LocalDateTime.now(), 0, 0, password
        );
    }
    
    // Create a minimal user representation
    public static UserDto basic(UUID id, String username, String displayName, String profileImage) {
        return new UserDto(
            id, username, null, displayName, null, null, null, 
            profileImage, null, false, null, 0, 0, null
        );
    }
    
    // Create a UserDto for error responses
    public static UserDto forError(Map<String, String> errors) {
        // Create a simple user with error message in the username field
        // This is a workaround since we can't add additional fields to the record
        return new UserDto(
            null, 
            errors.getOrDefault("username", "Error"), 
            errors.getOrDefault("email", "error@example.com"),
            "Error",
            null, null, null, null, null,
            false, null, 0, 0, null
        );
    }
} 