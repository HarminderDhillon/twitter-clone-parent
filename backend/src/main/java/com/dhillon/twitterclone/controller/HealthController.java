package com.dhillon.twitterclone.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller that provides health check endpoints for the application.
 */
@RestController
@RequestMapping("/api")
public class HealthController {
    private static final Logger logger = LoggerFactory.getLogger(HealthController.class);

    /**
     * Basic health check endpoint that returns a 200 OK response.
     * Used by health checks to verify the application is running.
     *
     * @return A simple status response
     */
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        logger.info("Health check endpoint called");
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Twitter Clone API");
        return ResponseEntity.ok(response);
    }

    /**
     * Another health check endpoint at /health for clients that expect this path
     *
     * @return A simple status response
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheckAlt() {
        logger.info("Health check alt endpoint called");
        return healthCheck();
    }
} 