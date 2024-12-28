package com.cristianvelasquezp.microservicerecipes.users.controllers;

import com.cristianvelasquezp.microservicerecipes.users.entities.UserEntity;
import com.cristianvelasquezp.microservicerecipes.users.exceptions.DatabaseConnectionException;
import com.cristianvelasquezp.microservicerecipes.users.exceptions.UserNotFoundException;
import com.cristianvelasquezp.microservicerecipes.users.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<UserEntity> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable String id) {
        Long idLong = Long.parseLong(id);
        Optional<UserEntity> user = userService.getUserById(idLong);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            throw new UserNotFoundException("User with id: " + idLong + " not found");
        }
    }

    @PostMapping("/users")
    public UserEntity createUser(@RequestBody UserEntity user) {
        return userService.createUser(user);
    }

    @PutMapping("/users")
    public UserEntity updateUser(@RequestBody UserEntity user) {
        return userService.updateUser(user);
    }

    @DeleteMapping("/users/{id}")
    public boolean deleteUser(@PathVariable String id) {
        Long idLong = Long.parseLong(id);
        return userService.deleteUser(idLong);
    }


}
