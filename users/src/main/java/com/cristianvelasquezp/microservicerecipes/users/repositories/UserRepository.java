package com.cristianvelasquezp.microservicerecipes.users.repositories;

import com.cristianvelasquezp.microservicerecipes.users.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
