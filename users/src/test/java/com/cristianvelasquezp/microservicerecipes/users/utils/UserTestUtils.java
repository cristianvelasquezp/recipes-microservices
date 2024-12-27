package com.cristianvelasquezp.microservicerecipes.users.utils;

import com.cristianvelasquezp.microservicerecipes.users.entities.UserEntity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class UserTestUtils {
    private final List<UserEntity> userEntities = new ArrayList<>();

    public List<UserEntity> createUserEntities() {
        UserEntity user1 = new UserEntity();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setEmail("user1@example.com");
        user1.setPassword("password1");
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        user1.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        user1.setStatus("active");
        userEntities.add(user1);

        UserEntity user2 = new UserEntity();
        user2.setId(2L);
        user2.setUsername("user2");
        user2.setEmail("user2@example.com");
        user2.setPassword("password2");
        user2.setFirstName("Jane");
        user2.setLastName("Doe");
        user2.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        user2.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        user2.setStatus("active");
        userEntities.add(user2);

        // Add more users as needed

        return userEntities;
    }

    public UserEntity addUserEntity(UserEntity user) {
        Long id = (long) (userEntities.size() + 1);
        user.setId(id);
        userEntities.add(user);
        return userEntities.get(userEntities.size() - 1);
    }
}
