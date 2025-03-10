package com.dhillon.twitterclone.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.List;

/**
 * Global exception handler for REST endpoints.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * Handle ResourceNotFoundException.
     *
     * @param ex the exception
     * @param request the HTTP request
     * @return the error response
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        return new ApiError(
            HttpStatus.NOT_FOUND.value(),
            "Not Found",
            ex.getMessage(),
            request.getRequestURI()
        );
    }
    
    /**
     * Handle BadRequestException.
     *
     * @param ex the exception
     * @param request the HTTP request
     * @return the error response
     */
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequestException(BadRequestException ex, HttpServletRequest request) {
        return new ApiError(
            HttpStatus.BAD_REQUEST.value(),
            "Bad Request",
            ex.getMessage(),
            request.getRequestURI()
        );
    }
    
    /**
     * Handle UnauthorizedException.
     *
     * @param ex the exception
     * @param request the HTTP request
     * @return the error response
     */
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiError handleUnauthorizedException(UnauthorizedException ex, HttpServletRequest request) {
        return new ApiError(
            HttpStatus.UNAUTHORIZED.value(),
            "Unauthorized",
            ex.getMessage(),
            request.getRequestURI()
        );
    }
    
    /**
     * Handle ForbiddenException.
     *
     * @param ex the exception
     * @param request the HTTP request
     * @return the error response
     */
    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleForbiddenException(ForbiddenException ex, HttpServletRequest request) {
        return new ApiError(
            HttpStatus.FORBIDDEN.value(),
            "Forbidden",
            ex.getMessage(),
            request.getRequestURI()
        );
    }
    
    /**
     * Handle Spring Security AccessDeniedException.
     *
     * @param ex the exception
     * @param request the HTTP request
     * @return the error response
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        return new ApiError(
            HttpStatus.FORBIDDEN.value(),
            "Forbidden",
            "Access denied: " + ex.getMessage(),
            request.getRequestURI()
        );
    }
    
    /**
     * Handle Spring Security AuthenticationException.
     *
     * @param ex the exception
     * @param request the HTTP request
     * @return the error response
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiError handleAuthenticationException(AuthenticationException ex, HttpServletRequest request) {
        String message = "Authentication failed";
        if (ex instanceof BadCredentialsException) {
            message = "Invalid username or password";
        }
        return new ApiError(
            HttpStatus.UNAUTHORIZED.value(),
            "Unauthorized",
            message,
            request.getRequestURI()
        );
    }
    
    /**
     * Handle validation exceptions.
     *
     * @param ex the exception
     * @param request the HTTP request
     * @return the error response
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(
            HttpStatus.BAD_REQUEST.value(),
            "Validation Error",
            "Input validation failed",
            request.getRequestURI()
        );
        
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            apiError.addDetail(fieldName + ": " + errorMessage);
        });
        
        return apiError;
    }
    
    /**
     * Handle binding exceptions.
     *
     * @param ex the exception
     * @param request the HTTP request
     * @return the error response
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBindException(BindException ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(
            HttpStatus.BAD_REQUEST.value(),
            "Binding Error",
            "Parameter binding failed",
            request.getRequestURI()
        );
        
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            apiError.addDetail(fieldName + ": " + errorMessage);
        });
        
        return apiError;
    }
    
    /**
     * Handle constraint violation exceptions.
     *
     * @param ex the exception
     * @param request the HTTP request
     * @return the error response
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(
            HttpStatus.BAD_REQUEST.value(),
            "Validation Error",
            "Constraint violation",
            request.getRequestURI()
        );
        
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            apiError.addDetail(violation.getPropertyPath() + ": " + violation.getMessage());
        }
        
        return apiError;
    }
    
    /**
     * Handle missing request parameter exceptions.
     *
     * @param ex the exception
     * @param request the HTTP request
     * @return the error response
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMissingParams(MissingServletRequestParameterException ex, HttpServletRequest request) {
        return new ApiError(
            HttpStatus.BAD_REQUEST.value(),
            "Bad Request",
            "Required parameter '" + ex.getParameterName() + "' is missing",
            request.getRequestURI()
        );
    }
    
    /**
     * Handle method argument type mismatch exceptions.
     *
     * @param ex the exception
     * @param request the HTTP request
     * @return the error response
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        return new ApiError(
            HttpStatus.BAD_REQUEST.value(),
            "Bad Request",
            "Parameter '" + ex.getName() + "' has invalid value '" + ex.getValue() + "'. Required type: " + ex.getRequiredType().getSimpleName(),
            request.getRequestURI()
        );
    }
    
    /**
     * Handle HTTP message not readable exceptions.
     *
     * @param ex the exception
     * @param request the HTTP request
     * @return the error response
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
        return new ApiError(
            HttpStatus.BAD_REQUEST.value(),
            "Bad Request",
            "Invalid request body: " + ex.getMessage(),
            request.getRequestURI()
        );
    }
    
    /**
     * Handle data integrity violation exceptions.
     *
     * @param ex the exception
     * @param request the HTTP request
     * @return the error response
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataIntegrityViolation(DataIntegrityViolationException ex, HttpServletRequest request) {
        String message = ex.getMessage();
        if (message.contains("constraint")) {
            message = "Data integrity violation: Constraint violation occurred";
        }
        return new ApiError(
            HttpStatus.CONFLICT.value(),
            "Conflict",
            message,
            request.getRequestURI()
        );
    }
    
    /**
     * Handle max upload size exceeded exceptions.
     *
     * @param ex the exception
     * @param request the HTTP request
     * @return the error response
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMaxUploadSizeExceeded(MaxUploadSizeExceededException ex, HttpServletRequest request) {
        return new ApiError(
            HttpStatus.BAD_REQUEST.value(),
            "Bad Request",
            "File upload size exceeds the maximum allowed limit",
            request.getRequestURI()
        );
    }
    
    /**
     * Handle all other exceptions.
     *
     * @param ex the exception
     * @param request the HTTP request
     * @return the error response
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleAllUncaughtException(Exception ex, HttpServletRequest request) {
        return new ApiError(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error",
            "An unexpected error occurred: " + ex.getMessage(),
            request.getRequestURI()
        );
    }
    
    /**
     * Create a response entity with status and body.
     *
     * @param status the HTTP status
     * @param body the response body
     * @return the response entity
     */
    private ResponseEntity<Object> createResponseEntity(HttpStatus status, Object body) {
        return new ResponseEntity<>(body, status);
    }
} 