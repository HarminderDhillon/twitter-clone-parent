package com.dhillon.twitterclone.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Entity representing a notification in the system.
 */
@Entity
@Table(name = "notifications")
public class Notification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Enumerated(EnumType.STRING)
    private NotificationType type;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_id", nullable = false)
    private User actor;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
    
    private boolean read;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    /**
     * Default constructor.
     */
    public Notification() {
    }
    
    /**
     * Constructor with required fields.
     *
     * @param user the user receiving the notification
     * @param type the notification type
     * @param actor the user who triggered the notification
     */
    public Notification(User user, NotificationType type, User actor) {
        this.user = user;
        this.type = type;
        this.actor = actor;
        this.read = false;
    }
    
    /**
     * Constructor with required fields and post.
     *
     * @param user the user receiving the notification
     * @param type the notification type
     * @param actor the user who triggered the notification
     * @param post the related post
     */
    public Notification(User user, NotificationType type, User actor, Post post) {
        this.user = user;
        this.type = type;
        this.actor = actor;
        this.post = post;
        this.read = false;
    }
    
    /**
     * Full constructor.
     *
     * @param id the ID
     * @param user the user receiving the notification
     * @param type the notification type
     * @param actor the user who triggered the notification
     * @param post the related post
     * @param read whether the notification has been read
     * @param createdAt the creation timestamp
     */
    public Notification(UUID id, User user, NotificationType type, User actor, Post post, boolean read, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.type = type;
        this.actor = actor;
        this.post = post;
        this.read = read;
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
    
    public NotificationType getType() {
        return type;
    }
    
    public void setType(NotificationType type) {
        this.type = type;
    }
    
    public User getActor() {
        return actor;
    }
    
    public void setActor(User actor) {
        this.actor = actor;
    }
    
    public Post getPost() {
        return post;
    }
    
    public void setPost(Post post) {
        this.post = post;
    }
    
    public boolean isRead() {
        return read;
    }
    
    public void setRead(boolean read) {
        this.read = read;
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
        Notification that = (Notification) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Notification{" +
               "id=" + id +
               ", user=" + (user != null ? user.getUsername() : null) +
               ", type=" + type +
               ", actor=" + (actor != null ? actor.getUsername() : null) +
               ", post=" + (post != null ? post.getId() : null) +
               ", read=" + read +
               ", createdAt=" + createdAt +
               '}';
    }
    
    /**
     * Builder for Notification.
     */
    public static class NotificationBuilder {
        private UUID id;
        private User user;
        private NotificationType type;
        private User actor;
        private Post post;
        private boolean read;
        private LocalDateTime createdAt;
        
        public NotificationBuilder() {
        }
        
        public NotificationBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        
        public NotificationBuilder user(User user) {
            this.user = user;
            return this;
        }
        
        public NotificationBuilder type(NotificationType type) {
            this.type = type;
            return this;
        }
        
        public NotificationBuilder actor(User actor) {
            this.actor = actor;
            return this;
        }
        
        public NotificationBuilder post(Post post) {
            this.post = post;
            return this;
        }
        
        public NotificationBuilder read(boolean read) {
            this.read = read;
            return this;
        }
        
        public NotificationBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        
        public Notification build() {
            return new Notification(id, user, type, actor, post, read, createdAt);
        }
    }
    
    public static NotificationBuilder builder() {
        return new NotificationBuilder();
    }
} 