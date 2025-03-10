package com.dhillon.twitterclone.e2e;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * E2E tests for the User API endpoints.
 * Tests are executed in order to build on each other.
 */
@TestMethodOrder(OrderAnnotation.class)
public class UserApiE2ETest extends E2EBaseTest {

    private static final String TEST_USERNAME = "e2etestuser";
    private static final String TEST_EMAIL = "e2etest@example.com";
    private static final String TEST_PASSWORD = "securePassword123";
    
    /**
     * Test that we can check if a username is available
     */
    @Test
    @Order(1)
    public void checkUsernameAvailability() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/users/check-username?username=" + TEST_USERNAME)
        .then()
            .statusCode(200)
            .body("available", is(true));
    }
    
    /**
     * Test that we can check if an email is available
     */
    @Test
    @Order(2)
    public void checkEmailAvailability() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/users/check-email?email=" + TEST_EMAIL)
        .then()
            .statusCode(200)
            .body("available", is(true));
    }
    
    /**
     * Test that we can create a new user
     */
    @Test
    @Order(3)
    public void createNewUser() {
        Map<String, Object> user = new HashMap<>();
        user.put("username", TEST_USERNAME);
        user.put("email", TEST_EMAIL);
        user.put("displayName", "E2E Test User");
        user.put("password", TEST_PASSWORD);
        user.put("bio", "This is a test bio for E2E testing");
        user.put("location", "Test Location");
        user.put("website", "https://test.example.com");
        
        given()
            .contentType(ContentType.JSON)
            .body(user)
        .when()
            .post("/users")
        .then()
            .statusCode(201)
            .body("username", equalTo(TEST_USERNAME))
            .body("email", equalTo(TEST_EMAIL))
            .body("displayName", equalTo("E2E Test User"))
            .body("bio", equalTo("This is a test bio for E2E testing"))
            .body("location", equalTo("Test Location"))
            .body("website", equalTo("https://test.example.com"));
    }
    
    /**
     * Test that we can retrieve a user profile
     */
    @Test
    @Order(4)
    public void getUserProfile() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/users/" + TEST_USERNAME)
        .then()
            .statusCode(200)
            .body("username", equalTo(TEST_USERNAME))
            .body("displayName", equalTo("E2E Test User"));
    }
    
    /**
     * Test that we can search for users
     */
    @Test
    @Order(5)
    public void searchUsers() {
        given()
            .param("query", "Test User")
        .when()
            .get("/users/search")
        .then()
            .statusCode(200)
            .body("size()", greaterThanOrEqualTo(1))
            .body("find { it.username == '" + TEST_USERNAME + "' }.displayName", equalTo("E2E Test User"));
    }
    
    /**
     * Test that we can update a user profile
     */
    @Test
    @Order(6)
    public void updateUserProfile() {
        // Create a user with a different username for this test
        String updateTestUsername = "updatetestuser";
        String updateTestEmail = "updatetest@example.com";
        
        Map<String, Object> newUser = new HashMap<>();
        newUser.put("username", updateTestUsername);
        newUser.put("email", updateTestEmail);
        newUser.put("password", "securePassword123");
        newUser.put("displayName", "Update Test User");
        newUser.put("bio", "This is a test bio for update testing");
        newUser.put("location", "Test Location");
        newUser.put("website", "https://test.example.com");
        
        // Create the user first
        given()
            .contentType(ContentType.JSON)
            .body(newUser)
        .when()
            .post("/users")
        .then()
            .statusCode(201);
        
        // Now update the user profile
        Map<String, Object> updates = new HashMap<>();
        updates.put("id", null);
        updates.put("username", updateTestUsername);
        updates.put("email", updateTestEmail);
        updates.put("displayName", "Updated E2E Test User");
        updates.put("bio", "This is an updated bio for E2E testing");
        updates.put("location", null);
        updates.put("website", null);
        updates.put("profileImage", null);
        updates.put("headerImage", null);
        updates.put("verified", false);
        updates.put("createdAt", null);
        updates.put("followersCount", 0);
        updates.put("followingCount", 0);
        updates.put("password", null);
        
        given()
            .contentType(ContentType.JSON)
            .body(updates)
        .when()
            .put("/users/" + updateTestUsername)
        .then()
            .statusCode(200)
            .body("displayName", equalTo("Updated E2E Test User"))
            .body("bio", equalTo("This is an updated bio for E2E testing"));
    }
    
    /**
     * Test that we can delete a user
     * This should be the last test as it cleans up the test user
     */
    @Test
    @Order(7)
    public void deleteUser() {
        given()
        .when()
            .delete("/users/" + TEST_USERNAME)
        .then()
            .statusCode(204);
            
        // Verify user is gone
        given()
        .when()
            .get("/users/" + TEST_USERNAME)
        .then()
            .statusCode(404);
    }
} 