package com.cristianvelasquezp.microservicerecipes.recipes.services.client;

import com.cristianvelasquezp.microservicerecipes.recipes.Entities.UserEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient("users")
public interface UserFeignClient {

    @GetMapping(value = "/users/{id}", consumes = "application/json")
    public ResponseEntity<UserEntity> getUserById(@PathVariable String id);
}
