# Twitter Clone Application Specification (Spring Boot Backend)

## 1. Overview

This document outlines the specifications for a Twitter clone web application with a Spring Boot backend. The application will allow users to create accounts, post short messages, follow other users, interact with posts through likes and replies, and explore content through various discovery mechanisms.

## 2. Core Features

### 2.1 User Management
- **Registration**: Email, username, password, profile picture upload
- **Social Login**: OAuth integration for Google, Twitter, GitHub
- **Authentication**: JWT-based authentication system with refresh tokens
- **Profiles**: Customizable bio, profile picture, header image, location, website
- **Following System**: Follow/unfollow users
- **Verification Badges**: For notable accounts (optional feature)
- **Password Reset**: Email-based OTP verification

### 2.2 Posts ("Tweets")
- **Creation**: Text posts up to 280 characters
- **Editing**: Allow users to edit tweets within a time window (5 minutes)
- **Media Attachments**: Support for images (up to 4 per post)
- **Hashtags**: Automatic detection and indexing
- **Mentions**: Using @ symbol to mention users
- **Post Types**: Regular posts, replies, reposts ("retweets"), quote retweets
- **Bookmarking**: Save posts for later reference

### 2.3 Timeline & Feeds
- **Home Timeline**: Posts from followed users in reverse chronological order
- **Explore/Discover**: Trending posts, topics, and suggested users
- **User Timeline**: All posts from a specific user
- **Notifications**: Likes, reposts, mentions, follows, and replies

### 2.4 Engagement
- **Likes**: Users can like/unlike posts
- **Replies**: Threaded conversation support
- **Reposts**: Share others' posts to followers
- **Bookmarks**: Save posts for later viewing

### 2.5 Search
- **Users**: Find users by username or display name
- **Content**: Search for posts containing specific text
- **Hashtags**: Find posts with specific hashtags
- **Filters**: Recent, most liked, most retweeted

### 2.6 Direct Messaging (DMs)
- **One-on-one private messaging**: Send private messages to other users
- **Group chats**: Create conversations with multiple users
- **Media sharing**: Send images and other media in DMs
- **Message reactions**: Support for emoji reactions
- **Read receipts & typing indicators**: Real-time chat status

### 2.7 Admin Dashboard
- **User Management**: Ban or suspend accounts
- **Content Moderation**: Handle reported tweets and content
- **Analytics**: Track platform usage, engagement metrics
- **System Health**: Monitor performance and manage resources

## 3. Technical Architecture

### 3.1 Backend (Spring Boot)
- **Framework**: Spring Boot 3.x
- **Database**: PostgreSQL for relational data
- **ORM**: Spring Data JPA with Hibernate
- **Cache**: Redis for session management, timeline caching, and real-time events
- **Search Engine**: Elasticsearch for advanced search capabilities
- **File Storage**: AWS S3 or Cloudinary for media storage
- **Queue System**: Spring Batch with RabbitMQ for background tasks
- **Real-time Communication**: WebSockets with Spring WebFlux and STOMP protocol
- **API Documentation**: SpringDoc with OpenAPI (Swagger)

### 3.2 Authentication & Security
- **Authentication**: JWT tokens with refresh mechanism using Spring Security
- **Password Security**: BCrypt password encoder with Spring Security
- **Rate Limiting**: Bucket4j or Spring Cloud Gateway for rate limiting
- **Input Validation**: Bean Validation (JSR 380) with custom validators
- **XSS Protection**: Content sanitization with OWASP HTML Sanitizer
- **CSRF Protection**: Spring Security CSRF protection

### 3.3 API Endpoints (Including WebSocket Endpoints)

#### User Management
- `POST /api/users` - Register new user
- `POST /api/auth/login` - User login
- `GET /api/users/:username` - Get user profile
- `PUT /api/users/:username` - Update user profile
- `GET /api/users/:username/followers` - Get user followers
- `GET /api/users/:username/following` - Get users being followed
- `POST /api/users/:username/follow` - Follow user
- `DELETE /api/users/:username/follow` - Unfollow user

#### Posts
- `POST /api/posts` - Create new post
- `GET /api/posts/:id` - Get specific post
- `DELETE /api/posts/:id` - Delete post
- `GET /api/posts/:id/replies` - Get replies to post
- `POST /api/posts/:id/replies` - Reply to post
- `POST /api/posts/:id/like` - Like a post
- `DELETE /api/posts/:id/like` - Unlike a post
- `POST /api/posts/:id/repost` - Repost a post
- `DELETE /api/posts/:id/repost` - Undo repost

#### Timelines
- `GET /api/timeline` - Get home timeline
- `GET /api/users/:username/posts` - Get user timeline
- `GET /api/explore` - Get explore/discover feed
- `GET /api/hashtags/:tag` - Get posts with hashtag

#### Search
- `GET /api/search/users?q=:query` - Search users
- `GET /api/search/posts?q=:query` - Search posts
- `GET /api/search/hashtags?q=:query` - Search hashtags

#### Notifications
- `GET /api/notifications` - Get user notifications
- `PUT /api/notifications/:id/read` - Mark notification as read
- `PUT /api/notifications/read-all` - Mark all notifications as read

#### Direct Messages
- `POST /api/messages` - Send a direct message
- `GET /api/messages/conversations` - Get all conversations
- `GET /api/messages/conversations/:id` - Get messages from a conversation
- `POST /api/messages/conversations` - Create a new conversation (group or individual)
- `POST /api/messages/:id/reaction` - React to a message

