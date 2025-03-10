package com.dhillon.twitterclone.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Entity representing a user in the system.
 */
@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String passwordHash;
    
    private String displayName;
    
    @Column(columnDefinition = "TEXT")
    private String bio;
    
    private String location;
    private String website;
    private String profileImage;
    private String headerImage;
    private boolean verified;
    private boolean enabled;
    
    @Column(name = "email_verified")
    private boolean emailVerified;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();
    
    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> following = new ArrayList<>();
    
    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followers = new ArrayList<>();
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "role")
    private List<String> roles = new ArrayList<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications = new ArrayList<>();
    
    /**
     * Default constructor.
     */
    public User() {
    }
    
    /**
     * Constructor with required fields.
     *
     * @param username the username
     * @param email the email
     * @param passwordHash the password hash
     */
    public User(String username, String email, String passwordHash) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.enabled = true;
    }
    
    /**
     * Full constructor.
     *
     * @param id the ID
     * @param username the username
     * @param email the email
     * @param passwordHash the password hash
     * @param displayName the display name
     * @param bio the bio
     * @param location the location
     * @param website the website
     * @param profileImage the profile image URL
     * @param headerImage the header image URL
     * @param verified whether the user is verified
     * @param enabled whether the user is enabled
     * @param emailVerified whether the email is verified
     * @param createdAt the creation timestamp
     * @param updatedAt the update timestamp
     * @param posts the posts
     * @param following the following relationships
     * @param followers the follower relationships
     * @param roles the roles
     * @param likes the likes
     * @param notifications the notifications
     */
    public User(UUID id, String username, String email, String passwordHash, String displayName,
                String bio, String location, String website, String profileImage, String headerImage,
                boolean verified, boolean enabled, boolean emailVerified, LocalDateTime createdAt,
                LocalDateTime updatedAt, List<Post> posts, List<Follow> following, List<Follow> followers,
                List<String> roles, List<Like> likes, List<Notification> notifications) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.displayName = displayName;
        this.bio = bio;
        this.location = location;
        this.website = website;
        this.profileImage = profileImage;
        this.headerImage = headerImage;
        this.verified = verified;
        this.enabled = enabled;
        this.emailVerified = emailVerified;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.posts = posts;
        this.following = following;
        this.followers = followers;
        this.roles = roles;
        this.likes = likes;
        this.notifications = notifications;
    }
    
    // Getters and Setters
    
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPasswordHash() {
        return passwordHash;
    }
    
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    
    public String getBio() {
        return bio;
    }
    
    public void setBio(String bio) {
        this.bio = bio;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getWebsite() {
        return website;
    }
    
    public void setWebsite(String website) {
        this.website = website;
    }
    
    public String getProfileImage() {
        return profileImage;
    }
    
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
    
    public String getHeaderImage() {
        return headerImage;
    }
    
    public void setHeaderImage(String headerImage) {
        this.headerImage = headerImage;
    }
    
    public boolean isVerified() {
        return verified;
    }
    
    public void setVerified(boolean verified) {
        this.verified = verified;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public boolean isEmailVerified() {
        return emailVerified;
    }
    
    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
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
    
    public List<Post> getPosts() {
        return posts;
    }
    
    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
    
    public List<Follow> getFollowing() {
        return following;
    }
    
    public void setFollowing(List<Follow> following) {
        this.following = following;
    }
    
    public List<Follow> getFollowers() {
        return followers;
    }
    
    public void setFollowers(List<Follow> followers) {
        this.followers = followers;
    }
    
    public List<String> getRoles() {
        return roles;
    }
    
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
    
    public List<Like> getLikes() {
        return likes;
    }
    
    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }
    
    public List<Notification> getNotifications() {
        return notifications;
    }
    
    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "User{" +
               "id=" + id +
               ", username='" + username + '\'' +
               ", email='" + email + '\'' +
               ", displayName='" + displayName + '\'' +
               ", verified=" + verified +
               ", enabled=" + enabled +
               '}';
    }
    
    /**
     * Builder for User.
     */
    public static class UserBuilder {
        private UUID id;
        private String username;
        private String email;
        private String passwordHash;
        private String displayName;
        private String bio;
        private String location;
        private String website;
        private String profileImage;
        private String headerImage;
        private boolean verified;
        private boolean enabled = true;
        private boolean emailVerified;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private List<Post> posts = new ArrayList<>();
        private List<Follow> following = new ArrayList<>();
        private List<Follow> followers = new ArrayList<>();
        private List<String> roles = new ArrayList<>();
        private List<Like> likes = new ArrayList<>();
        private List<Notification> notifications = new ArrayList<>();
        
        public UserBuilder() {
        }
        
        public UserBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        
        public UserBuilder username(String username) {
            this.username = username;
            return this;
        }
        
        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }
        
        public UserBuilder passwordHash(String passwordHash) {
            this.passwordHash = passwordHash;
            return this;
        }
        
        public UserBuilder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }
        
        public UserBuilder bio(String bio) {
            this.bio = bio;
            return this;
        }
        
        public UserBuilder location(String location) {
            this.location = location;
            return this;
        }
        
        public UserBuilder website(String website) {
            this.website = website;
            return this;
        }
        
        public UserBuilder profileImage(String profileImage) {
            this.profileImage = profileImage;
            return this;
        }
        
        public UserBuilder headerImage(String headerImage) {
            this.headerImage = headerImage;
            return this;
        }
        
        public UserBuilder verified(boolean verified) {
            this.verified = verified;
            return this;
        }
        
        public UserBuilder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }
        
        public UserBuilder emailVerified(boolean emailVerified) {
            this.emailVerified = emailVerified;
            return this;
        }
        
        public UserBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        
        public UserBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }
        
        public UserBuilder posts(List<Post> posts) {
            this.posts = posts;
            return this;
        }
        
        public UserBuilder following(List<Follow> following) {
            this.following = following;
            return this;
        }
        
        public UserBuilder followers(List<Follow> followers) {
            this.followers = followers;
            return this;
        }
        
        public UserBuilder roles(List<String> roles) {
            this.roles = roles;
            return this;
        }
        
        public UserBuilder likes(List<Like> likes) {
            this.likes = likes;
            return this;
        }
        
        public UserBuilder notifications(List<Notification> notifications) {
            this.notifications = notifications;
            return this;
        }
        
        public User build() {
            return new User(
                id, username, email, passwordHash, displayName, bio, location, website,
                profileImage, headerImage, verified, enabled, emailVerified, createdAt,
                updatedAt, posts, following, followers, roles, likes, notifications
            );
        }
    }
    
    public static UserBuilder builder() {
        return new UserBuilder();
    }
} 