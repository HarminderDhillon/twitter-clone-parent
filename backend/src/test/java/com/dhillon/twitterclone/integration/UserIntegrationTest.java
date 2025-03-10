package com.dhillon.twitterclone.integration;

import com.dhillon.twitterclone.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for the User API.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class UserIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    public void getUserByUsername_ReturnsUser() throws Exception {
        // First create a user
        UserDto userDto = new UserDto(
            null,
            "testuser",
            "test@example.com",
            "Test User",
            null,
            null,
            null,
            null,
            null,
            false,
            null,
            0,
            0,
            "password123"
        );

        MvcResult createResult = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andReturn();

        // Then get the user by username
        mockMvc.perform(get("/users/testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.displayName").value("Test User"));
    }
    
    @Test
    public void createUser_WithValidData_CreatesUserAndReturnsIt() throws Exception {
        UserDto userDto = new UserDto(
            null,
            "newuser",
            "newuser@example.com",
            "New User",
            null,
            null,
            null,
            null,
            null,
            false,
            null,
            0,
            0,
            "password123"
        );

        MvcResult result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andReturn();

        UserDto newUserDto = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                UserDto.class
        );

        assertThat(newUserDto.username()).isEqualTo("newuser");
        assertThat(newUserDto.email()).isEqualTo("newuser@example.com");
        assertThat(newUserDto.displayName()).isEqualTo("New User");
        assertThat(newUserDto.id()).isNotNull();
    }
    
    @Test
    public void updateUser_WithValidData_UpdatesUserAndReturnsIt() throws Exception {
        // First create a user
        UserDto userDto = new UserDto(
            null,
            "updateuser",
            "updateuser@example.com",
            "Update User",
            null,
            null,
            null,
            null,
            null,
            false,
            null,
            0,
            0,
            "password123"
        );

        MvcResult createResult = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andReturn();

        UserDto createdUserDto = objectMapper.readValue(
                createResult.getResponse().getContentAsString(),
                UserDto.class
        );

        // Then update the user
        UserDto updateDto = new UserDto(
            createdUserDto.id(),
            createdUserDto.username(),
            createdUserDto.email(),
            "Updated Name",
            "New bio",
            "New York",
            "https://example.com",
            null,
            null,
            false,
            null,
            0,
            0,
            null
        );

        MvcResult updateResult = mockMvc.perform(put("/users/" + createdUserDto.username())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andReturn();

        UserDto updatedUserDto = objectMapper.readValue(
                updateResult.getResponse().getContentAsString(),
                UserDto.class
        );

        assertThat(updatedUserDto.displayName()).isEqualTo("Updated Name");
        assertThat(updatedUserDto.bio()).isEqualTo("New bio");
        assertThat(updatedUserDto.location()).isEqualTo("New York");
        assertThat(updatedUserDto.website()).isEqualTo("https://example.com");
    }
    
    @Test
    public void deleteUser_RemovesUserFromDatabase() throws Exception {
        // First create a user
        UserDto userDto = new UserDto(
            null,
            "deleteuser",
            "deleteuser@example.com",
            "Delete User",
            null,
            null,
            null,
            null,
            null,
            false,
            null,
            0,
            0,
            "password123"
        );

        MvcResult createResult = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andReturn();

        // Then delete the user
        mockMvc.perform(delete("/users/deleteuser"))
                .andExpect(status().isNoContent());

        // Verify user is deleted
        mockMvc.perform(get("/users/deleteuser"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void searchUsers_FindsMatchingUsers() throws Exception {
        // Create some users
        UserDto user1 = new UserDto(
            null,
            "johndoe",
            "john@example.com",
            "John Doe",
            null,
            null,
            null,
            null,
            null,
            false,
            null,
            0,
            0,
            "password123"
        );

        UserDto user2 = new UserDto(
            null,
            "janedoe",
            "jane@example.com",
            "Jane Doe",
            null,
            null,
            null,
            null,
            null,
            false,
            null,
            0,
            0,
            "password123"
        );

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user2)))
                .andExpect(status().isCreated());

        // Search for users with "doe" in their name
        mockMvc.perform(get("/users/search?query=doe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].displayName").value("John Doe"))
                .andExpect(jsonPath("$[1].displayName").value("Jane Doe"));
    }
    
    @Test
    public void checkUsername_WithAvailableUsername_ReturnsTrue() throws Exception {
        mockMvc.perform(get("/users/check-username?username=availableuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.available").value(true));
    }
    
    @Test
    public void checkUsername_WithExistingUsername_ReturnsFalse() throws Exception {
        // First create a user
        UserDto userDto = new UserDto(
            null,
            "existinguser",
            "existing@example.com",
            "Existing User",
            null,
            null,
            null,
            null,
            null,
            false,
            null,
            0,
            0,
            "password123"
        );

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated());

        // Check if username is available
        mockMvc.perform(get("/users/check-username?username=existinguser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.available").value(false));
    }
    
    @Test
    public void checkEmail_WithAvailableEmail_ReturnsTrue() throws Exception {
        mockMvc.perform(get("/users/check-email?email=available@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.available").value(true));
    }
    
    @Test
    public void checkEmail_WithExistingEmail_ReturnsFalse() throws Exception {
        // First create a user
        UserDto userDto = new UserDto(
            null,
            "emailuser",
            "existing-email@example.com",
            "Email User",
            null,
            null,
            null,
            null,
            null,
            false,
            null,
            0,
            0,
            "password123"
        );

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated());

        // Check if email is available
        mockMvc.perform(get("/users/check-email?email=existing-email@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.available").value(false));
    }
} 