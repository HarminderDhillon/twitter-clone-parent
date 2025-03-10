package com.dhillon.twitterclone.entity;

/**
 * Enum representing the type of notification.
 */
public enum NotificationType {
    /**
     * When a user likes a post.
     */
    LIKE,
    
    /**
     * When a user replies to a post.
     */
    REPLY,
    
    /**
     * When a user reposts a post.
     */
    REPOST,
    
    /**
     * When a user follows another user.
     */
    FOLLOW,
    
    /**
     * When a user mentions another user in a post.
     */
    MENTION
} 