#### WebSocket Endpoints
- `/ws/connect` - WebSocket connection endpoint
- `/topic/timeline` - Timeline updates subscription
- `/topic/notifications` - Real-time notifications
- `/topic/messages` - Direct message updates
- `/topic/typing/:conversationId` - Typing indicators for conversations

### 3.4 Data Models (Java Entities)

#### User Entity
```java
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
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "user")
    private List<Post> posts;
    
    @OneToMany(mappedBy = "follower")
    private List<Follow> following;
    
    @OneToMany(mappedBy = "following")
    private List<Follow> followers;
}
```

#### Post Entity
```java
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
    
    @ElementCollection
    @CollectionTable(name = "post_media", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "media_url")
    private List<String> media;
    
    private boolean isReply;
    
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Post parent;
    
    @OneToMany(mappedBy = "parent")
    private List<Post> replies;
    
    private boolean isRepost;
    
    @ManyToOne
    @JoinColumn(name = "original_post_id")
    private Post originalPost;
    
    @OneToMany(mappedBy = "originalPost")
    private List<Post> reposts;
    
    @ManyToMany
    @JoinTable(
        name = "post_hashtags",
        joinColumns = @JoinColumn(name = "post_id"),
        inverseJoinColumns = @JoinColumn(name = "hashtag_id")
    )
    private Set<Hashtag> hashtags;
    
    @OneToMany(mappedBy = "post")
    private Set<Like> likes;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
```

#### Follow Entity
```java
@Entity
@Table(name = "follows", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"follower_id", "following_id"}))
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;
    
    @ManyToOne
    @JoinColumn(name = "following_id", nullable = false)
    private User following;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
}
```

#### Like Entity
```java
@Entity
@Table(name = "likes",
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "post_id"}))
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
}
```

#### Notification Entity
```java
@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Enumerated(EnumType.STRING)
    private NotificationType type;
    
    @ManyToOne
    @JoinColumn(name = "actor_id", nullable = false)
    private User actor;
    
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
    
    private boolean read;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
}

public enum NotificationType {
    LIKE, REPLY, REPOST, FOLLOW, MENTION
}
```

#### Hashtag Entity
```java
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
    private Set<Post> posts;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
```

## 4. Frontend (Optional Specification)

### 4.1 Technologies
- **Framework**: React.js with TypeScript
- **State Management**: Redux or Context API
- **Styling**: TailwindCSS or Styled Components
- **Routing**: React Router
- **API Client**: Axios

### 4.2 Key Views
- Login/Registration
- Home Timeline
- User Profile
- Single Post with Replies
- Explore Page
- Notifications
- Search Results
- Settings

## 5. Deployment & DevOps

### 5.1 Infrastructure
- **Hosting**: AWS (EC2, Lambda for serverless tasks)
- **Database Hosting**: AWS RDS for PostgreSQL
- **Static Assets**: AWS S3 for frontend assets
- **Container**: Docker with Docker Compose for development
- **Orchestration**: Kubernetes for production
- **CI/CD**: GitHub Actions or GitLab CI
- **Monitoring**: Prometheus with Grafana dashboards
- **Error Tracking**: Sentry for frontend and backend error tracking
- **Logging**: ELK Stack (Elasticsearch, Logstash, Kibana)
- **Load Balancing**: Nginx + Spring Boot embedded Tomcat

### 5.2 Environments
- Development
- Staging
- Production

## 6. Performance Considerations

### 6.1 Scaling Strategies
- Horizontal scaling for API servers
- Database read replicas
- Caching layers for timelines and popular content
- CDN for media content
- Sharding for database beyond certain scale

### 6.2 Optimizations
- Pagination for all list endpoints
- Lazy loading for media
- Efficient timeline generation algorithms
- Background processing for intensive operations

## 7. Future Enhancements (Post-MVP)

- Video upload support and live streaming
- Polls in posts
- Lists (curated groups of users)
- Trends based on location
- Advanced analytics for user engagement
- Scheduled posts
- Third-party API integrations
- Mobile applications (React Native or Flutter)
- Multi-language support
- AI-based content moderation (detect hate speech, spam, fake news)
- Monetization features (subscriptions, promoted tweets, ads)
- Blockchain-based tweet verification to prevent misinformation

## 8. Implementation Timeline

### Phase 1: Core Backend (4 weeks)
- Set up Spring Boot project structure with Maven/Gradle
- Configure Spring Security with JWT authentication
- Implement JPA entities and repositories
- Create RESTful controllers for core functionality
- Develop service layer for business logic
- Build initial timeline algorithms

### Phase 2: Advanced Features (3 weeks)
- Implement engagement features (likes, replies, reposts)
- Add hashtag functionality
- Create notification system
- Develop search capabilities

### Phase 3: Frontend (4 weeks)
- Build user interface components
- Implement state management
- Connect to backend APIs
- Optimize for responsiveness

### Phase 4: Testing & Refinement (2 weeks)
- End-to-end testing
- Performance optimization
- Security auditing
- User acceptance testing

### Phase 5: Deployment (1 week)
- Set up production environment
- Deploy application
- Monitor performance
- Address post-launch issues

## 9. Metrics & Analytics

- User growth and retention
- Post engagement rates
- Timeline performance
- API response times
- Error rates
- Server utilization

## 10. Compliance & Legal

- GDPR compliance for user data
- Terms of service documentation
- Privacy policy
- Content moderation guidelines and implementation
- Copyright policy for user-generated content
