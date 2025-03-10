package com.dhillon.twitterclone.repository;

import com.dhillon.twitterclone.entity.Post;
import com.dhillon.twitterclone.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository for Post entity operations.
 */
@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
    
    /**
     * Find all posts by user ordered by creation date descending (user timeline).
     *
     * @param user the user
     * @param pageable pagination information
     * @return page of posts
     */
    Page<Post> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
    
    /**
     * Find all posts by user ID ordered by creation date descending (user timeline).
     *
     * @param userId the user ID
     * @param pageable pagination information
     * @return page of posts
     */
    Page<Post> findByUserIdOrderByCreatedAtDesc(UUID userId, Pageable pageable);
    
    /**
     * Find all posts that have a specific hashtag.
     *
     * @param hashtagId the hashtag ID
     * @param pageable pagination information
     * @return page of posts
     */
    @Query("SELECT p FROM Post p JOIN p.hashtags h WHERE h.id = :hashtagId ORDER BY p.createdAt DESC")
    Page<Post> findByHashtagId(@Param("hashtagId") UUID hashtagId, Pageable pageable);
    
    /**
     * Find all posts that have a specific hashtag name.
     *
     * @param hashtagName the hashtag name
     * @param pageable pagination information
     * @return page of posts
     */
    @Query("SELECT p FROM Post p JOIN p.hashtags h WHERE h.name = :hashtagName ORDER BY p.createdAt DESC")
    Page<Post> findByHashtagName(@Param("hashtagName") String hashtagName, Pageable pageable);
    
    /**
     * Search for posts containing specific text.
     *
     * @param query the search query
     * @param pageable pagination information
     * @return page of posts
     */
    @Query("SELECT p FROM Post p WHERE LOWER(p.content) LIKE LOWER(CONCAT('%', :query, '%')) ORDER BY p.createdAt DESC")
    Page<Post> searchPosts(@Param("query") String query, Pageable pageable);
    
    /**
     * Find all replies to a specific post.
     *
     * @param parentId the parent post ID
     * @param pageable pagination information
     * @return page of reply posts
     */
    Page<Post> findByParentIdOrderByCreatedAtDesc(UUID parentId, Pageable pageable);
    
    /**
     * Find home timeline posts (posts from followed users).
     *
     * @param userId the user ID
     * @param pageable pagination information
     * @return page of posts
     */
    @Query("SELECT p FROM Post p WHERE p.user.id IN " +
           "(SELECT f.following.id FROM Follow f WHERE f.follower.id = :userId) " +
           "OR p.user.id = :userId " +
           "ORDER BY p.createdAt DESC")
    Page<Post> findHomeTimeline(@Param("userId") UUID userId, Pageable pageable);
    
    /**
     * Find trending posts based on like count, repost count, and reply count.
     *
     * @param pageable pagination information
     * @return page of trending posts
     */
    @Query("SELECT p FROM Post p ORDER BY (p.likeCount + p.repostCount + p.replyCount) DESC, p.createdAt DESC")
    Page<Post> findTrendingPosts(Pageable pageable);
} 