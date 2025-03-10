package com.dhillon.twitterclone.exception;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an API error response.
 */
public record ApiError(
    LocalDateTime timestamp,
    int status,
    String error,
    String message,
    String path,
    List<String> details
) {
    /**
     * Default constructor.
     */
    public ApiError() {
        this(LocalDateTime.now(), 0, null, null, null, new ArrayList<>());
    }
    
    /**
     * Constructor with basic error information.
     *
     * @param status HTTP status code
     * @param error error type
     * @param message error message
     * @param path request path
     */
    public ApiError(int status, String error, String message, String path) {
        this(LocalDateTime.now(), status, error, message, path, new ArrayList<>());
    }
    
    /**
     * Adds a detail message to the error.
     *
     * @param detail the detail message to add
     * @return the updated ApiError instance
     */
    public ApiError addDetail(String detail) {
        List<String> newDetails = new ArrayList<>(this.details);
        newDetails.add(detail);
        return new ApiError(this.timestamp, this.status, this.error, this.message, this.path, newDetails);
    }
} 