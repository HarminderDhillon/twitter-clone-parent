package com.dhillon.twitterclone.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Entity representing a like on a post.
 */
@Entity
@Table(
    name = "likes",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "post_id"})
)
public class Like {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    /**
     * Default constructor.
     */
    public Like() {
    }
    
    /**
     * Constructor with required fields.
     *
     * @param user the user who liked
     * @param post the post that was liked
     */
    public Like(User user, Post post) {
        this.user = user;
        this.post = post;
    }
    
    /**
     * Full constructor.
     *
     * @param id the ID
     * @param user the user who liked
     * @param post the post that was liked
     * @param createdAt the creation timestamp
     */
    public Like(UUID id, User user, Post post, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.post = post;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public Post getPost() {
        return post;
    }
    
    public void setPost(Post post) {
        this.post = post;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Like like = (Like) o;
        return Objects.equals(id, like.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Like{" +
               "id=" + id +
               ", user=" + (user != null ? user.getUsername() : null) +
               ", post=" + (post != null ? post.getId() : null) +
               ", createdAt=" + createdAt +
               '}';
    }
    
    /**
     * Builder for Like.
     */
    public static class LikeBuilder {
        private UUID id;
        private User user;
        private Post post;
        private LocalDateTime createdAt;
        
        public LikeBuilder() {
        }
        
        public LikeBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        
        public LikeBuilder user(User user) {
            this.user = user;
            return this;
        }
        
        public LikeBuilder post(Post post) {
            this.post = post;
            return this;
        }
        
        public LikeBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        
        public Like build() {
            return new Like(id, user, post, createdAt);
        }
    }
    
    public static LikeBuilder builder() {
        return new LikeBuilder();
    }
} 