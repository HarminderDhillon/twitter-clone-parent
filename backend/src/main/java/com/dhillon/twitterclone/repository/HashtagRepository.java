package com.dhillon.twitterclone.repository;

import com.dhillon.twitterclone.entity.Hashtag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Hashtag entity operations.
 */
@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, UUID> {
    
    /**
     * Find a hashtag by name.
     *
     * @param name the name of the hashtag
     * @return optional hashtag if found
     */
    Optional<Hashtag> findByName(String name);
    
    /**
     * Check if a hashtag exists by name.
     *
     * @param name the name of the hashtag
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);
    
    /**
     * Find trending hashtags based on post count.
     *
     * @param pageable pagination information
     * @return page of trending hashtags
     */
    Page<Hashtag> findAllByOrderByPostCountDesc(Pageable pageable);
    
    /**
     * Search for hashtags by name.
     *
     * @param query the search query
     * @param pageable pagination information
     * @return page of matching hashtags
     */
    @Query("SELECT h FROM Hashtag h WHERE LOWER(h.name) LIKE LOWER(CONCAT('%', :query, '%')) ORDER BY h.postCount DESC")
    Page<Hashtag> searchHashtags(@Param("query") String query, Pageable pageable);
} 