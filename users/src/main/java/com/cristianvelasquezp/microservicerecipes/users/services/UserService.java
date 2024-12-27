package com.cristianvelasquezp.microservicerecipes.users.services;

import com.cristianvelasquezp.microservicerecipes.users.entities.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {
    public List<UserEntity> getAllUsers();

    public Optional<UserEntity> getUserById(Long id);

    public UserEntity createUser(UserEntity user);

    public UserEntity updateUser(UserEntity user);

    public boolean deleteUser(Long id);
}
