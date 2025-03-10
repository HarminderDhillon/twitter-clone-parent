package com.dhillon.twitterclone.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Entity representing a hashtag in the system.
 */
@Entity
@Table(name = "hashtags")
public class Hashtag {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(unique = true, nullable = false)
    private String name;
    
    private int postCount;
    
    @ManyToMany(mappedBy = "hashtags")
    private Set<Post> posts = new HashSet<>();
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    /**
     * Default constructor.
     */
    public Hashtag() {
    }
    
    /**
     * Constructor with required fields.
     *
     * @param name the name of the hashtag
     */
    public Hashtag(String name) {
        this.name = name;
        this.postCount = 0;
    }
    
    /**
     * Full constructor.
     *
     * @param id the ID
     * @param name the name of the hashtag
     * @param postCount the post count
     * @param posts the posts with this hashtag
     * @param createdAt the creation timestamp
     * @param updatedAt the update timestamp
     */
    public Hashtag(UUID id, String name, int postCount, Set<Post> posts, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.postCount = postCount;
        this.posts = posts;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and Setters
    
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getPostCount() {
        return postCount;
    }
    
    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }
    
    public Set<Post> getPosts() {
        return posts;
    }
    
    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    /**
     * Increments the post count by 1.
     */
    public void incrementPostCount() {
        this.postCount++;
    }
    
    /**
     * Decrements the post count by 1, ensuring it doesn't go below 0.
     */
    public void decrementPostCount() {
        if (this.postCount > 0) {
            this.postCount--;
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hashtag hashtag = (Hashtag) o;
        return Objects.equals(id, hashtag.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Hashtag{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", postCount=" + postCount +
               '}';
    }
    
    /**
     * Builder for Hashtag.
     */
    public static class HashtagBuilder {
        private UUID id;
        private String name;
        private int postCount;
        private Set<Post> posts = new HashSet<>();
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        
        public HashtagBuilder() {
        }
        
        public HashtagBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        
        public HashtagBuilder name(String name) {
            this.name = name;
            return this;
        }
        
        public HashtagBuilder postCount(int postCount) {
            this.postCount = postCount;
            return this;
        }
        
        public HashtagBuilder posts(Set<Post> posts) {
            this.posts = posts;
            return this;
        }
        
        public HashtagBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        
        public HashtagBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }
        
        public Hashtag build() {
            return new Hashtag(id, name, postCount, posts, createdAt, updatedAt);
        }
    }
    
    public static HashtagBuilder builder() {
        return new HashtagBuilder();
    }
} 