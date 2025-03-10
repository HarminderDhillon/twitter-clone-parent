package com.dhillon.twitterclone.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Entity representing a follow relationship between users.
 */
@Entity
@Table(
    name = "follows",
    uniqueConstraints = @UniqueConstraint(columnNames = {"follower_id", "following_id"})
)
public class Follow {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id", nullable = false)
    private User following;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    /**
     * Default constructor.
     */
    public Follow() {
    }
    
    /**
     * Constructor with required fields.
     *
     * @param follower the user who is following
     * @param following the user being followed
     */
    public Follow(User follower, User following) {
        this.follower = follower;
        this.following = following;
    }
    
    /**
     * Full constructor.
     *
     * @param id the ID
     * @param follower the user who is following
     * @param following the user being followed
     * @param createdAt the creation timestamp
     */
    public Follow(UUID id, User follower, User following, LocalDateTime createdAt) {
        this.id = id;
        this.follower = follower;
        this.following = following;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public User getFollower() {
        return follower;
    }
    
    public void setFollower(User follower) {
        this.follower = follower;
    }
    
    public User getFollowing() {
        return following;
    }
    
    public void setFollowing(User following) {
        this.following = following;
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
        Follow follow = (Follow) o;
        return Objects.equals(id, follow.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Follow{" +
               "id=" + id +
               ", follower=" + (follower != null ? follower.getUsername() : null) +
               ", following=" + (following != null ? following.getUsername() : null) +
               ", createdAt=" + createdAt +
               '}';
    }
    
    /**
     * Builder for Follow.
     */
    public static class FollowBuilder {
        private UUID id;
        private User follower;
        private User following;
        private LocalDateTime createdAt;
        
        public FollowBuilder() {
        }
        
        public FollowBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        
        public FollowBuilder follower(User follower) {
            this.follower = follower;
            return this;
        }
        
        public FollowBuilder following(User following) {
            this.following = following;
            return this;
        }
        
        public FollowBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        
        public Follow build() {
            return new Follow(id, follower, following, createdAt);
        }
    }
    
    public static FollowBuilder builder() {
        return new FollowBuilder();
    }
} 