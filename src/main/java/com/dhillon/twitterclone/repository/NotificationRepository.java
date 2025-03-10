package com.dhillon.twitterclone.repository;

import com.dhillon.twitterclone.entity.Notification;
import com.dhillon.twitterclone.entity.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository for Notification entity operations.
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    
    /**
     * Find all notifications for a user ordered by creation date descending.
     *
     * @param userId the ID of the user
     * @param pageable pagination information
     * @return page of notifications
     */
    Page<Notification> findByUserIdOrderByCreatedAtDesc(UUID userId, Pageable pageable);
    
    /**
     * Find all unread notifications for a user.
     *
     * @param userId the ID of the user
     * @param pageable pagination information
     * @return page of unread notifications
     */
    Page<Notification> findByUserIdAndReadFalseOrderByCreatedAtDesc(UUID userId, Pageable pageable);
    
    /**
     * Count unread notifications for a user.
     *
     * @param userId the ID of the user
     * @return count of unread notifications
     */
    long countByUserIdAndReadFalse(UUID userId);
    
    /**
     * Find notifications by type for a user.
     *
     * @param userId the ID of the user
     * @param type the notification type
     * @param pageable pagination information
     * @return page of notifications
     */
    Page<Notification> findByUserIdAndTypeOrderByCreatedAtDesc(UUID userId, NotificationType type, Pageable pageable);
    
    /**
     * Mark all notifications for a user as read.
     *
     * @param userId the ID of the user
     * @return number of rows affected
     */
    @Modifying
    @Query("UPDATE Notification n SET n.read = true WHERE n.user.id = :userId AND n.read = false")
    int markAllAsRead(@Param("userId") UUID userId);
} 