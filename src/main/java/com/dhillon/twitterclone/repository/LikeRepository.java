package com.dhillon.twitterclone.repository;

import com.dhillon.twitterclone.entity.Like;
import com.dhillon.twitterclone.entity.Post;
import com.dhillon.twitterclone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Like entity operations.
 */
@Repository
public interface LikeRepository extends JpaRepository<Like, UUID> {
    
    /**
     * Find a like by user and post.
     *
     * @param user the user who liked
     * @param post the post that was liked
     * @return optional like if exists
     */
    Optional<Like> findByUserAndPost(User user, Post post);
    
    /**
     * Check if a like exists by user ID and post ID.
     *
     * @param userId the ID of the user
     * @param postId the ID of the post
     * @return true if exists, false otherwise
     */
    boolean existsByUserIdAndPostId(UUID userId, UUID postId);
    
    /**
     * Count the number of likes for a post.
     *
     * @param postId the ID of the post
     * @return the count of likes
     */
    long countByPostId(UUID postId);
    
    /**
     * Find all users who liked a post.
     *
     * @param postId the ID of the post
     * @return list of like entities
     */
    List<Like> findByPostId(UUID postId);
    
    /**
     * Delete a like by user and post.
     *
     * @param user the user who liked
     * @param post the post that was liked
     * @return number of rows affected
     */
    long deleteByUserAndPost(User user, Post post);
} 