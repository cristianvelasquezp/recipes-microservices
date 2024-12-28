package com.cristianvelasquezp.microservicerecipes.users.controllers;

import com.cristianvelasquezp.microservicerecipes.users.entities.UserEntity;
import com.cristianvelasquezp.microservicerecipes.users.exceptions.DatabaseConnectionException;
import com.cristianvelasquezp.microservicerecipes.users.exceptions.UserNotFoundException;
import com.cristianvelasquezp.microservicerecipes.users.models.ErrorResponseModel;
import com.cristianvelasquezp.microservicerecipes.users.services.UserService;
import com.cristianvelasquezp.microservicerecipes.users.utils.UserTestUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    UserService userService;

    UserTestUtils userTestUtils;

    List<UserEntity> users;

    @BeforeEach
    void setUp() {
        userTestUtils = new UserTestUtils();
        users = userTestUtils.createUserEntities();
    }

    @Test
    @DisplayName("Should return all users when there are users in the database")
    void testGetAllUsers_whenThereAreUsersInDatabase_thenReturnAllUsers() throws Exception {
        //Given
        when(userService.getAllUsers()).thenReturn(users);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users");
        //When
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String responseBodyAsString = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<List<UserEntity>> jacksonTypeReference = new TypeReference<List<UserEntity>>() {};

        List<UserEntity> response = objectMapper.readValue(responseBodyAsString, jacksonTypeReference);

        //Then
        assertEquals(200, result.getResponse().getStatus());
        assertEquals(users.size(), response.size());
        assertEquals(1L, response.getFirst().getId());
        assertEquals("user1", response.getFirst().getUsername());
        assertEquals("user1@example.com", response.getFirst().getEmail());
    }

    @Test
    @DisplayName("Should return all users when there are no users in the database")
    void testGetAllUsers_whenThereAreNoUsersInDatabase_thenReturnEmptyList() throws Exception {
        //Given
        when(userService.getAllUsers()).thenReturn(List.of());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users");
        //When
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String responseBodyAsString = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<List<UserEntity>> jacksonTypeReference = new TypeReference<List<UserEntity>>() {};

        List<UserEntity> response = objectMapper.readValue(responseBodyAsString, jacksonTypeReference);

        //Then
        assertEquals(200, result.getResponse().getStatus());
        assertEquals(0, response.size());
    }

    @Test
    @DisplayName("Should return error 500 when there is an error fetching users")
    void testGetAllUsers_whenThereIsAnErrorFetchingUsers_thenReturnError500() throws Exception {
        //Given
        when(userService.getAllUsers()).thenThrow(new DatabaseConnectionException("An error occurred while fetching users: Connection refused", new Exception()));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users");
        //When
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        String responseBodyAsString = result.getResponse().getContentAsString();

        ErrorResponseModel response = objectMapper.readValue(responseBodyAsString, ErrorResponseModel.class);

        //Then
        assertEquals(500, result.getResponse().getStatus());
        assertEquals("An error occurred while fetching users: Connection refused", response.getMessage());
    }

    @Test
    @DisplayName("Should return a user when the id exists")
    void testGetUserById_whenIdExists_thenReturnUser() throws Exception {
        //Given
        when(userService.getUserById(1L)).thenReturn(Optional.ofNullable(users.get(0)));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/1");
        //When
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String responseBodyAsString = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();

        UserEntity response = objectMapper.readValue(responseBodyAsString, UserEntity.class);

        //Then
        assertEquals(200, result.getResponse().getStatus());
        assertEquals(1L, response.getId());
        assertEquals("user1", response.getUsername());
        assertEquals("user1@example.com", response.getEmail());
    }

    @Test
    @DisplayName("Should return error 404 when the id does not exist")
    void testGetUserById_whenIdDoesNotExist_thenReturnError404() throws Exception {
        //Given
        when(userService.getUserById(12L)).thenReturn(Optional.empty());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/12");
        //When
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        String responseBodyAsString = result.getResponse().getContentAsString();

        ErrorResponseModel response = objectMapper.readValue(responseBodyAsString, ErrorResponseModel.class);

        //Then
        assertEquals(404, result.getResponse().getStatus());
        assertEquals("User with id: 12 not found", response.getMessage());
    }

    @Test
    @DisplayName("Should return error 500 when there is an error fetching user by id")
    void testGetUserById_whenThereIsAnErrorFetchingUserById_thenReturnError500() throws Exception {
        //Given
        when(userService.getUserById(1L)).thenThrow(new DatabaseConnectionException("An error occurred while fetching user with id: 1 Connection refused", new Exception()));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/1");
        //When
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        String responseBodyAsString = result.getResponse().getContentAsString();

        ErrorResponseModel response = objectMapper.readValue(responseBodyAsString, ErrorResponseModel.class);

        //Then
        assertEquals(500, result.getResponse().getStatus());
        assertEquals("An error occurred while fetching user with id: 1 Connection refused", response.getMessage());
    }

    @Test
    @DisplayName("Should return a user when a user is created successfully")
    void testCreateUser_whenUserIsCreated_thenReturnUser() throws Exception {
        // Given
        UserEntity newUser = new UserEntity();
        newUser.setUsername("JohnDoe");
        newUser.setEmail("john.doe@example.com");
        UserEntity userCreated = userTestUtils.addUserEntity(newUser);

        when(userService.createUser(any(UserEntity.class))).thenReturn(userCreated);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users")
                .contentType("application/json")
                .accept("application/json")
                .content(new ObjectMapper().writeValueAsString(newUser));
        // When
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String responseBodyAsString = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();

        UserEntity response = objectMapper.readValue(responseBodyAsString, UserEntity.class);

        // Then
        assertEquals(200, result.getResponse().getStatus());
        assertEquals(3L, response.getId());
        assertEquals("JohnDoe", response.getUsername());
        assertEquals("john.doe@example.com", response.getEmail());
    }

    @Test
    @DisplayName("Should return error 500 when there is an error creating a user")
    void testCreateUser_whenThereIsAnErrorCreatingUser_thenReturnError500() throws Exception {
        // Given
        UserEntity newUser = new UserEntity();
        newUser.setUsername("JohnDoe");
        newUser.setEmail("john.doe@example.com");

        when(userService.createUser(any(UserEntity.class))).thenThrow(new DatabaseConnectionException("An error occurred while creating user: Connection refused"));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users")
                .contentType("application/json")
                .accept("application/json")
                .content(new ObjectMapper().writeValueAsString(newUser));
        // When
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        String responseBodyAsString = result.getResponse().getContentAsString();

        ErrorResponseModel response = objectMapper.readValue(responseBodyAsString, ErrorResponseModel.class);

        // Then
        assertEquals(500, result.getResponse().getStatus());
        assertEquals("An error occurred while creating user: Connection refused", response.getMessage());
    }

    @Test
    @DisplayName("Should return a user when a user is updated successfully")
    void testUpdateUser_whenUserIsUpdated_thenReturnUser() throws Exception {
        // Given
        UserEntity updatedUser = users.getFirst();
        updatedUser.setUsername("JohnDoeUpdated");

        when(userService.updateUser(any(UserEntity.class))).thenReturn(updatedUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/users")
                .contentType("application/json")
                .accept("application/json")
                .content(new ObjectMapper().writeValueAsString(updatedUser));
        // When
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String responseBodyAsString = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();

        UserEntity response = objectMapper.readValue(responseBodyAsString, UserEntity.class);

        // Then
        assertEquals(200, result.getResponse().getStatus());
        assertEquals(1L, response.getId());
        assertEquals("JohnDoeUpdated", response.getUsername());
        assertEquals("user1@example.com", response.getEmail());
    }

    @Test
    @DisplayName("Should return error 400 when the user does not have an id")
    void testUpdateUser_whenUserDoesNotHaveId_thenReturnError400() throws Exception {
        // Given
        UserEntity updatedUser = users.getFirst();
        updatedUser.setId(null);

        when(userService.updateUser(any(UserEntity.class))).thenThrow(new IllegalArgumentException("User must have an id to be updated"));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/users")
                .contentType("application/json")
                .accept("application/json")
                .content(new ObjectMapper().writeValueAsString(updatedUser));
        // When
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        String responseBodyAsString = result.getResponse().getContentAsString();

        ErrorResponseModel response = objectMapper.readValue(responseBodyAsString, ErrorResponseModel.class);

        // Then
        assertEquals(400, result.getResponse().getStatus());
        assertEquals("User must have an id to be updated", response.getMessage());
    }

    @Test
    @DisplayName("Should return true when a user is deleted successfully")
    void testDeleteUser_whenUserIsDeleted_thenReturnTrue() throws Exception {
        // Given
        when(userService.deleteUser(1L)).thenReturn(true);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/users/1");
        // When
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String responseBodyAsString = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();

        boolean response = objectMapper.readValue(responseBodyAsString, Boolean.class);

        // Then
        assertEquals(200, result.getResponse().getStatus());
        assertTrue(response);
    }

    @Test
    @DisplayName("Should return error 404 when the user does not exist")
    void testDeleteUser_whenUserDoesNotExist_thenReturnError404() throws Exception {
        // Given
        when(userService.deleteUser(12L)).thenThrow(new UserNotFoundException("An error occurred while deleting user: User with id 12 not found"));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/users/12");
        // When
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        String responseBodyAsString = result.getResponse().getContentAsString();

        ErrorResponseModel response = objectMapper.readValue(responseBodyAsString, ErrorResponseModel.class);

        // Then
        assertEquals(404, result.getResponse().getStatus());
        assertEquals("An error occurred while deleting user: User with id 12 not found", response.getMessage());
    }

    @Test
    @DisplayName("Should return error 500 when there is an error deleting a user")
    void testDeleteUser_whenThereIsAnErrorDeletingUser_thenReturnError500() throws Exception {
        // Given
        when(userService.deleteUser(1L)).thenThrow(new DatabaseConnectionException("An error occurred while deleting user with id: 1 Connection refused"));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/users/1");
        // When
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        String responseBodyAsString = result.getResponse().getContentAsString();

        ErrorResponseModel response = objectMapper.readValue(responseBodyAsString, ErrorResponseModel.class);

        // Then
        assertEquals(500, result.getResponse().getStatus());
        assertEquals("An error occurred while deleting user with id: 1 Connection refused", response.getMessage());
    }

}