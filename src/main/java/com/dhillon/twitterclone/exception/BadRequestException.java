package com.dhillon.twitterclone.exception;

/**
 * Exception thrown when a request is invalid.
 */
public class BadRequestException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Constructs a new bad request exception with the specified detail message.
     *
     * @param message the detail message
     */
    public BadRequestException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new bad request exception with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause
     */
    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
} 