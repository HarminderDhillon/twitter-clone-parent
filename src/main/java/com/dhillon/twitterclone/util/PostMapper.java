package com.dhillon.twitterclone.util;

import com.dhillon.twitterclone.dto.PostDto;
import com.dhillon.twitterclone.entity.Hashtag;
import com.dhillon.twitterclone.entity.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for mapping between Post entities and DTOs.
 */
public class PostMapper {
    
    /**
     * Convert a Post entity to a PostDto.
     *
     * @param post the post entity
     * @return the post DTO
     */
    public static PostDto toDto(Post post) {
        if (post == null) {
            return null;
        }
        
        // Convert hashtags to strings
        List<String> hashtagNames = post.getHashtags().stream()
                .map(Hashtag::getName)
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
            false, // liked - would need to be set based on current user
            false, // retweeted - would need to be set based on current user
            post.getCreatedAt(),
            hashtagNames,
            new ArrayList<>() // mentions - would need to be extracted
        );
    }
    
    /**
     * Convert a list of Post entities to a list of PostDtos.
     *
     * @param posts the list of post entities
     * @return the list of post DTOs
     */
    public static List<PostDto> toDtoList(List<Post> posts) {
        if (posts == null) {
            return new ArrayList<>();
        }
        
        return posts.stream()
                .map(PostMapper::toDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Convert a PostDto to a Post entity.
     * Note: This does not set the user, parent, or original post references.
     * Those must be set separately.
     *
     * @param dto the post DTO
     * @return the post entity
     */
    public static Post toEntity(PostDto dto) {
        if (dto == null) {
            return null;
        }
        
        Post post = new Post();
        post.setId(dto.id());
        post.setContent(dto.content());
        post.setMedia(dto.media());
        
        return post;
    }
    
    /**
     * Update a Post entity with data from a PostDto.
     * Only updates fields that are allowed to be updated.
     *
     * @param post the post entity to update
     * @param dto the post DTO with updated data
     * @return the updated post entity
     */
    public static Post updateEntity(Post post, PostDto dto) {
        if (post == null || dto == null) {
            return post;
        }
        
        // Only content can be updated
        if (dto.content() != null) {
            post.setContent(dto.content());
        }
        
        return post;
    }
} 