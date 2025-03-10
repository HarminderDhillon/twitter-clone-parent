package com.dhillon.twitterclone.dto;

import java.time.LocalDateTime;

/**
 * Data transfer object representing an API error response.
 * 
 * @param status HTTP status code
 * @param error Error type
 * @param message Detailed error message
 * @param timestamp Timestamp when the error occurred
 */
public record ApiError(
    int status,
    String error,
    String message,
    LocalDateTime timestamp
) {} 