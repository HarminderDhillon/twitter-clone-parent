package com.dhillon.twitterclone.exception;

/**
 * Exception thrown when the server understood the request but refuses to authorize it.
 */
public class ForbiddenException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Constructs a new forbidden exception with the specified detail message.
     *
     * @param message the detail message
     */
    public ForbiddenException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new forbidden exception with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause
     */
    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }
} 