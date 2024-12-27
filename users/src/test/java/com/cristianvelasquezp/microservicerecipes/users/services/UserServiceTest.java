package com.cristianvelasquezp.microservicerecipes.users.services;

import com.cristianvelasquezp.microservicerecipes.users.entities.UserEntity;
import com.cristianvelasquezp.microservicerecipes.users.exceptions.UserNotFoundException;
import com.cristianvelasquezp.microservicerecipes.users.repositories.UserRepository;
import com.cristianvelasquezp.microservicerecipes.users.utils.UserTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private List<UserEntity> users;

    @BeforeEach
    void setUp() {
        UserTestUtils userTestUtils = new UserTestUtils();
        users = userTestUtils.createUserEntities();
    }

    @DisplayName("Should return a list of user entities when there are users in the database")
    @Test
    void testGetAllUsers_whenIHaveUsersInTheDatabase_returnListOfUserEntities() {
        // Given
        List<UserEntity> expectedUsers = users;
        when(userRepository.findAll()).thenReturn(expectedUsers);

        // When
        List<UserEntity> actualUsers = userService.getAllUsers();

        // Then
        assertFalse(actualUsers.isEmpty());
        assertTrue(actualUsers.stream().allMatch(user -> user != null));
    }

    @DisplayName("Should return an empty list when there are no users in the database")
    @Test
    void testGetAllUsers_whenNoUsersInTheDatabase_returnEmptyList() {
        // Given
        List<UserEntity> expectedUsers = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(expectedUsers);

        // When
        List<UserEntity> actualUsers = userService.getAllUsers();

        // Then
        assertTrue(actualUsers.isEmpty());
    }

    @DisplayName("Should throw an exception with a specific message when the repository returns an error")
    @Test
    void testGetAllUsers_whenRepositoryThrowsException_throwExceptionWithSpecificMessage() {
        // Given
        when(userRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.getAllUsers());
        assertEquals("An error occurred while fetching users: Database error", exception.getMessage());
    }

    // Test for getUserById

    @DisplayName("Should return a user entity when the user exists in the database")
    @Test
    void testGetUserById_whenUserExists_returnUserEntity() {
        // Given
        UserEntity expectedUser = users.get(0);
        when(userRepository.findById(1L)).thenReturn(Optional.of(expectedUser));

        // When
        Optional<UserEntity> actualUser = userService.getUserById(1L);

        // Then
        assertTrue(actualUser.isPresent());
        assertEquals(1L, actualUser.get().getId());
        assertEquals("user1", actualUser.get().getUsername());
        assertEquals("user1@example.com", actualUser.get().getEmail());
        assertEquals("password1", actualUser.get().getPassword());
    }

    @DisplayName("Should return an empty optional when the user does not exist in the database")
    @Test
    void testGetUserById_whenUserDoesNotExist_returnEmptyOptional() {
        // Given
        when(userRepository.findById(12L)).thenReturn(Optional.empty());

        // When
        Optional<UserEntity> actualUser = userService.getUserById(12L);

        // Then
        assertTrue(actualUser.isEmpty());
    }

    @DisplayName("Should throw an exception with a specific message when the repository returns an error")
    @Test
    void testGetUserById_whenRepositoryThrowsException_throwExceptionWithSpecificMessage() {
        // Given
        long id = 12L;
        when(userRepository.findById(id)).thenThrow(new RuntimeException("Database error"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.getUserById(id));
        assertEquals("An error occurred while fetching user with id: " + id + " Database error", exception.getMessage());
    }

    // Test for createUser

    @DisplayName("Should return a user entity with id when the user is created")
    @Test
    void testCreateUser_whenUserIsCreated_returnUserEntityWithId() {
        // Given
        UserEntity user = new UserEntity();
        user.setUsername("JohnDoe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");
        when(userRepository.save(user)).thenReturn(getUserWithId(user));

        // When
        UserEntity actualUser = userService.createUser(user);

        // Then
        assertNotNull(actualUser);
        assertEquals(3L, actualUser.getId());
        assertEquals("JohnDoe", actualUser.getUsername());
        assertEquals("john.doe@example.com", actualUser.getEmail());
        assertEquals("password123", actualUser.getPassword());
    }

    @DisplayName("Should throw an exception with a specific message when the repository returns an error")
    @Test
    void testCreateUser_whenRepositoryThrowsException_throwExceptionWithSpecificMessage() {
        // Given
        UserEntity user = new UserEntity();
        user.setUsername("JohnDoe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");
        when(userRepository.save(user)).thenThrow(new RuntimeException("Database error"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.createUser(user));
        assertEquals("An error occurred while creating user: Database error", exception.getMessage());
    }

    //test for updateUser

    @DisplayName("Should return a user entity with updated values when the user is updated")
    @Test
    void testUpdateUser_whenUserIsUpdated_returnUserEntityWithUpdatedValues() {
        // Given
        UserEntity user = users.getFirst();
        user.setUsername("UpdatedUser");
        user.setEmail("updated.user@example.com");
        user.setPassword("newpassword123");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        // When
        UserEntity actualUser = userService.updateUser(user);

        // Then
        assertNotNull(actualUser);
        assertEquals(1L, actualUser.getId());
        assertEquals("UpdatedUser", actualUser.getUsername());
        assertEquals("updated.user@example.com", actualUser.getEmail());
        assertEquals("newpassword123", actualUser.getPassword());
    }

    @DisplayName("Should throw an exception with a specific message when the user does not have an id")
    @Test
    void testUpdateUser_whenUserDoesNotHaveId_throwExceptionWithSpecificMessage() {
        // Given
        UserEntity user = new UserEntity();
        user.setUsername("JohnDoe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.updateUser(user));
        assertEquals("User must have an id to be updated", exception.getMessage());
    }

    @DisplayName("Should throw an exception with a specific message when the user does not exist in the database")
    @Test
    void testUpdateUser_whenUserDoesNotExist_throwExceptionWithSpecificMessage() {
        // Given
        UserEntity user = users.getFirst();
        user.setId(11L);
        user.setUsername("Updated User");
        when(userRepository.findById(11L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.updateUser(user));
        assertEquals("An error occurred while updating user: User with id 11 not found", exception.getMessage());
    }

    @DisplayName("Should throw an exception with a specific message when the repository returns an error")
    @Test
    void testUpdateUser_whenRepositoryThrowsException_throwExceptionWithSpecificMessage() {
        // Given
        UserEntity user = users.getFirst();
        user.setUsername("UpdatedUser");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenThrow(new RuntimeException("Database error"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.updateUser(user));
        assertEquals("An error occurred while updating user: Database error", exception.getMessage());
    }

    // Test for deleteUser

    @DisplayName("Should return true when the user is deleted")
    @Test
    void testDeleteUser_whenUserIsDeleted_returnTrue() {
        // Given
        long id = 1L;
        when(userRepository.existsById(id)).thenReturn(true);
        // When
        boolean isDeleted = userService.deleteUser(id);

        // Then
        assertTrue(isDeleted);
    }

    @DisplayName("Should throw an exception with a specific message when the id does not exist in the database")
    @Test
    void testDeleteUser_whenIdDoesNotExist_throwExceptionWithSpecificMessage() {
        // Given
        long id = 11L;
        when(userRepository.existsById(id)).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(UserNotFoundException.class, () -> userService.deleteUser(id));
        assertEquals("An error occurred while deleting user: User with id 11 not found", exception.getMessage());
    }



    private UserEntity getUserWithId(UserEntity user) {
        user.setId(3L);
        return user;
    }
}