package com.dhillon.twitterclone.util;

import com.dhillon.twitterclone.dto.UserDto;
import com.dhillon.twitterclone.entity.User;
import com.dhillon.twitterclone.repository.FollowRepository;

/**
 * Mapper utility class for User and UserDto conversion.
 */
public class UserMapper {
    
    private UserMapper() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Convert User entity to UserDto.
     *
     * @param user the user entity
     * @return the user DTO
     */
    public static UserDto toDto(User user) {
        if (user == null) {
            return null;
        }
        
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
            0,
            0,
            null
        );
    }
    
    /**
     * Convert User entity to UserDto with follower counts.
     *
     * @param user the user entity
     * @param followersCount the followers count
     * @param followingCount the following count
     * @return the user DTO with follower counts
     */
    public static UserDto toDtoWithCounts(User user, long followersCount, long followingCount) {
        if (user == null) {
            return null;
        }
        
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
            (int) followersCount,
            (int) followingCount,
            null
        );
    }
    
    /**
     * Convert UserDto to User entity.
     *
     * @param dto the user DTO
     * @return the user entity
     */
    public static User toEntity(UserDto dto) {
        if (dto == null) {
            return null;
        }
        
        User user = new User();
        // Only set the fields that can be updated from the DTO
        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setPasswordHash(dto.password()); // Will be encoded by the service
        user.setDisplayName(dto.displayName());
        user.setBio(dto.bio());
        user.setLocation(dto.location());
        user.setWebsite(dto.website());
        user.setProfileImage(dto.profileImage());
        user.setHeaderImage(dto.headerImage());
        
        return user;
    }
    
    /**
     * Update a User entity with data from a UserDto.
     * Only updates fields that are allowed to be updated.
     *
     * @param user the user entity to update
     * @param dto the user DTO with updated data
     * @return the updated user entity
     */
    public static User updateEntity(User user, UserDto dto) {
        if (user == null || dto == null) {
            return user;
        }
        
        // Only update fields that are provided in the DTO
        if (dto.displayName() != null) {
            user.setDisplayName(dto.displayName());
        }
        
        if (dto.bio() != null) {
            user.setBio(dto.bio());
        }
        
        if (dto.location() != null) {
            user.setLocation(dto.location());
        }
        
        if (dto.website() != null) {
            user.setWebsite(dto.website());
        }
        
        if (dto.profileImage() != null) {
            user.setProfileImage(dto.profileImage());
        }
        
        if (dto.headerImage() != null) {
            user.setHeaderImage(dto.headerImage());
        }
        
        return user;
    }
} 