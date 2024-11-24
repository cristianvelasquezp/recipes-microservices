package com.cristianvelasquezp.microservicerecipes.recipes.repositories;

import com.cristianvelasquezp.microservicerecipes.recipes.Entities.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<RecipeEntity, Long> {

}
