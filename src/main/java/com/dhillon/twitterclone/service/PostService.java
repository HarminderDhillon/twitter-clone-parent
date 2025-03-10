package com.dhillon.twitterclone.service;

import com.dhillon.twitterclone.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service for managing posts.
 */
public interface PostService {
    
    /**
     * Find a post by ID.
     *
     * @param id the post ID
     * @return optional post if found
     */
    Optional<Post> findById(UUID id);
    
    /**
     * Create a new post.
     *
     * @param post the post to create
     * @return the created post
     */
    Post createPost(Post post);
    
    /**
     * Update a post.
     *
     * @param id the post ID
     * @param post the updated post data
     * @return the updated post
     */
    Post updatePost(UUID id, Post post);
    
    /**
     * Delete a post.
     *
     * @param id the post ID
     */
    void deletePost(UUID id);
    
    /**
     * Get user timeline (posts by a specific user).
     *
     * @param userId the user ID
     * @param pageable pagination information
     * @return page of posts
     */
    Page<Post> getUserTimeline(UUID userId, Pageable pageable);
    
    /**
     * Get home timeline (posts from followed users).
     *
     * @param userId the user ID
     * @param pageable pagination information
     * @return page of posts
     */
    Page<Post> getHomeTimeline(UUID userId, Pageable pageable);
    
    /**
     * Search for posts.
     *
     * @param query the search query
     * @param pageable pagination information
     * @return page of posts
     */
    Page<Post> searchPosts(String query, Pageable pageable);
    
    /**
     * Get trending posts.
     *
     * @param pageable pagination information
     * @return page of posts
     */
    Page<Post> getTrendingPosts(Pageable pageable);
    
    /**
     * Get posts by hashtag.
     *
     * @param hashtag the hashtag name
     * @param pageable pagination information
     * @return page of posts
     */
    Page<Post> getPostsByHashtag(String hashtag, Pageable pageable);
    
    /**
     * Get replies to a post.
     *
     * @param postId the parent post ID
     * @param pageable pagination information
     * @return page of reply posts
     */
    Page<Post> getReplies(UUID postId, Pageable pageable);
    
    /**
     * Create a reply to a post.
     *
     * @param parentId the parent post ID
     * @param reply the reply post
     * @return the created reply post
     */
    Post createReply(UUID parentId, Post reply);
    
    /**
     * Create a repost.
     *
     * @param originalPostId the original post ID
     * @param repost the repost
     * @return the created repost
     */
    Post createRepost(UUID originalPostId, Post repost);
    
    /**
     * Extract hashtags from post content.
     *
     * @param content the post content
     * @return list of hashtag names
     */
    List<String> extractHashtags(String content);
} 