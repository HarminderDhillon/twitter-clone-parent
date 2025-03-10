package com.dhillon.twitterclone.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Data Transfer Object for Post")
public record PostDto(
    @Schema(description = "Unique identifier of the post")
    UUID id,

    @Schema(description = "User ID of the post author")
    UUID userId,

    @Schema(description = "Username of the post author")
    String username,

    @Schema(description = "Display name of the post author")
    String displayName,

    @Schema(description = "Profile image URL of the post author")
    String profileImage,

    @NotBlank(message = "Content cannot be empty")
    @Size(max = 280, message = "Content cannot exceed 280 characters")
    @Schema(description = "Content of the post", maxLength = 280)
    String content,

    @Schema(description = "List of media attachments")
    List<String> media,

    @Schema(description = "Number of likes for the post")
    int likeCount,

    @Schema(description = "Number of retweets for the post")
    int retweetCount,

    @Schema(description = "Number of replies for the post")
    int replyCount,

    @Schema(description = "Whether the current user has liked the post")
    boolean liked,

    @Schema(description = "Whether the current user has retweeted the post")
    boolean retweeted,

    @Schema(description = "Timestamp when the post was created")
    LocalDateTime createdAt,

    @Schema(description = "List of hashtags in the post")
    List<String> hashtags,

    @Schema(description = "List of mentioned users in the post")
    List<String> mentions
) {
    // Compact constructor to ensure lists are never null
    public PostDto {
        media = media != null ? media : new ArrayList<>();
        hashtags = hashtags != null ? hashtags : new ArrayList<>();
        mentions = mentions != null ? mentions : new ArrayList<>();
    }
    
    // Factory method for creating a post with just content
    public static PostDto ofContent(String content) {
        return new PostDto(
            null, null, null, null, null, 
            content, 
            new ArrayList<>(), 0, 0, 0, false, false, 
            LocalDateTime.now(), 
            new ArrayList<>(), new ArrayList<>()
        );
    }
}