package com.dhillon.twitterclone.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Entity representing a post (tweet) in the system.
 */
@Entity
@Table(name = "posts")
public class Post {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
    
    @ElementCollection
    @CollectionTable(name = "post_media", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "media_url")
    private List<String> media = new ArrayList<>();
    
    @Column(name = "is_reply")
    private boolean isReply;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Post parent;
    
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> replies = new ArrayList<>();
    
    @Column(name = "is_repost")
    private boolean isRepost;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_post_id")
    private Post originalPost;
    
    @OneToMany(mappedBy = "originalPost")
    private List<Post> reposts = new ArrayList<>();
    
    @ManyToMany
    @JoinTable(
        name = "post_hashtags",
        joinColumns = @JoinColumn(name = "post_id"),
        inverseJoinColumns = @JoinColumn(name = "hashtag_id")
    )
    private Set<Hashtag> hashtags = new HashSet<>();
    
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Like> likes = new HashSet<>();
    
    @Column(name = "like_count")
    private int likeCount;
    
    @Column(name = "reply_count")
    private int replyCount;
    
    @Column(name = "repost_count")
    private int repostCount;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    /**
     * Default constructor.
     */
    public Post() {
    }
    
    /**
     * Constructor with required fields.
     *
     * @param user the user who created the post
     * @param content the content of the post
     */
    public Post(User user, String content) {
        this.user = user;
        this.content = content;
    }
    
    /**
     * Full constructor.
     *
     * @param id the ID
     * @param user the user who created the post
     * @param content the content of the post
     * @param media the media URLs
     * @param isReply whether it's a reply
     * @param parent the parent post if it's a reply
     * @param replies the replies to this post
     * @param isRepost whether it's a repost
     * @param originalPost the original post if it's a repost
     * @param reposts the reposts of this post
     * @param hashtags the hashtags in this post
     * @param likes the likes on this post
     * @param likeCount the like count
     * @param replyCount the reply count
     * @param repostCount the repost count
     * @param createdAt the creation timestamp
     * @param updatedAt the update timestamp
     */
    public Post(UUID id, User user, String content, List<String> media, boolean isReply,
                Post parent, List<Post> replies, boolean isRepost, Post originalPost,
                List<Post> reposts, Set<Hashtag> hashtags, Set<Like> likes, int likeCount,
                int replyCount, int repostCount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.user = user;
        this.content = content;
        this.media = media;
        this.isReply = isReply;
        this.parent = parent;
        this.replies = replies;
        this.isRepost = isRepost;
        this.originalPost = originalPost;
        this.reposts = reposts;
        this.hashtags = hashtags;
        this.likes = likes;
        this.likeCount = likeCount;
        this.replyCount = replyCount;
        this.repostCount = repostCount;
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
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public List<String> getMedia() {
        return media;
    }
    
    public void setMedia(List<String> media) {
        this.media = media;
    }
    
    public boolean isReply() {
        return isReply;
    }
    
    public void setReply(boolean reply) {
        isReply = reply;
    }
    
    public Post getParent() {
        return parent;
    }
    
    public void setParent(Post parent) {
        this.parent = parent;
    }
    
    public List<Post> getReplies() {
        return replies;
    }
    
    public void setReplies(List<Post> replies) {
        this.replies = replies;
    }
    
    public boolean isRepost() {
        return isRepost;
    }
    
    public void setRepost(boolean repost) {
        isRepost = repost;
    }
    
    public Post getOriginalPost() {
        return originalPost;
    }
    
    public void setOriginalPost(Post originalPost) {
        this.originalPost = originalPost;
    }
    
    public List<Post> getReposts() {
        return reposts;
    }
    
    public void setReposts(List<Post> reposts) {
        this.reposts = reposts;
    }
    
    public Set<Hashtag> getHashtags() {
        return hashtags;
    }
    
    public void setHashtags(Set<Hashtag> hashtags) {
        this.hashtags = hashtags;
    }
    
    public Set<Like> getLikes() {
        return likes;
    }
    
    public void setLikes(Set<Like> likes) {
        this.likes = likes;
    }
    
    public int getLikeCount() {
        return likeCount;
    }
    
    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
    
    public int getReplyCount() {
        return replyCount;
    }
    
    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }
    
    public int getRepostCount() {
        return repostCount;
    }
    
    public void setRepostCount(int repostCount) {
        this.repostCount = repostCount;
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
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(id, post.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Post{" +
               "id=" + id +
               ", user=" + (user != null ? user.getUsername() : null) +
               ", content='" + (content != null && content.length() > 30 ? content.substring(0, 27) + "..." : content) + '\'' +
               ", isReply=" + isReply +
               ", isRepost=" + isRepost +
               ", likeCount=" + likeCount +
               ", replyCount=" + replyCount +
               ", repostCount=" + repostCount +
               '}';
    }
    
    /**
     * Builder for Post.
     */
    public static class PostBuilder {
        private UUID id;
        private User user;
        private String content;
        private List<String> media = new ArrayList<>();
        private boolean isReply;
        private Post parent;
        private List<Post> replies = new ArrayList<>();
        private boolean isRepost;
        private Post originalPost;
        private List<Post> reposts = new ArrayList<>();
        private Set<Hashtag> hashtags = new HashSet<>();
        private Set<Like> likes = new HashSet<>();
        private int likeCount;
        private int replyCount;
        private int repostCount;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        
        public PostBuilder() {
        }
        
        public PostBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        
        public PostBuilder user(User user) {
            this.user = user;
            return this;
        }
        
        public PostBuilder content(String content) {
            this.content = content;
            return this;
        }
        
        public PostBuilder media(List<String> media) {
            this.media = media;
            return this;
        }
        
        public PostBuilder isReply(boolean isReply) {
            this.isReply = isReply;
            return this;
        }
        
        public PostBuilder parent(Post parent) {
            this.parent = parent;
            return this;
        }
        
        public PostBuilder replies(List<Post> replies) {
            this.replies = replies;
            return this;
        }
        
        public PostBuilder isRepost(boolean isRepost) {
            this.isRepost = isRepost;
            return this;
        }
        
        public PostBuilder originalPost(Post originalPost) {
            this.originalPost = originalPost;
            return this;
        }
        
        public PostBuilder reposts(List<Post> reposts) {
            this.reposts = reposts;
            return this;
        }
        
        public PostBuilder hashtags(Set<Hashtag> hashtags) {
            this.hashtags = hashtags;
            return this;
        }
        
        public PostBuilder likes(Set<Like> likes) {
            this.likes = likes;
            return this;
        }
        
        public PostBuilder likeCount(int likeCount) {
            this.likeCount = likeCount;
            return this;
        }
        
        public PostBuilder replyCount(int replyCount) {
            this.replyCount = replyCount;
            return this;
        }
        
        public PostBuilder repostCount(int repostCount) {
            this.repostCount = repostCount;
            return this;
        }
        
        public PostBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        
        public PostBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }
        
        public Post build() {
            return new Post(
                id, user, content, media, isReply, parent, replies, isRepost,
                originalPost, reposts, hashtags, likes, likeCount, replyCount, repostCount,
                createdAt, updatedAt
            );
        }
    }
    
    public static PostBuilder builder() {
        return new PostBuilder();
    }
} 