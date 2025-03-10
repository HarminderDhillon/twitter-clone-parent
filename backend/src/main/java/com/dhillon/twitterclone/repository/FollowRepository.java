package com.dhillon.twitterclone.repository;

import com.dhillon.twitterclone.entity.Follow;
import com.dhillon.twitterclone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Follow entity operations.
 */
@Repository
public interface FollowRepository extends JpaRepository<Follow, UUID> {
    
    /**
     * Find a follow relationship between two users.
     *
     * @param follower the user who is following
     * @param following the user being followed
     * @return optional follow relationship if exists
     */
    Optional<Follow> findByFollowerAndFollowing(User follower, User following);
    
    /**
     * Check if a follow relationship exists between two users.
     *
     * @param followerId the ID of the user who is following
     * @param followingId the ID of the user being followed
     * @return true if exists, false otherwise
     */
    boolean existsByFollowerIdAndFollowingId(UUID followerId, UUID followingId);
    
    /**
     * Count the number of followers a user has.
     *
     * @param userId the ID of the user
     * @return the count of followers
     */
    long countByFollowingId(UUID userId);
    
    /**
     * Count the number of users a user is following.
     *
     * @param userId the ID of the user
     * @return the count of followings
     */
    long countByFollowerId(UUID userId);
    
    /**
     * Delete a follow relationship between two users.
     *
     * @param follower the user who is following
     * @param following the user being followed
     * @return number of rows affected
     */
    long deleteByFollowerAndFollowing(User follower, User following);
} 