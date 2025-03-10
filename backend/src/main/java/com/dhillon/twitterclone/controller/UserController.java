package com.dhillon.twitterclone.controller;

import com.dhillon.twitterclone.dto.UserDto;
import com.dhillon.twitterclone.entity.User;
import com.dhillon.twitterclone.service.UserService;
import com.dhillon.twitterclone.repository.FollowRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.HashMap;

@RestController
@RequestMapping("/users")
@Tag(name = "User", description = "User management APIs")
public class UserController {

    private final UserService userService;
    private final FollowRepository followRepository;

    public UserController(UserService userService, FollowRepository followRepository) {
        this.userService = userService;
        this.followRepository = followRepository;
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve a list of all users")
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class)))
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<User> users = userService.searchUsers("");
        List<UserDto> userDTOs = users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    @GetMapping("/{idOrUsername}")
    @Operation(summary = "Get user by ID or username", description = "Retrieve a specific user by their ID or username")
    @ApiResponse(responseCode = "200", description = "User retrieved successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<?> getUser(
            @Parameter(description = "ID or username of the user to retrieve", required = true)
            @PathVariable String idOrUsername) {
        try {
            UUID id = UUID.fromString(idOrUsername);
            Optional<User> userOpt = userService.findById(id);
            if (userOpt.isPresent()) {
                return ResponseEntity.ok(convertToDto(userOpt.get()));
            } else {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("status", 404);
                errorResponse.put("error", "Not Found");
                errorResponse.put("message", "User not found with ID: " + id);
                errorResponse.put("path", "/users/" + idOrUsername);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
        } catch (IllegalArgumentException e) {
            // Not a UUID, so treat as username
            Optional<User> userOpt = userService.findByUsername(idOrUsername);
            if (userOpt.isPresent()) {
                return ResponseEntity.ok(convertToDto(userOpt.get()));
            } else {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("status", 404);
                errorResponse.put("error", "Not Found");
                errorResponse.put("message", "User not found with username: " + idOrUsername);
                errorResponse.put("path", "/users/" + idOrUsername);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
        }
    }

    @PutMapping("/{idOrUsername}")
    @Operation(summary = "Update user", description = "Update an existing user")
    @ApiResponse(responseCode = "200", description = "User updated successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<UserDto> updateUser(
            @Parameter(description = "ID or username of the user to update", required = true)
            @PathVariable String idOrUsername,
            @Parameter(description = "Updated user data", required = true)
            @RequestBody UserDto userDto) {
        try {
            UUID id = UUID.fromString(idOrUsername);
            if (!userService.findById(id).isPresent()) {
                return ResponseEntity.notFound().build();
            }
            User updatedUser = userService.updateUser(id, convertToEntity(userDto));
            return ResponseEntity.ok(convertToDto(updatedUser));
        } catch (IllegalArgumentException e) {
            // Not a UUID, so treat as username
            Optional<User> existingUser = userService.findByUsername(idOrUsername);
            if (existingUser.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            User updatedUser = userService.updateUser(existingUser.get().getId(), convertToEntity(userDto));
            return ResponseEntity.ok(convertToDto(updatedUser));
        }
    }

    @DeleteMapping("/{idOrUsername}")
    @Operation(summary = "Delete user", description = "Delete an existing user")
    @ApiResponse(responseCode = "204", description = "User deleted successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID or username of the user to delete", required = true)
            @PathVariable String idOrUsername) {
        try {
            UUID id = UUID.fromString(idOrUsername);
            if (userService.findById(id).isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            // Not a UUID, so treat as username
            Optional<User> user = userService.findByUsername(idOrUsername);
            if (user.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            userService.deleteUser(user.get().getId());
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/check-username")
    @Operation(summary = "Check username availability", description = "Check if a username is available for registration")
    @ApiResponse(responseCode = "200", description = "Username availability checked successfully")
    public ResponseEntity<Map<String, Boolean>> isUsernameAvailable(
            @Parameter(description = "Username to check", required = true)
            @RequestParam String username) {
        boolean available = userService.isUsernameAvailable(username);
        Map<String, Boolean> response = Map.of("available", available);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-email")
    @Operation(summary = "Check email availability", description = "Check if an email is available for registration")
    @ApiResponse(responseCode = "200", description = "Email availability checked successfully")
    public ResponseEntity<Map<String, Boolean>> isEmailAvailable(
            @Parameter(description = "Email to check", required = true)
            @RequestParam String email) {
        boolean available = userService.isEmailAvailable(email);
        Map<String, Boolean> response = Map.of("available", available);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @Operation(summary = "Search users", description = "Search for users by username or display name")
    @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    public ResponseEntity<List<UserDto>> searchUsers(
            @Parameter(description = "Search query", required = true)
            @RequestParam String query) {
        List<User> users = userService.searchUsers(query);
        List<UserDto> userDtos = users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDtos);
    }

    @PostMapping
    @Operation(summary = "Create user", description = "Create a new user")
    @ApiResponse(responseCode = "201", description = "User created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid user data")
    public ResponseEntity<?> createUser(
            @Parameter(description = "User data for creating a new user", required = true)
            @RequestBody UserDto userDto) {
        
        // Check if username is available
        if (!userService.isUsernameAvailable(userDto.username())) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", 400);
            errorResponse.put("error", "Bad Request");
            errorResponse.put("message", "Username is already taken");
            errorResponse.put("path", "/users");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        
        // Check if email is available
        if (!userService.isEmailAvailable(userDto.email())) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", 400);
            errorResponse.put("error", "Bad Request");
            errorResponse.put("message", "Email is already registered");
            errorResponse.put("path", "/users");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        
        User user = convertToEntity(userDto);
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(createdUser));
    }
    
    /**
     * Converts User entity to UserDto
     */
    private UserDto convertToDto(User user) {
        int followersCount = (int) followRepository.countByFollowingId(user.getId());
        int followingCount = (int) followRepository.countByFollowerId(user.getId());
        
        return new UserDto(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getDisplayName(),
            user.getBio(),
            user.getLocation(),
            user.getWebsite(),
            user.getProfileImage(),
            user.getHeaderImage(),
            user.isVerified(),
            user.getCreatedAt(),
            followersCount,
            followingCount,
            null // no need to expose password
        );
    }
    
    /**
     * Converts UserDto to User entity
     */
    private User convertToEntity(UserDto userDto) {
        User user = new User();
        user.setId(userDto.id());
        user.setUsername(userDto.username());
        user.setEmail(userDto.email());
        user.setDisplayName(userDto.displayName());
        user.setBio(userDto.bio());
        user.setLocation(userDto.location());
        user.setWebsite(userDto.website());
        user.setProfileImage(userDto.profileImage());
        user.setHeaderImage(userDto.headerImage());
        if (userDto.password() != null) {
            user.setPasswordHash(userDto.password()); // Will be encoded in service
        }
        return user;
    }
} 