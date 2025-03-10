package com.dhillon.twitterclone.controller;

import com.dhillon.twitterclone.dto.PostDto;
import com.dhillon.twitterclone.entity.Post;
import com.dhillon.twitterclone.entity.User;
import com.dhillon.twitterclone.service.PostService;
import com.dhillon.twitterclone.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * REST controller for post operations.
 */
@RestController
@RequestMapping("/posts")
@Tag(name = "Post", description = "Post management APIs")
public class PostController {
    
    private final PostService postService;
    private final UserService userService;
    
    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }
    
    @GetMapping
    @Operation(summary = "Get all posts", description = "Retrieve a list of all posts")
    @ApiResponse(responseCode = "200", description = "Posts retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostDto.class)))
    public ResponseEntity<List<PostDto>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postService.searchPosts("", pageable);
        List<PostDto> postDTOs = posts.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(postDTOs);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get post by ID", description = "Retrieve a specific post by its ID")
    @ApiResponse(responseCode = "200", description = "Post retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Post not found")
    public ResponseEntity<PostDto> getPostById(
            @Parameter(description = "ID of the post to retrieve", required = true)
            @PathVariable UUID id) {
        return postService.findById(id)
                .map(post -> ResponseEntity.ok(convertToDto(post)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get posts by user ID", description = "Retrieve all posts from a specific user")
    @ApiResponse(responseCode = "200", description = "Posts retrieved successfully")
    public ResponseEntity<List<PostDto>> getPostsByUserId(
            @Parameter(description = "ID of the user", required = true)
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postService.getUserTimeline(userId, pageable);
        List<PostDto> postDTOs = posts.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(postDTOs);
    }
    
    @PostMapping
    @Operation(summary = "Create post", description = "Create a new post")
    @ApiResponse(responseCode = "201", description = "Post created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid post data")
    public ResponseEntity<PostDto> createPost(
            @Parameter(description = "Post data for creating a new post", required = true)
            @RequestBody PostDto postDto) {
        Post post = convertToEntity(postDto);
        Post createdPost = postService.createPost(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(createdPost));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update post", description = "Update an existing post")
    @ApiResponse(responseCode = "200", description = "Post updated successfully")
    @ApiResponse(responseCode = "404", description = "Post not found")
    public ResponseEntity<PostDto> updatePost(
            @Parameter(description = "ID of the post to update", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Updated post data", required = true)
            @RequestBody PostDto postDto) {
        Post post = convertToEntity(postDto);
        Post updatedPost = postService.updatePost(id, post);
        return ResponseEntity.ok(convertToDto(updatedPost));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete post", description = "Delete an existing post")
    @ApiResponse(responseCode = "204", description = "Post deleted successfully")
    @ApiResponse(responseCode = "404", description = "Post not found")
    public ResponseEntity<Void> deletePost(
            @Parameter(description = "ID of the post to delete", required = true)
            @PathVariable UUID id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{parentId}/reply")
    @Operation(summary = "Create reply", description = "Create a reply to an existing post")
    @ApiResponse(responseCode = "201", description = "Reply created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid reply data")
    public ResponseEntity<PostDto> createReply(
            @Parameter(description = "ID of the parent post", required = true)
            @PathVariable UUID parentId,
            @Parameter(description = "Reply data", required = true)
            @RequestBody PostDto replyDto) {
        Post reply = convertToEntity(replyDto);
        Post createdReply = postService.createReply(parentId, reply);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(createdReply));
    }
    
    @PostMapping("/{originalPostId}/repost")
    @Operation(summary = "Create repost", description = "Repost an existing post")
    @ApiResponse(responseCode = "201", description = "Repost created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid repost data")
    public ResponseEntity<PostDto> createRepost(
            @Parameter(description = "ID of the original post", required = true)
            @PathVariable UUID originalPostId,
            @Parameter(description = "Repost data", required = true)
            @RequestBody PostDto repostDto) {
        Post repost = convertToEntity(repostDto);
        Post createdRepost = postService.createRepost(originalPostId, repost);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(createdRepost));
    }
    
    /**
     * Converts Post entity to PostDto
     */
    private PostDto convertToDto(Post post) {
        // Convert hashtags to strings
        List<String> hashtagNames = post.getHashtags().stream()
                .map(hashtag -> hashtag.getName())
                .collect(Collectors.toList());
        
        return new PostDto(
            post.getId(),
            post.getUser().getId(),
            post.getUser().getUsername(),
            post.getUser().getDisplayName() != null ? 
                post.getUser().getDisplayName() : post.getUser().getUsername(),
            post.getUser().getProfileImage(),
            post.getContent(),
            post.getMedia(),
            post.getLikeCount(),
            post.getRepostCount(),
            post.getReplyCount(),
            false, // liked status would need to be set based on current user
            false, // retweeted status would need to be set based on current user
            post.getCreatedAt(),
            hashtagNames,
            new ArrayList<>() // mentions would need to be extracted
        );
    }
    
    /**
     * Converts PostDto to Post entity
     */
    private Post convertToEntity(PostDto postDto) {
        Post post = new Post();
        post.setId(postDto.id());
        post.setContent(postDto.content());
        post.setMedia(postDto.media());
        
        // Set user
        if (postDto.userId() != null) {
            Optional<User> userOpt = userService.findById(postDto.userId());
            userOpt.ifPresent(post::setUser);
        }
        
        return post;
    }
} 