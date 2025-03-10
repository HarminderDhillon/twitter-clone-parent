package com.dhillon.twitterclone.service.impl;

import com.dhillon.twitterclone.entity.Hashtag;
import com.dhillon.twitterclone.entity.Post;
import com.dhillon.twitterclone.exception.ResourceNotFoundException;
import com.dhillon.twitterclone.repository.HashtagRepository;
import com.dhillon.twitterclone.repository.PostRepository;
import com.dhillon.twitterclone.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Implementation of the PostService interface.
 */
@Service
public class PostServiceImpl implements PostService {
    
    private final PostRepository postRepository;
    private final HashtagRepository hashtagRepository;
    
    // Pattern to match hashtags in post content
    private static final Pattern HASHTAG_PATTERN = Pattern.compile("#(\\w+)");
    
    /**
     * Constructor with dependencies.
     *
     * @param postRepository the post repository
     * @param hashtagRepository the hashtag repository
     */
    public PostServiceImpl(PostRepository postRepository, HashtagRepository hashtagRepository) {
        this.postRepository = postRepository;
        this.hashtagRepository = hashtagRepository;
    }
    
    @Override
    public Optional<Post> findById(UUID id) {
        return postRepository.findById(id);
    }
    
    @Override
    @Transactional
    public Post createPost(Post post) {
        // Extract and process hashtags
        processHashtags(post);
        
        return postRepository.save(post);
    }
    
    @Override
    @Transactional
    public Post updatePost(UUID id, Post updatedPost) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        
        // Only content can be updated
        if (updatedPost.getContent() != null) {
            existingPost.setContent(updatedPost.getContent());
            
            // Re-process hashtags
            existingPost.getHashtags().clear();
            processHashtags(existingPost);
        }
        
        return postRepository.save(existingPost);
    }
    
    @Override
    @Transactional
    public void deletePost(UUID id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        
        postRepository.delete(post);
    }
    
    @Override
    public Page<Post> getUserTimeline(UUID userId, Pageable pageable) {
        return postRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }
    
    @Override
    public Page<Post> getHomeTimeline(UUID userId, Pageable pageable) {
        return postRepository.findHomeTimeline(userId, pageable);
    }
    
    @Override
    public Page<Post> searchPosts(String query, Pageable pageable) {
        return postRepository.searchPosts(query, pageable);
    }
    
    @Override
    public Page<Post> getTrendingPosts(Pageable pageable) {
        return postRepository.findTrendingPosts(pageable);
    }
    
    @Override
    public Page<Post> getPostsByHashtag(String hashtag, Pageable pageable) {
        return postRepository.findByHashtagName(hashtag, pageable);
    }
    
    @Override
    public Page<Post> getReplies(UUID postId, Pageable pageable) {
        return postRepository.findByParentIdOrderByCreatedAtDesc(postId, pageable);
    }
    
    @Override
    @Transactional
    public Post createReply(UUID parentId, Post reply) {
        Post parentPost = postRepository.findById(parentId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", parentId));
        
        reply.setReply(true);
        reply.setParent(parentPost);
        
        // Extract and process hashtags
        processHashtags(reply);
        
        Post savedReply = postRepository.save(reply);
        
        // Update parent post reply count
        parentPost.setReplyCount(parentPost.getReplyCount() + 1);
        postRepository.save(parentPost);
        
        return savedReply;
    }
    
    @Override
    @Transactional
    public Post createRepost(UUID originalPostId, Post repost) {
        Post originalPost = postRepository.findById(originalPostId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", originalPostId));
        
        repost.setRepost(true);
        repost.setOriginalPost(originalPost);
        
        // Extract and process hashtags
        processHashtags(repost);
        
        Post savedRepost = postRepository.save(repost);
        
        // Update original post repost count
        originalPost.setRepostCount(originalPost.getRepostCount() + 1);
        postRepository.save(originalPost);
        
        return savedRepost;
    }
    
    @Override
    public List<String> extractHashtags(String content) {
        if (content == null || content.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<String> hashtags = new ArrayList<>();
        Matcher matcher = HASHTAG_PATTERN.matcher(content);
        
        while (matcher.find()) {
            hashtags.add(matcher.group(1).toLowerCase());
        }
        
        return hashtags;
    }
    
    /**
     * Process hashtags in a post.
     * Extracts hashtags from content, finds or creates Hashtag entities,
     * and associates them with the post.
     *
     * @param post the post to process
     */
    private void processHashtags(Post post) {
        if (post.getContent() == null) {
            return;
        }
        
        List<String> hashtagNames = extractHashtags(post.getContent());
        Set<Hashtag> hashtags = new HashSet<>();
        
        for (String name : hashtagNames) {
            // Find existing hashtag or create a new one
            Hashtag hashtag = hashtagRepository.findByName(name)
                    .orElseGet(() -> {
                        Hashtag newHashtag = new Hashtag();
                        newHashtag.setName(name);
                        return hashtagRepository.save(newHashtag);
                    });
            
            hashtags.add(hashtag);
        }
        
        post.setHashtags(hashtags);
    }
} 