package com.dhillon.twitterclone.exception;

/**
 * Exception thrown when authentication is required and has failed or has not been provided.
 */
public class UnauthorizedException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Constructs a new unauthorized exception with the specified detail message.
     *
     * @param message the detail message
     */
    public UnauthorizedException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new unauthorized exception with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause
     */
    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
} 