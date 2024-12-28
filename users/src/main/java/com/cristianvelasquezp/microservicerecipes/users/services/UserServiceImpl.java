package com.cristianvelasquezp.microservicerecipes.users.services;

import com.cristianvelasquezp.microservicerecipes.users.entities.UserEntity;
import com.cristianvelasquezp.microservicerecipes.users.exceptions.DatabaseConnectionException;
import com.cristianvelasquezp.microservicerecipes.users.exceptions.UserNotFoundException;
import com.cristianvelasquezp.microservicerecipes.users.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserEntity> getAllUsers() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            throw new DatabaseConnectionException("An error occurred while fetching users: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<UserEntity> getUserById(Long id) {
        try {
            return userRepository.findById(id);
        } catch (Exception e) {
            throw new DatabaseConnectionException("An error occurred while fetching user with id: " + id + " " + e.getMessage(), e);
        }
    }

    @Override
    public UserEntity createUser(UserEntity user) {
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            Logger.getGlobal().severe("An error occurred while creating user: " + e.getMessage());
            throw new DatabaseConnectionException("An error occurred while creating user: " + e.getMessage(), e);
        }
    }

    @Override
    public UserEntity updateUser(UserEntity user) {
        if (user.getId() == null) {
            throw new IllegalArgumentException("User must have an id to be updated");
        }
        userRepository.findById(user.getId()).orElseThrow(() -> new UserNotFoundException("An error occurred while updating user: User with id " + user.getId() + " not found"));
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            throw new DatabaseConnectionException("An error occurred while updating user: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("An error occurred while deleting user: User with id " + id + " not found");
        }
        try {
            userRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new DatabaseConnectionException("An error occurred while deleting user with id: " + id + " " + e.getMessage(), e);
        }
    }
}
